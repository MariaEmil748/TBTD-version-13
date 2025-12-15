package com.testing.inventorytracking;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetHistory extends Fragment {
    EditText hrcode;
    Button Gethistory;
    private RadioButton Serial_number, Asset_number, HR_code, roomScaaning ;
    private String server, user, pass, url,address;
    private RecyclerView recyclerView;
    private HandheldAdapter adapter;
    TextView totalCount, totalTV;
    int c = 0 ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssetHistory() {
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

        menuItem.setVisible(false);
        menuItem3.setVisible(false);
        menuItem4.setVisible(false);
        menuItem2.setVisible(false);
        menuItem5.setVisible(false);

    }

    /**
     * Use this factory method to create a new instance of
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
    public void onDestroy() {
        super.onDestroy();
        ((WelcomeActivity) getActivity())
                .setActionBarTitle("Asset Tracking System", " ");
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(true);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(true);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(true);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(false);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(false);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(false);
        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_asset_history, container, false);
        ((WelcomeActivity) getActivity())
                .setActionBarTitle("Search", " ");
        hrcode = view.findViewById(R.id.searching);
        totalTV = view.findViewById(R.id.totalTx);
        Gethistory = view.findViewById(R.id.gethistory);
        Serial_number = view.findViewById(R.id.serialsearch);
        Asset_number = view.findViewById(R.id.assetSearch);
        HR_code = view.findViewById(R.id.hrsearch);
        roomScaaning = view.findViewById(R.id.RoomSearch);
        recyclerView = view.findViewById(R.id.historyrecycle);
        url = getArguments().getString("get");
        server = getArguments().getString("server");
        user = getArguments().getString("user");
        pass = getArguments().getString("pass");
        address = getArguments().getString("Address");
        totalCount = view.findViewById(R.id.totalCountS);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Gethistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c =((WelcomeActivity)getActivity()).Checkconnection();
                if(c==2){
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Search();
            }
        });
        return view;
    }

    private void Search() {
        final ArrayList<Tiasset> searchList = new ArrayList<>();
        searchList.clear();
//        if (server.equals("teie")) {
//            if(c == 0){
//                url= "http://"+address+":8030/assets/";
//            }else if ( c == 1){
//                url = "http://nteie-app.tbtd-egypt.com:8030/assets/";
//            }else if (c == 2){
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//            }
            if (server.equals("teie")) {
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
//                    url = AppConfig.ASSETS_URL;
                    url = AppConfig.getAssetsUrl(server);

                }

                RetrofitClient retrofitClient = new RetrofitClient();
            Retrofit retrofit = retrofitClient.connect(url, user, pass);
            SapClient sapClient = retrofit.create(SapClient.class);
            if (Asset_number.isChecked() == true) {
                String asset = "00000" + hrcode.getText().toString();
                Call<List<Tiasset>> call = sapClient.getassetteie(asset);
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        try{if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "Not Found ", Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }}catch (NumberFormatException | NullPointerException e ){
                            Toast.makeText(getContext(), "Error :  " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                hrcode.setText("");
            } else if (Serial_number.isChecked() == true) {
                hrcode.setText("");
                return;
            } else if (HR_code.isChecked() == true) {
//                if(c == 0){
//                    url = "http://"+address+":8030/staff/";
//                }else if (c == 1){
//                    url= "http://tbtd-app.tbtd-egypt.com:8040/staff/";
//                }else if (c == 2){
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                }
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
                    url = AppConfig.getStaffUrl(server);
                }

                retrofitClient = new RetrofitClient();
                retrofit = retrofitClient.connect(url, user, pass);
                sapClient = retrofit.create(SapClient.class);
                int id = Integer.valueOf(hrcode.getText().toString());
                Call<List<Tiasset>> call = sapClient.getinfoteie(id);

                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        try {
                            if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                                Toast.makeText(getContext(), "Not Found ", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                searchList.addAll(response.body());
                                adapter = new HandheldAdapter(searchList, server);
                                recyclerView.setAdapter(adapter);
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(searchList.size() + " ");
                            }
                        } catch (NumberFormatException | NullPointerException e) {
                            Toast.makeText(getContext(), "Error :  " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error :  " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                hrcode.setText("");
            }else{
                String room_number;
                try {
                    room_number = hrcode.getText().toString();
                    if (room_number.isEmpty()) {
                        Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                         return;
                    }

                }catch (NumberFormatException t) {
                    Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Retrofit retrofitt = retrofitClient.connect(url, user, pass);
                SapClient sapClientt = retrofitt.create(SapClient.class);
                Call<List<Tiasset>> call = sapClientt.getRoomAsset(room_number);
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "Wrong Number ", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                hrcode.setText("");
                return;
            }
            hrcode.setText("");

        }
        // tbtd cooooooooooooooooooooooooooooooooooooooooooooooooode  tbtdddddddddddddddddd
        else {
//            if(c == 0){
//                url= "http://"+address+":8030/assets/";
//            }else if ( c == 1){
//                url = "http://tbtd-app.tbtd-egypt.com:8040/assets/";
//            }else if (c == 2){
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//            }
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
//                    url = AppConfig.ASSETS_URL;
                    url = AppConfig.getAssetsUrl(server);

                }


                RetrofitClient retrofitClient = new RetrofitClient();
            Retrofit retrofit = retrofitClient.connect(url, user, pass);
            SapClient sapClient = retrofit.create(SapClient.class);
            if (Asset_number.isChecked() == true) {
                String asettest = hrcode.getText().toString();
                String asset = "00000" + hrcode.getText().toString();
                if(asettest.isEmpty()){
                    Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<List<Tiasset>> call = sapClient.getassettbtd(asset);
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
                        } else {
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                hrcode.setText("");
                return;
            } else if (Serial_number.isChecked() == true) {
                String asset = hrcode.getText().toString();
                if(asset.isEmpty()){
                    Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<List<Tiasset>> call = sapClient.getassetS(asset);
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
                        } else {
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                hrcode.setText("");
                return;
            } else if (HR_code.isChecked() == true) {
//                if(c == 0){
//                    url = "AppConfig.STAFF_URL";
//                }else if (c == 1){
//                    url= "AppConfig.STAFF_URL";
//                }else if (c == 2){
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                }
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
//                    url = AppConfig.STAFF_URL;
                    url = AppConfig.getStaffUrl(server);

                }


                Retrofit retrofitt = retrofitClient.connect(url, user, pass);
                SapClient sapClientt = retrofitt.create(SapClient.class);
                int id = 0;
                try {
                    id = Integer.valueOf(hrcode.getText().toString());
                } catch (NumberFormatException t) {
                    Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<List<Tiasset>> call = sapClientt.gettotalAsset(id);
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "Wrong Number", Toast.LENGTH_SHORT).show();

                        } else {
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                hrcode.setText("");
                return;
            }else if (roomScaaning.isChecked() == true){
                String room_number;
                try {
                    room_number = hrcode.getText().toString();
                    if (room_number.isEmpty()) {
                        Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }catch (NumberFormatException t) {
                    Toast.makeText(getContext(), "Write number first ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Retrofit retrofitt = retrofitClient.connect(url, user, pass);
                SapClient sapClientt = retrofitt.create(SapClient.class);
                Call<List<Tiasset>> call = sapClientt.getRoomAsset(room_number);
                try{


                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "Wrong Number ", Toast.LENGTH_SHORT).show();
                        } else {
                            searchList.addAll(response.body());
                            adapter = new HandheldAdapter(searchList, server);
                            recyclerView.setAdapter(adapter);
                            totalTV.setVisibility(View.VISIBLE);
                            totalCount.setVisibility(View.VISIBLE);
                            totalCount.setText(searchList.size() + " ");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "No Values", Toast.LENGTH_SHORT).show();
                }
                hrcode.setText("");
                return;
            }
            hrcode.setText("");
        }
    }


}
