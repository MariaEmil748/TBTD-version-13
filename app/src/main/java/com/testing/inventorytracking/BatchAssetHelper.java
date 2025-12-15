package com.testing.inventorytracking;

import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Helper class for batch processing multiple assets in a single API call
 * This significantly improves performance by reducing 20 requests to 1 request
 */
public class BatchAssetHelper {

    /**
     * Converts ArrayList to comma-separated string for batch API
     * Example: ["123", "456", "789"] -> "123,456,789"
     */
    public static String convertListToCommaSeparated(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Process batch asset request - MUCH FASTER than individual requests
     * Usage: Replace the for-loop in ScanAndCheck() with this single call
     * 
     * @param fragment The calling fragment
     * @param assetNumbers Comma-separated asset numbers (e.g., "123,456,789")
     * @param server Company name ("teie" or "tbtd")
     * @param isAssetNumber True for asset numbers, false for serial numbers
     * @param sapClient The SAP client interface
     * @param callback Callback to handle the response
     */
    public static void fetchAssetsBatch(
            Fragment fragment,
            String assetNumbers,
            String server,
            boolean isAssetNumber,
            SapClient sapClient,
            BatchAssetCallback callback) {

        Call<List<Tiasset>> call;

        // Choose correct batch endpoint
        if (isAssetNumber) {
            // Batch request for asset numbers
            call = server.equalsIgnoreCase("teie") ? 
                   sapClient.getAssetsBatchTEIE(assetNumbers) : 
                   sapClient.getAssetsBatchTBTD(assetNumbers);
        } else {
            // Batch request for serial numbers
            call = server.equalsIgnoreCase("teie") ? 
                   sapClient.getAssetsBatchSerialTEIE(assetNumbers) : 
                   sapClient.getAssetsBatchSerialTBTD(assetNumbers);
        }

        // Execute the batch request
        call.enqueue(new Callback<List<Tiasset>>() {
            @Override
            public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    callback.onFailure("No data retrieved from server");
                    return;
                }

                // Success - return all assets at once
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                callback.onFailure("Network Error: " + t.getMessage());
            }
        });
    }

    /**
     * Remove duplicates from asset list efficiently using HashSet
     * 
     * @param newAssets New assets to add
     * @param existingAssets Existing cached assets
     * @return List of unique new assets (no duplicates)
     */
    public static List<Tiasset> filterDuplicates(List<Tiasset> newAssets, List<Tiasset> existingAssets) {
        // Create HashSet of existing asset numbers for O(1) lookup
        Set<String> existingAssetNumbers = new HashSet<>();
        for (Tiasset asset : existingAssets) {
            existingAssetNumbers.add(asset.getANLN1());
        }

        // Filter out duplicates
        List<Tiasset> uniqueAssets = new ArrayList<>();
        for (Tiasset asset : newAssets) {
            if (!existingAssetNumbers.contains(asset.getANLN1())) {
                uniqueAssets.add(asset);
            }
        }

        return uniqueAssets;
    }

    /**
     * Callback interface for batch asset fetching
     */
    public interface BatchAssetCallback {
        void onSuccess(List<Tiasset> assets);
        void onFailure(String errorMessage);
    }
}
