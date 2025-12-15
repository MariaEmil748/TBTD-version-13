package com.testing.inventorytracking;

import android.database.Observable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SapClient {

    // login teie
    @GET("login")
    public Call<List<Users>> login(@Query("UNAME") String UNAME, @Query(value = "PASSWORD", encoded = true) String Password);

    //login tbtd
    @GET("login")
    public Call<List<Users>> logintbtd(@Query("UNAME") String UNAME, @Query(value = "PASSWORD", encoded = true) String PASSWORD);

    //Get from tbtd
    @GET("assets")
    public Call<List<Tiasset>> getassettbtd(@Header("ANLN1") String ANLN1);

    // get from tbtb using serial\
    @GET("assets")
    public Call<List<Tiasset>> getassetS(@Header("ANLHTXT") String ANLHTXT);


    // get from TEIE using serial\
    @GET("assets")
    public Call<List<Tiasset>> getassetteieS(@Header("ANLHTXT") String ANLHTXT);

    //get info emp tbtd
    @GET("staff")
    public Call<List<Tiasset>> gettotalAsset(@Header("PERNR") int PERNR);

    @GET("assets")
    public Call<List<Tiasset>> getinfo(@Header("PERNR") int PERNR);

    // get asign  asset  from teie
    @GET("staff")
    public Call<List<Tiasset>> getinfoteie(@Header("PERNR") int PERNR);

    //getplant tbtd
    @GET("assets")
    public Call<List<Tiasset>> getPlant(@Header("WERKS") String WERKS);


    //getplant teie
    @GET("assets")
    public Call<List<Tiasset>> getPlantT(@Header("WERKS") String WERKS);

    //get costcenter tbtd
    @GET("assets")
    public Call<List<Tiasset>> getcostcenter(@Header("KOSTL") String KOSTL);


    //get costcenter teie
    @GET("assets")
    public Call<List<Tiasset>> getcostcenterT(@Header("KOSTL") String KOSTL);

    // get from teie
    @GET("assets")
    public Call<List<Tiasset>> getassetteie(@Header("ANLN1") String ANLN1);

    // get emp info from teie
    @GET("assets")
    public Call<List<Tiasset>> getid(@Header("PERNR") int PERNR);


    //get room asset
    @GET("assets")
    public Call<List<Tiasset>> getRoomAsset(@Header("ROOMNO") String ROOMNO);


    //get all  room or search
    @GET("rooms")
    public Call<List<Tiasset>> getRooms(@Header("ROOMNO") String ROOMNO);

    //get all  room or search
    @GET("rooms/rooms")
    public Call<List<RoomParamter>> getRoomList();

    @GET("ats/status")
    public Call<List<String>> getStatuses(@Query("plantNo") String plantNo);

    @GET("ats/costcenter")
    public Call<List<String>> getCostCenter();

    @GET("ats/plantno")
    public Call<List<String>> getplantNo();

    // ✅ NEW: Batch endpoints for multiple assets at once (much faster!)
    // Get multiple assets by asset numbers (TBTD)
    @GET("assets/batch")
    public Call<List<Tiasset>> getAssetsBatchTBTD(@Query("assetNumbers") String assetNumbers);

    // Get multiple assets by asset numbers (TEIE)
    @GET("assets/batch")
    public Call<List<Tiasset>> getAssetsBatchTEIE(@Query("assetNumbers") String assetNumbers);

    // Get multiple assets by serial numbers (TBTD)
    @GET("assets/batch-serial")
    public Call<List<Tiasset>> getAssetsBatchSerialTBTD(@Query("serialNumbers") String serialNumbers);

    // Get multiple assets by serial numbers (TEIE)
    @GET("assets/batch-serial")
    public Call<List<Tiasset>> getAssetsBatchSerialTEIE(@Query("serialNumbers") String serialNumbers);

    // 🌟 BEST: Universal batch - searches BOTH asset numbers AND serial numbers automatically!
    // No more white screen! No more choosing wrong type!
    @GET("assets/batch-universal")
    public Call<List<Tiasset>> getAssetsBatchUniversal(@Query("searchValues") String searchValues);


//    @GET("rooms")
//    static Observable<List<RoomParamter>> listRooms(@Path("rooms") String rooms) {
//        return null;
//    }
//
//    @GET("status")
//    static Observable<List<String>> listStatuses(@Path("status") String status) {
//        return null;
//    }
//
//
//    @GET("costCenter")
//    static Observable<List<String>> listCostCenters(@Path("costCenter") String constCenter) {
//        return null;
//    }
}
