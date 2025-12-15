package com.testing.inventorytracking;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadDataDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ReadDataDetailsFragment extends Fragment implements HandheldAdapter.PassData, androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private EditText serialnumber;
    private TextView totalCount, totalTVs, roomNumberS, actualS, diffS;
    private RadioButton srCheck, asCheck, roomCheck;
    private Button GetData;
    private LinearLayout linerRoom;
    private String server, transfer, user, pass, url, address;
    private ProgressBar simpleProgressBar;
    private RecyclerView recyclerView;
    private HandheldAdapter.PassData ref;
    final List<Tiasset> finalList = new ArrayList<>();
    final List<Tiasset> cachedlist = new ArrayList<>();
    HandheldAdapter adapter;
    private int actualStock, diffStock;
    int c = 0;
    int count = 0;
    int sum = 1;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReadDataDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.Search_item);
        MenuItem menuItem2 = menu.findItem(R.id.savaRoom);
        MenuItem menuItem3 = menu.findItem(R.id.logout);
        MenuItem menuItem4 = menu.findItem(R.id.savasummary);
        MenuItem menuItem5 = menu.findItem(R.id.delete);
        MenuItem menuItem6 = menu.findItem(R.id.AssignAssets);


        if (transfer.equals("transfer")) {
            menuItem.setVisible(true);
            menuItem3.setVisible(false);
            menuItem4.setVisible(false);
            menuItem2.setVisible(false);
            menuItem5.setVisible(false);
            menuItem6.setVisible(false);

        } else {
            menuItem.setVisible(true);
            menuItem3.setVisible(false);
            menuItem4.setVisible(true);
            menuItem2.setVisible(true);
            menuItem5.setVisible(true);
            menuItem6.setVisible(false);
        }
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    /**
     * Use this factory method to  a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadDataDetailsFragment newInstance(String param1, String param2) {
        ReadDataDetailsFragment fragment = new ReadDataDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(false);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(false);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        sum = 1;
        server = getArguments().getString("server");
        user = getArguments().getString("user");
        pass = getArguments().getString("pass");
        transfer = getArguments().getString("notaudit");
        address = getArguments().getString("Address");
        final View view = inflater.inflate(R.layout.fragment_read_details, container, false);
        Toast.makeText(getContext(), "✨ Choose 'Asset/Serial' to auto-search both OR 'Room' for room scanning", Toast.LENGTH_LONG).show();
        c = ((WelcomeActivity) getActivity()).Checkconnection();
        if (c == 2) {
            Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
        }
        srCheck = view.findViewById(R.id.serialnumberradio);
        asCheck = view.findViewById(R.id.assetnumberradio);
        roomCheck = view.findViewById(R.id.Roomradio);
        linerRoom = view.findViewById(R.id.Liner_Room);
        
        // ✅ Show radio buttons - user must choose!
        asCheck.setText("Asset/Serial (Auto)");
        srCheck.setVisibility(View.GONE); // Hide serial button (merged with asset)
        asCheck.setVisibility(View.VISIBLE);
        roomCheck.setVisibility(View.VISIBLE);
        
        ref = this;
        totalTVs = view.findViewById(R.id.totalTxS);
        final ArrayList<String> Multiarraylis = new ArrayList<>();
        serialnumber = view.findViewById(R.id.serialNumberHand);
        serialnumber.setEnabled(false);  // ✅ Disabled until user selects radio button
        totalCount = view.findViewById(R.id.totalCount);
        GetData = view.findViewById(R.id.getdata);
        roomNumberS = view.findViewById(R.id.room_NumberStock);
        actualS = view.findViewById(R.id.actual_AssetsStock);
        diffS = view.findViewById(R.id.difference_CountStock);
        simpleProgressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.rec2);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Initialize empty adapter to suppress "No adapter attached" warning
        adapter = new HandheldAdapter(ref, finalList, server);
        recyclerView.setAdapter(adapter);
        new RoomGetcontrollar(getContext(), finalList, Multiarraylis).execute();
        if (transfer.equals("not_transfer")) {
            ((WelcomeActivity) getActivity())
                    .setActionBarTitle("Asset Stock Take", " ");
        } else {
            ((WelcomeActivity) getActivity())
                    .setActionBarTitle("Asset Transfer", " ");
            linerRoom.setVisibility(View.GONE);
            roomCheck.setVisibility(View.GONE);
        }

        // ✅ Radio button listeners - user chooses between Asset/Serial OR Room
        asCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serialnumber.setEnabled(true);
                    serialnumber.requestFocus();
                    linerRoom.setVisibility(View.VISIBLE); // ✅ Show room section for asset/serial too!
                }
            }
        });

        roomCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serialnumber.setEnabled(true);
                    serialnumber.requestFocus();
                    linerRoom.setVisibility(View.VISIBLE); // ✅ Show room section for room scanning
                }
            }
        });

        serialnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Runnable r = new Runnable() {
                    public void run() {
                        String  assetnumber = serialnumber.getText().toString();
                        if(!Multiarraylis.contains(assetnumber)&& !assetnumber.isEmpty()){
                            totalCount.setVisibility(View.VISIBLE);
                            Multiarraylis.add(assetnumber);
                            serialnumber.getText().clear();
                            totalCount.setText(Multiarraylis.size()+"");
                        }else {
                            serialnumber.getText().clear();
                        }
                        if (Multiarraylis.size() == 50 ){
                            serialnumber.setVisibility(View.GONE);
                            serialnumber.clearFocus();
                        }
                    }
                };

//                if (!TextUtils.isEmpty(serialnumber.getText())){
//                    serialnumber.setEnabled(false);
//                }else {
//                    serialnumber.setEnabled(true);
//                }
                Handler h = new Handler();
                h.postDelayed(r, 100);  // Reduced from 300ms to 100ms for faster scanning
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        GetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = ((WelcomeActivity) getActivity()).Checkconnection();
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Multiarraylis.size() == 0) {
                    Toast.makeText(getContext(), "Scan Barcode ", Toast.LENGTH_SHORT).show();
                    return;
                }


                ScanAndCheck(Multiarraylis);
//                serialnumber.getText().clear();

                simpleProgressBar.setVisibility(View.VISIBLE);
                simpleProgressBar.animate();

//                if (!TextUtils.isEmpty(serialnumber.getText())){
//                    serialnumber.setEnabled(false);
//                }else {
//                    serialnumber.setEnabled(true);
//                }

            }
        });
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savaRoom:
                if (finalList.size() == 0) {
                    Toast.makeText(getContext(), "There is Nothing Scanned For Saving !!", Toast.LENGTH_SHORT).show();
                } else {
                    new Roomcontrollar(getContext(), "DELETE").execute();
                    new Roomcontrollar(getContext(), "INSERT", finalList).execute();
                }
                return true;
            case R.id.savasummary:
                if (finalList.size() == 0) {
                    Toast.makeText(getContext(), "There is Nothing Scanned For Uploading !!", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Use SINGLE SESSION for all uploads - much more efficient!
                    sum = 1;
                    Toast.makeText(getContext(), "📤 Starting upload of " + finalList.size() + " assets...", Toast.LENGTH_LONG).show();
                    Log.e("UploadStart", "Uploading " + finalList.size() + " assets in ONE SESSION");
                    
                    // Convert finalList to array and pass ALL assets to single AsyncTask
                    Tiasset[] assetsArray = finalList.toArray(new Tiasset[0]);
                    new UploadSummaryBatch().execute(assetsArray);
                }
                return true;
            case R.id.delete:
                if (finalList.size() == 0) {
                    Toast.makeText(getContext(), "There is Nothing Scanned For Deleting !!", Toast.LENGTH_SHORT).show();
                } else {
                    new Roomcontrollar(getContext(), "DELETE").execute();
                    finalList.clear();
                    adapter.updatingList(finalList);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ScanAndCheck(final ArrayList<String> Multiarraylis) {
        // ✅ OPTIMIZED: Concurrent processing - ALL requests at once instead of chunks
        // Much faster than sequential processing!
        if (c == 2) {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Multiarraylis == null || Multiarraylis.isEmpty()) {
            Toast.makeText(getContext(), "No items scanned", Toast.LENGTH_SHORT).show();
            return;
        }

        // Normalize and dedupe
        Multiarraylis.remove("");
        Multiarraylis.remove("\n");
        Set<String> set = new HashSet<>(Multiarraylis);
        Multiarraylis.clear();
        set.remove("");
        Multiarraylis.addAll(set);

        if (Multiarraylis.isEmpty()) {
            Toast.makeText(getContext(), "No valid items to search", Toast.LENGTH_SHORT).show();
            return;
        }

        // Limit to first 50 items
        final ArrayList<String> toProcess = new ArrayList<>();
        for (int i = 0; i < Multiarraylis.size() && i < 50; i++) {
            toProcess.add(Multiarraylis.get(i));
        }

        simpleProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Fetching " + toProcess.size() + " assets (10 at a time)...", Toast.LENGTH_SHORT).show();

        // Prepare Retrofit - reuse same connection
        url = AppConfig.getAssetsUrl(server);
        RetrofitClient retrofitClient = new RetrofitClient();
        Retrofit retrofit = retrofitClient.connect(url, user, pass);
        final SapClient sapClient = retrofit.create(SapClient.class);

        // Results collector
        final List<Tiasset> tempResults = new ArrayList<>();
        final AtomicInteger currentBatchIndex = new AtomicInteger(0);

        // ✅ BATCH processing - processes 10 assets at a time (max 10 SAP sessions)
        fetchNextBatch(toProcess, sapClient, tempResults, currentBatchIndex);

        // Clear scanned list (UI input)
        Multiarraylis.clear();
    }
    
    // ✅ BATCH METHOD: Fetch 10 assets at a time (max 10 SAP sessions)
    private void fetchNextBatch(final ArrayList<String> toProcess, final SapClient sapClient,
                                 final List<Tiasset> tempResults, final AtomicInteger currentBatchIndex) {
        final int batchSize = 10;
        final int startIndex = currentBatchIndex.get() * batchSize;
        
        if (startIndex >= toProcess.size()) {
            // All batches done - update UI
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    simpleProgressBar.setVisibility(View.GONE);
                    updateUI(tempResults);
                }
            });
            return;
        }

        // Calculate end index for this batch
        final int endIndex = Math.min(startIndex + batchSize, toProcess.size());
        final int totalRequests = (endIndex - startIndex) * 2; // asset + serial for each
        final AtomicInteger completedInBatch = new AtomicInteger(0);

        // Process 10 assets concurrently
        for (int i = startIndex; i < endIndex; i++) {
            final String value = toProcess.get(i);
            final String assetValue = value.matches("\\d+") ? "00000" + value : value;

            // Try asset number first
            Call<List<Tiasset>> assetCall = server.equalsIgnoreCase("tbtd")
                    ? sapClient.getassettbtd(assetValue)
                    : sapClient.getassetteie(assetValue);

            assetCall.enqueue(new Callback<List<Tiasset>>() {
                @Override
                public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        Tiasset asset = response.body().get(0);
                        if (asset != null && asset.getANLN1() != null && !asset.getANLN1().trim().isEmpty()) {
                            synchronized (tempResults) {
                                boolean exists = false;
                                for (Tiasset e : tempResults) {
                                    if (e.getANLN1().equals(asset.getANLN1())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) tempResults.add(asset);
                            }
                        }
                    }

                    if (completedInBatch.incrementAndGet() == totalRequests) {
                        // This batch is done, move to next batch
                        currentBatchIndex.incrementAndGet();
                        fetchNextBatch(toProcess, sapClient, tempResults, currentBatchIndex);
                    }
                }

                @Override
                public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                    if (completedInBatch.incrementAndGet() == totalRequests) {
                        // This batch is done, move to next batch
                        currentBatchIndex.incrementAndGet();
                        fetchNextBatch(toProcess, sapClient, tempResults, currentBatchIndex);
                    }
                }
            });

            // Try serial number
            Call<List<Tiasset>> serialCall = server.equalsIgnoreCase("tbtd")
                    ? sapClient.getassetS(value)
                    : sapClient.getassetteieS(value);

            serialCall.enqueue(new Callback<List<Tiasset>>() {
                @Override
                public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        Tiasset asset = response.body().get(0);
                        if (asset != null && asset.getANLN1() != null && !asset.getANLN1().trim().isEmpty()) {
                            synchronized (tempResults) {
                                boolean exists = false;
                                for (Tiasset e : tempResults) {
                                    if (e.getANLN1().equals(asset.getANLN1())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) tempResults.add(asset);
                            }
                        }
                    }

                    if (completedInBatch.incrementAndGet() == totalRequests) {
                        // This batch is done, move to next batch
                        currentBatchIndex.incrementAndGet();
                        fetchNextBatch(toProcess, sapClient, tempResults, currentBatchIndex);
                    }
                }

                @Override
                public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                    if (completedInBatch.incrementAndGet() == totalRequests) {
                        // This batch is done, move to next batch
                        currentBatchIndex.incrementAndGet();
                        fetchNextBatch(toProcess, sapClient, tempResults, currentBatchIndex);
                    }
                }
            });
        }
    }
    
    private void updateUI(List<Tiasset> foundAssets) {
        simpleProgressBar.setVisibility(View.GONE);
        
        if (foundAssets.isEmpty()) {
            Toast.makeText(getContext(), "No assets found", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Add unique results to final list
        for (Tiasset asset : foundAssets) {
            boolean found = false;
            for (Tiasset existing : cachedlist) {
                if (existing.getANLN1().equals(asset.getANLN1())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                finalList.add(asset);
            }
        }
        
        cachedlist.clear();
        cachedlist.addAll(finalList);
        
        // Update UI
        adapter = new HandheldAdapter(ref, finalList, server);
        recyclerView.setAdapter(adapter);
        totalTVs.setVisibility(View.VISIBLE);
        totalCount.setVisibility(View.VISIBLE);
        totalCount.setText(finalList.size() + " ");
        
        // Update stock difference
        if (!actualS.getText().toString().isEmpty()) {
            actualStock = Integer.parseInt(actualS.getText().toString());
            if (finalList.size() > actualStock) {
                diffStock = finalList.size() - actualStock;
                diffS.setText("+" + diffStock + " ");
            } else if (finalList.size() == actualStock) {
                diffS.setText("0");
            } else {
                diffStock = actualStock - finalList.size();
                diffS.setText("-" + diffStock + " ");
            }
        }
        
        Toast.makeText(getContext(), 
            "✅ Found " + foundAssets.size() + " assets (searched both asset# and serial#)", 
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendData(Tiasset tiasset) {
        if (c == 2) {
            Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tiasset == null) {
            Toast.makeText(getContext(), "Asset data is null", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogFragment dialogFragment = EditDialog.newInstanse(tiasset, server, transfer, user, pass, url, address);
        dialogFragment.show(getParentFragmentManager(), "EditDialog");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (finalList.size() == 0) {
            return false;
        }
        List<Tiasset> newList = new ArrayList<>();
        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).ANLN1.contains(s) || finalList.get(i).ANLHTXT.contains(s)) {
                newList.add(finalList.get(i));
            }
        }
        adapter.updatingList(newList);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((WelcomeActivity) getActivity())
                .setActionBarTitle("Asset Tracking System", " ");
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(true);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(true);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(true);
    }


    public class RoomGetcontrollar extends AsyncTask<List<Tiasset>, Void, List<Tiasset>> {

        Context context;
        List<Tiasset> list;
        List<Tiasset> offlineData;
        ArrayList<String> multiarraylist;

        public RoomGetcontrollar(Context context, List<Tiasset> list, ArrayList<String> multiarraylist) {
            this.context = context;
            this.list = list;
            this.multiarraylist = multiarraylist;
        }


        @Override
        protected List<Tiasset> doInBackground(List<Tiasset>... lists) {
            offlineData = MyRoomDatabase.getInstance(context).roominterface().getAll();
            return offlineData;
        }

        @Override
        protected void onPostExecute(List<Tiasset> offlineData) {
            super.onPostExecute(offlineData);

            if (offlineData.size() == 0) {
                Toast.makeText(getContext(), " No Data Saved ", Toast.LENGTH_SHORT).show();
            } else {
                finalList.addAll(offlineData);
                adapter = new HandheldAdapter(ref, finalList, server);
                recyclerView.setAdapter(adapter);
            }
            cachedlist.addAll(offlineData);
        }

    }

    //  ======================  suuuuuuuuuuuuuuuummarrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrry ====================

    public class UploadSummary extends AsyncTask<Tiasset, String, okhttp3.Response> {
        @Override
        protected okhttp3.Response doInBackground(Tiasset... tiassets) {
            // ✅ Check internet connection
            int connectionStatus = ((WelcomeActivity) getActivity()).Checkconnection();
            if (connectionStatus == 2) {
                // No internet connection
                return null;
            }
            
            OkHttpClient clientget = new OkHttpClient().newBuilder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();
            
            // ✅ Use correct summary endpoint with SAP client
            String urlsummaery = AppConfig.getSummaryUrl(server);
            Log.e("UploadURL_Full", "Complete URL: " + urlsummaery);

            String credential = Credentials.basic(user, pass);
            Request requestget = new Request.Builder()
                    .url(urlsummaery)
                    .get()
                    .header("Authorization", credential)
                    .header("content-type", "application/json")
                    .header("X-CSRF-Token", "fetch")
                    .build();
            okhttp3.Response responseget = null;
            try {
                responseget = clientget.newCall(requestget).execute();
                Log.e("CSRF_GET", "Response code: " + responseget.code());
            } catch (IOException e) {
                Log.e("UploadError", "Failed to get CSRF token: " + e.getMessage());
                return null; // ✅ Return null if can't get CSRF token
            }
            
            // ✅ Check if GET request was successful
            if (responseget == null || !responseget.isSuccessful()) {
                Log.e("UploadError", "CSRF token request failed");
                return null;
            }
            
            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonToPost = new JSONObject();
            try {
                String costupdate = tiassets[0].KOSTL ;
                if (tiassets[0].KOSTL.length() == 3) {
                    tiassets[0].setKOSTL( "0000000" + costupdate);
                } else {
                    tiassets[0].setKOSTL( "000000" + costupdate);
                }
                jsonToPost.put("EMP_ID", tiassets[0].PERNR);
                jsonToPost.put("ASSET_NO", tiassets[0].ANLN1);
                jsonToPost.put("VALIDDATE", tiassets[0].ADATU);
                jsonToPost.put("BRAND", tiassets[0].ORD41);
                jsonToPost.put("PLANT", tiassets[0].NAME1);
                jsonToPost.put("COSTCENTER", tiassets[0].KTEXT);
                jsonToPost.put("MODEL", tiassets[0].ORD42);
                jsonToPost.put("SERIAL_NO", tiassets[0].ANLHTXT);
                jsonToPost.put("KOSTL", costupdate );
                jsonToPost.put("WERKS", tiassets[0].WERKS);
                jsonToPost.put("EMP_NAME", tiassets[0].ENAME);
                jsonToPost.put("DEP", tiassets[0].DEPARTMENT);
                jsonToPost.put("ETITLE", tiassets[0].TITLE);
                jsonToPost.put("ASSET_STATUS", tiassets[0].STATUS);
                jsonToPost.put("ROOMNO", tiassets[0].ROOMNO);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("UploadError", "JSON creation failed: " + e.getMessage());
            }
            
            RequestBody body = RequestBody.create(mediaType, jsonToPost.toString());
            
            // ✅ Safely get CSRF token and Cookie
            String csrfToken = responseget.headers().get("x-csrf-token");
            String cookie = responseget.headers().get("Set-Cookie");
            
            if (csrfToken == null || csrfToken.isEmpty()) {
                Log.e("UploadError", "No CSRF token received from server");
                return null;
            }
            
            Headers headers = new Headers.Builder()
                    .add("content-type", "application/json")
                    .add("X-CSRF-Token", csrfToken)
                    .add("Cookie", cookie != null ? cookie : "")
                    .add("Authorization", credential)
                    .build();
            Request request = new Request.Builder()
                    .url(urlsummaery)
                    .post(body)
                    .headers(headers)
                    .build();
            okhttp3.Response response = null;
            try {
                response = clientget.newCall(request).execute();
                Log.e("UploadResponse", "Upload success: " + response.isSuccessful() + ", Code: " + response.code());
                if (!response.isSuccessful() && response.body() != null) {
                    Log.e("UploadError", "Error body: " + response.body().string());
                }
            } catch (IOException e) {
                Log.e("UploadError", "Upload failed: " + e.getMessage());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(okhttp3.Response response) {
            super.onPostExecute(response);
            
            Log.e("UploadPostExecute", "onPostExecute called for asset #" + sum);
            
            // ✅ Check if fragment is still attached
            if (getActivity() == null || !isAdded()) {
                Log.e("UploadPostExecute", "Fragment not attached, skipping toast");
                return;
            }
            
            if (response == null) {
                Log.e("UploadPostExecute", "Response is NULL - no internet or request failed");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "❌ Asset " + sum + " - Upload Failed (No Response)", Toast.LENGTH_LONG).show();
                    }
                });
                sum++;
                return;
            }
            
            try {
                final int code = response.code();
                final boolean isSuccess = response.isSuccessful();
                Log.e("UploadPostExecute", "Response code: " + code + ", isSuccessful: " + isSuccess);
                
                // ✅ Show success dialog on UI thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess) {
                            Log.e("ToastShowing", "About to show success dialog for asset " + sum);
                            
                            // Show AlertDialog instead of Toast for better visibility
                            new android.app.AlertDialog.Builder(getActivity())
                                .setTitle("✅ Upload Successful")
                                .setMessage("Asset " + sum + " has been uploaded successfully!")
                                .setPositiveButton("OK", null)
                                .show();
                            
                            Log.e("UploadSuccess", "Asset " + sum + " uploaded successfully");
                        } else {
                            String errorMsg = "❌ Asset " + sum + " Failed (Code: " + code + ")";
                            
                            new android.app.AlertDialog.Builder(getActivity())
                                .setTitle("❌ Upload Failed")
                                .setMessage(errorMsg)
                                .setPositiveButton("OK", null)
                                .show();
                            
                            Log.e("UploadFailed", errorMsg);
                        }
                    }
                });
                sum++;
            } catch (Exception e) {
                final String errorMsg = "❌ Asset " + sum + " Error: " + e.getMessage();
                Log.e("UploadException", errorMsg, e);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                sum++;
            }
        }

        @Override
        protected void onCancelled(okhttp3.Response response) {
            super.onCancelled(response);
        }
    }

    //  ======================  BATCH UPLOAD - ONE SESSION FOR ALL ASSETS ====================
    
    public class UploadSummaryBatch extends AsyncTask<Tiasset, String, Integer> {
        private int totalAssets = 0;
        private int successCount = 0;
        private int failCount = 0;
        
        @Override
        protected Integer doInBackground(Tiasset... tiassets) {
            totalAssets = tiassets.length;
            
            // ✅ Check internet connection
            int connectionStatus = ((WelcomeActivity) getActivity()).Checkconnection();
            if (connectionStatus == 2) {
                Log.e("BatchUpload", "No internet connection");
                return -1; // No internet
            }
            
            // ✅ Create ONE OkHttpClient for all uploads
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build();
            
            String urlsummary = AppConfig.getSummaryUrl(server);
            String credential = Credentials.basic(user, pass);
            
            Log.e("BatchUpload", "Starting batch upload of " + totalAssets + " assets");
            Log.e("BatchUpload", "URL: " + urlsummary);
            
            // ✅ Step 1: Get CSRF token ONCE for entire session
            Request requestget = new Request.Builder()
                    .url(urlsummary)
                    .get()
                    .header("Authorization", credential)
                    .header("content-type", "application/json")
                    .header("X-CSRF-Token", "fetch")
                    .build();
            
            okhttp3.Response responseget = null;
            String csrfToken = null;
            String cookie = null;
            
            try {
                responseget = client.newCall(requestget).execute();
                Log.e("BatchUpload", "CSRF GET Response code: " + responseget.code());
                
                if (responseget.isSuccessful()) {
                    csrfToken = responseget.headers().get("x-csrf-token");
                    cookie = responseget.headers().get("Set-Cookie");
                    Log.e("BatchUpload", "CSRF Token obtained: " + (csrfToken != null ? "YES" : "NO"));
                } else {
                    Log.e("BatchUpload", "Failed to get CSRF token");
                    return -2; // CSRF failed
                }
            } catch (IOException e) {
                Log.e("BatchUpload", "CSRF request exception: " + e.getMessage());
                return -2;
            } finally {
                if (responseget != null) {
                    responseget.close();
                }
            }
            
            if (csrfToken == null || csrfToken.isEmpty()) {
                Log.e("BatchUpload", "No CSRF token received");
                return -2;
            }
            
            // ✅ Step 2: Upload each asset using the SAME session (same CSRF token & cookie)
            MediaType mediaType = MediaType.parse("application/json");
            
            for (int i = 0; i < tiassets.length; i++) {
                Tiasset asset = tiassets[i];
                int assetNumber = i + 1;
                
                try {
                    Log.e("BatchUpload", "Uploading asset " + assetNumber + "/" + totalAssets + ": " + asset.getANLN1());
                    
                    // Build JSON payload
                    JSONObject jsonToPost = new JSONObject();
                    String costupdate = asset.KOSTL;
                    if (asset.KOSTL != null) {
                        if (asset.KOSTL.length() == 3) {
                            costupdate = "0000000" + asset.KOSTL;
                        } else if (asset.KOSTL.length() == 4) {
                            costupdate = "000000" + asset.KOSTL;
                        }
                    }
                    
                    jsonToPost.put("EMP_ID", asset.PERNR);
                    jsonToPost.put("ASSET_NO", asset.ANLN1);
                    jsonToPost.put("VALIDDATE", asset.ADATU);
                    jsonToPost.put("BRAND", asset.ORD41);
                    jsonToPost.put("PLANT", asset.NAME1);
                    jsonToPost.put("COSTCENTER", asset.KTEXT);
                    jsonToPost.put("MODEL", asset.ORD42);
                    jsonToPost.put("SERIAL_NO", asset.ANLHTXT);
                    jsonToPost.put("KOSTL", costupdate);
                    jsonToPost.put("WERKS", asset.WERKS);
                    jsonToPost.put("EMP_NAME", asset.ENAME);
                    jsonToPost.put("DEP", asset.DEPARTMENT);
                    jsonToPost.put("ETITLE", asset.TITLE);
                    jsonToPost.put("ASSET_STATUS", asset.STATUS);
                    jsonToPost.put("ROOMNO", asset.ROOMNO);
                    
                    RequestBody body = RequestBody.create(mediaType, jsonToPost.toString());
                    
                    // ✅ Reuse same CSRF token and cookie for all uploads!
                    Headers headers = new Headers.Builder()
                            .add("content-type", "application/json")
                            .add("X-CSRF-Token", csrfToken)
                            .add("Cookie", cookie != null ? cookie : "")
                            .add("Authorization", credential)
                            .build();
                    
                    Request request = new Request.Builder()
                            .url(urlsummary)
                            .post(body)
                            .headers(headers)
                            .build();
                    
                    okhttp3.Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        
                        if (response.isSuccessful()) {
                            successCount++;
                            Log.e("BatchUpload", "✅ Asset " + assetNumber + " uploaded - Code: " + response.code());
                            
                            // Publish progress to show toast on UI thread
                            publishProgress("SUCCESS", String.valueOf(assetNumber), asset.ANLN1);
                        } else {
                            failCount++;
                            Log.e("BatchUpload", "❌ Asset " + assetNumber + " failed - Code: " + response.code());
                            publishProgress("FAILED", String.valueOf(assetNumber), String.valueOf(response.code()));
                        }
                    } catch (IOException e) {
                        failCount++;
                        Log.e("BatchUpload", "❌ Asset " + assetNumber + " exception: " + e.getMessage());
                        publishProgress("ERROR", String.valueOf(assetNumber), e.getMessage());
                    } finally {
                        if (response != null) {
                            response.close();
                        }
                    }
                    
                } catch (JSONException e) {
                    failCount++;
                    Log.e("BatchUpload", "JSON error for asset " + assetNumber + ": " + e.getMessage());
                    publishProgress("ERROR", String.valueOf(assetNumber), "JSON Error");
                }
                
                // Small delay between uploads to avoid overwhelming the server
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            Log.e("BatchUpload", "Batch complete - Success: " + successCount + ", Failed: " + failCount);
            return successCount; // Return number of successful uploads
        }
        
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            
            // Check if fragment is still attached
            if (getActivity() == null || !isAdded()) {
                return;
            }
            
            String status = values[0];
            String assetNum = values[1];
            String detail = values[2];
            
            if (status.equals("SUCCESS")) {
                Toast toast = Toast.makeText(getActivity(), 
                    "✅ Asset " + assetNum + " Uploaded Successfully!", 
                    Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0, 0);
                toast.show();
            } else if (status.equals("FAILED")) {
                Toast toast = Toast.makeText(getActivity(), 
                    "❌ Asset " + assetNum + " Failed (Code: " + detail + ")", 
                    Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0, 0);
                toast.show();
            } else if (status.equals("ERROR")) {
                Toast toast = Toast.makeText(getActivity(), 
                    "❌ Asset " + assetNum + " Error: " + detail, 
                    Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            
            // Check if fragment is still attached
            if (getActivity() == null || !isAdded()) {
                return;
            }
            
            String message;
            if (result == -1) {
                message = "❌ No Internet Connection";
            } else if (result == -2) {
                message = "❌ Failed to get CSRF token";
            } else {
                message = "✅ Upload Complete!\n" + 
                         "Success: " + successCount + "/" + totalAssets + "\n" +
                         "Failed: " + failCount;
            }
            
            Log.e("BatchUpload", "Final result: " + message);
            
            // Show final summary dialog
            new android.app.AlertDialog.Builder(getActivity())
                .setTitle("Upload Summary")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((WelcomeActivity) getActivity())
                .setActionBarTitle("Asset Tracking System", " ");
    }
}
