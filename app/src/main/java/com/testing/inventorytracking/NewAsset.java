//package com.testing.inventorytracking;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.testing.inventorytracking.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Credentials;
//import okhttp3.Headers;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Retrofit;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link NewAsset#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class NewAsset extends Fragment {
//   private Button post;
//   private  String ipNetwork ,address;
//    private EditText assetnumbernew, descriptionnew, serialnumbernew;
//    String url ;
//    int c ;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    String server;
//
//
//    public NewAsset() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment NewAsset.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static NewAsset newInstance(String param1, String param2) {
//        NewAsset fragment = new NewAsset();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ((WelcomeActivity) getActivity())
//                .setActionBarTitle("Asset Tracking System", " ");
//        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(true);
//        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(true);
//        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(true);
//        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(true);
//        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(true);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(false);
//        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(false);
//        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(false);
//        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(false);
//        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(false);
//        View view = inflater.inflate(R.layout.fragment_new_asset, container, false);
//        ((WelcomeActivity) getActivity())
//                .setActionBarTitle("New Asset Register", " ");
//          c = ((WelcomeActivity) getActivity()).Checkconnection();
//        if (c == 2) {
//            Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//        }
//        Calendar calendar = Calendar.getInstance();
//        post = view.findViewById(R.id.updatenew);
//        assetnumbernew = view.findViewById(R.id.assetnumbernew);
//        descriptionnew = view.findViewById(R.id.descriptionnew);
//        serialnumbernew = view.findViewById(R.id.serialnumbernew);
//        serialnumbernew.setEnabled(false);
//        descriptionnew.setEnabled(false);
//        url = getArguments().getString("get");
//        server = getArguments().getString("server");
//        final String user = getArguments().getString("user");
//        final String pass = getArguments().getString("pass");
//        address = getArguments().getString("Address");
//
//
//        assetnumbernew.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    switch (keyCode) {
//                        case KeyEvent.KEYCODE_ENTER:
//                            String asset = assetnumbernew.getText().toString();
//                            if(asset.isEmpty()){
//                                Toast.makeText(getContext(), "Scan Number First ", Toast.LENGTH_SHORT).show();
//                                return true;
//                            }
//                            c =((WelcomeActivity)getActivity()).Checkconnection();
//                            if(server.equals("teie")){
//                                if(c == 0){
//                                    url= "http://"+address+":8030/assets/";
//                                }else if ( c == 1){
//                                    url = "http://nteie-app.tbtd-egypt.com:8030/assets/";
//                                }else if (c == 2){
//                                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                if(c == 0){
//                                    url= "http://"+address+":8030/assets/";
//                                }else if ( c == 1){
//                                    url = "http://tbtd-app.tbtd-egypt.com:8040/assets/";
//                                }else if (c == 2){
//                                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            RetrofitClient retrofitClient = new RetrofitClient();
//                            Retrofit retrofit = retrofitClient.connect(url,user,pass);
//                            final SapClient sapClient = retrofit.create(SapClient.class);
//                            Call<List<Tiasset>> call;
//                            if (server.equals("teie")) {
//                                call = sapClient.getassetteie("00000" + assetnumbernew.getText().toString());
//                            } else {
//                                call = sapClient.getassettbtd("00000" + assetnumbernew.getText().toString());
//                            }
//                            try {
//                                call.enqueue(new Callback<List<Tiasset>>() {
//                                    @Override
//                                    public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
//                                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
//                                            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            descriptionnew.setText(response.body().get(0).getTXT50());
//                                            serialnumbernew.setEnabled(true);
//                                        }
//                                    }
//                                    @Override
//                                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
//                                        Toast.makeText(getContext(),  "error" + t.getMessage() , Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            } catch (NullPointerException r) {
//                                Toast.makeText(getActivity(), "Error" + r.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                            return true;
//                        default:
//                            break;
//                    }
//                }
//                return false;
//            }
//        });
//        post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                c = ((WelcomeActivity) getActivity()).Checkconnection();
//                if (c == 2) {
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
//                String assetnumber = assetnumbernew.getText().toString();
//                String serial = serialnumbernew.getText().toString();
//                if (serial.equals("")) {
//                    Toast.makeText(getActivity(), "Scan Serialnumber first", Toast.LENGTH_SHORT).show();
//                } else {
//                    new Connect().execute(serial.toUpperCase(), assetnumber, user, pass);
//                    assetnumbernew.setText("");
//                    descriptionnew.setText("");
//                }
//
//            }
//        });
//        return view;
//    }
//
//    public class Connect extends AsyncTask<String, String, Response> {
//        @Override
//        protected okhttp3.Response doInBackground(String... assetvalues) {
//            OkHttpClient clientget = new OkHttpClient().newBuilder()
//                    .writeTimeout(1, TimeUnit.MINUTES)
//                    .build();
//            String urlnew = "";
//            if (server.equals("teie")) {
//                if(c == 0){
////                    urlnew = "http://"+address+":8030/ran/ran?sap-client=500";
//                }else if ( c == 1 ){
//                    urlnew= "http://nteie-app.tbtd-egypt.com:8030/ran/ran?sap-client=500";
//                }else if (c == 2) {
//                    return null;
//                }
//            } else {
////                if(c == 0){
////                    urlnew = "http://"+address+":8030/ran/ran?sap-client=200";
////                }else if ( c == 1 ){
////                    urlnew= "http://10.60.120.6:8030/ran/ran?sap-client=200";
////                }else if (c == 2) {
////                    return null;
////                }
//                if (c == 2) {
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                } else {
//                    url = AppConfig.RAN_URL;
//                }
//
//            }
//            String credential = Credentials.basic(assetvalues[2], assetvalues[3]);
//            Request requestget = new Request.Builder()
//                    .url(urlnew)
//                    .get()
//                    .header("Authorization", credential)
//                    .header("content-type", "application/json")
//                    .header("X-CSRF-Token", "fetch")
//                    .build();
//            okhttp3.Response responseget = null;
//            try {
//                responseget = clientget.newCall(requestget).execute();
//                String asset = responseget.body().string();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            MediaType mediaType = MediaType.parse("application/json");
//            JSONObject jsonToPost = new JSONObject();
//            try {
//                jsonToPost.put("SERIAL_NO", assetvalues[0]);
//                jsonToPost.put("ASSET_NO", "00000" + assetvalues[1]);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestBody body = RequestBody.create(mediaType, jsonToPost.toString());
//            Headers headers = new Headers.Builder()
//                    .add("content-type", "application/json")
//                    .add("X-CSRF-Token", responseget.headers().get("x-csrf-token"))
//                    .add("Cookie", responseget.headers().get("Set-Cookie"))
//                    .add("Authorization", credential)
//                    .build();
//            Request request = new Request.Builder()
//                    .url(urlnew)
//                    .post(body)
//                    .headers(headers)
//                    .build();
//            okhttp3.Response response = null;
//            try {
//                response = clientget.newCall(request).execute();
//                Log.e("response", String.valueOf(response.isSuccessful()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(okhttp3.Response response) {
//            super.onPostExecute(response);
//            if(response.code() == 200) {
//                Toast.makeText(getContext(), "Data Posted", Toast.LENGTH_LONG).show();
//                serialnumbernew.setText(" ");
//            }else{
//                Toast.makeText(getContext(), "Data Not Posted", Toast.LENGTH_LONG).show();
//
//            }
//
//        }
//    }
//
//}


package com.testing.inventorytracking;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.testing.inventorytracking.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class NewAsset extends Fragment {
    private Button post;
    private String ipNetwork, address;
    private EditText assetnumbernew, descriptionnew, serialnumbernew;
    private String url;
    private int c;
    private String server;

    public NewAsset() {}

    public static NewAsset newInstance(String param1, String param2) {
        NewAsset fragment = new NewAsset();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((WelcomeActivity) getActivity()).setActionBarTitle("Asset Tracking System", " ");
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(true);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(true);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(false);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(false);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(false);

        View view = inflater.inflate(R.layout.fragment_new_asset, container, false);
        ((WelcomeActivity) getActivity()).setActionBarTitle("New Asset Register", " ");

        c = ((WelcomeActivity) getActivity()).Checkconnection();
        if (c == 2) {
            Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
        }

        post = view.findViewById(R.id.updatenew);
        assetnumbernew = view.findViewById(R.id.assetnumbernew);
        descriptionnew = view.findViewById(R.id.descriptionnew);
        serialnumbernew = view.findViewById(R.id.serialnumbernew);

        serialnumbernew.setEnabled(false);
        descriptionnew.setEnabled(false);

        url = getArguments().getString("get");
        server = getArguments().getString("server");
        final String user = getArguments().getString("user");
        final String pass = getArguments().getString("pass");
        address = getArguments().getString("Address");

        assetnumbernew.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String asset = assetnumbernew.getText().toString();
                if (asset.isEmpty()) {
                    Toast.makeText(getContext(), "Scan Number First ", Toast.LENGTH_SHORT).show();
                    return true;
                }

                c = ((WelcomeActivity) getActivity()).Checkconnection();
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                    return true;
                }


//                url = AppConfig.ASSETS_URL;
//                if (server.equalsIgnoreCase("teie")) {
//                    url = AppConfig.getAssetsUrl("teie");
//                } else {
//                    url = AppConfig.getAssetsUrl("tbtd");
//                }
//                android.util.Log.d("DEBUG_URLS", "Company: " + server + ", Assets URL: " + url);
                url = AppConfig.getAssetsUrl(server);
                android.util.Log.d("DEBUG_URLS", "Company: " + server + ", Assets URL: " + url);


                RetrofitClient retrofitClient = new RetrofitClient();
                Retrofit retrofit = retrofitClient.connect(url, user, pass);
                final SapClient sapClient = retrofit.create(SapClient.class);

                Call<List<Tiasset>> call = server.equals("teie")
                        ? sapClient.getassetteie("00000" + asset)
                        : sapClient.getassettbtd("00000" + asset);

                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
                        } else {
                            descriptionnew.setText(response.body().get(0).getTXT50());
                            serialnumbernew.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
            return false;
        });

        post.setOnClickListener(v -> {
            c = ((WelcomeActivity) getActivity()).Checkconnection();
            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                return;
            }
            String assetnumber = assetnumbernew.getText().toString();
            String serial = serialnumbernew.getText().toString();
            if (serial.isEmpty()) {
                Toast.makeText(getActivity(), "Scan Serialnumber first", Toast.LENGTH_SHORT).show();
            } else {
                new Connect().execute(serial.toUpperCase(), assetnumber, user, pass);
                assetnumbernew.setText("");
                descriptionnew.setText("");
            }
        });
        return view;
    }

    public class Connect extends AsyncTask<String, String, Response> {
        @Override
        protected Response doInBackground(String... assetvalues) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

//            String urlnew = AppConfig.RAN_URL;
            String urlnew;
            if (server.equalsIgnoreCase("teie")) {
                urlnew = AppConfig.getRanUrl("teie");
            } else {
                urlnew = AppConfig.getRanUrl("tbtd");
            }
            android.util.Log.d("DEBUG_URLS", "Company: " + server + ", RAN URL: " + urlnew);


            String credential = Credentials.basic(assetvalues[2], assetvalues[3]);

            // GET request for CSRF token
            Request requestGet = new Request.Builder()
                    .url(urlnew)
                    .get()
                    .header("Authorization", credential)
                    .header("content-type", "application/json")
                    .header("X-CSRF-Token", "fetch")
                    .build();

            Response responseGet;
            try {
                responseGet = client.newCall(requestGet).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            String csrfToken = responseGet.header("x-csrf-token");
            String cookie = responseGet.header("Set-Cookie");

            // POST body
            JSONObject jsonToPost = new JSONObject();
            try {
                jsonToPost.put("SERIAL_NO", assetvalues[0]);
                jsonToPost.put("ASSET_NO", "00000" + assetvalues[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonToPost.toString());
            Headers headers = new Headers.Builder()
                    .add("content-type", "application/json")
                    .add("X-CSRF-Token", csrfToken != null ? csrfToken : "")
                    .add("Cookie", cookie != null ? cookie : "")
                    .add("Authorization", credential)
                    .build();

            Request requestPost = new Request.Builder()
                    .url(urlnew)
                    .post(body)
                    .headers(headers)
                    .build();

            try {
                return client.newCall(requestPost).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response != null && response.isSuccessful()) {
                Toast.makeText(getContext(), "Data Posted", Toast.LENGTH_LONG).show();
                serialnumbernew.setText("");
            } else {
                Toast.makeText(getContext(), "Data Not Posted", Toast.LENGTH_LONG).show();
            }
        }
    }
}
