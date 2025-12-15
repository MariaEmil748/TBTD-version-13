package com.testing.inventorytracking;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

import com.testing.inventorytracking.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomAssetMapping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomAssetMapping extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String server, user, pass, url, address;
    private TextView roomNumber, actualAsset, differenceCount;
    private RadioButton serial_Number, asset_Number, room_Scan;
    private EditText scanningET;
    private RecyclerView recyclerView;
    private HandheldAdapter adapter;
    private TextView totalCount, totalTV;
    private Button getAssets;
    private ArrayList<String> Multiarraylis = new ArrayList<>();
    private final ArrayList<Tiasset> finalList = new ArrayList<>();
    private final List<Tiasset> cachedlist = new ArrayList<>();
    private String room_Number;
    private int diff;
    private int actual;
    private int c = 0;
    private int count = 0;
    private int sum = 1;
    private Calendar calendar = Calendar.getInstance();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RoomAssetMapping() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomMapping.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomAssetMapping newInstance(String param1, String param2) {
        RoomAssetMapping fragment = new RoomAssetMapping();
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

        menuItem.setVisible(true);
        menuItem3.setVisible(false);
        menuItem4.setVisible(false);
        menuItem2.setVisible(false);
        menuItem5.setVisible(false);
        menuItem6.setVisible(true);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (finalList.isEmpty()) {
            return false;
        }
        List<Tiasset> newList = new ArrayList<>();
        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).ANLN1.contains(newText) || finalList.get(i).ANLHTXT.contains(newText)) {
                newList.add(finalList.get(i));
            }
        }
        adapter.updatingList(newList);
        return true;
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
        // Inflate the layout for this fragment
        ((WelcomeActivity) getActivity()).Assetaudit.setEnabled(false);
        ((WelcomeActivity) getActivity()).RoomMapping.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetSearch.setEnabled(false);
        ((WelcomeActivity) getActivity()).NewAsset.setEnabled(false);
        ((WelcomeActivity) getActivity()).AssetTransfer.setEnabled(false);
        new RoomGetcontrollar(getContext(), finalList, Multiarraylis).execute();
        url = getArguments().getString("get");
        server = getArguments().getString("server");
        user = getArguments().getString("user");
        pass = getArguments().getString("pass");
        address = getArguments().getString("Address");
        setHasOptionsMenu(true);
        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_room_mapping, container, false);
        ((WelcomeActivity) getActivity())
                .setActionBarTitle("Room Mapping", " ");
        c = ((WelcomeActivity) getActivity()).Checkconnection();
        if (c == 2) {
            Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
        }
        roomNumber = view.findViewById(R.id.room_Number);
        actualAsset = view.findViewById(R.id.actual_Assets);
        differenceCount = view.findViewById(R.id.difference_Count);
        serial_Number = view.findViewById(R.id.serialsearchR);
        asset_Number = view.findViewById(R.id.assetSearchR);
        room_Scan = view.findViewById(R.id.room_ScanR);
        scanningET = view.findViewById(R.id.searchingR);
        getAssets = view.findViewById(R.id.getAssetsR);
        totalCount = view.findViewById(R.id.totalCountSR);
        totalTV = view.findViewById(R.id.totalTxR);
         Multiarraylis = new ArrayList<>();
        recyclerView = view.findViewById(R.id.historyrecycleR);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new ItemTouchHelper(itemTouthHelper).attachToRecyclerView(recyclerView);
        scanningET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Runnable r = new Runnable() {
                    public void run() {
                        String  assetnumber = scanningET.getText().toString();
                        Multiarraylis.add(assetnumber);
                        scanningET.getText().clear();
                    }
                };
                Handler h = new Handler();
                h.postDelayed(r, 100);  // Reduced from 300ms to 100ms for faster scanning
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                    serialnumber.getText().clear();
            }
        });

        getAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = ((WelcomeActivity) getActivity()).Checkconnection();
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Multiarraylis.size() == 0) {
                     Toast.makeText(getContext(), "Scan Barcode ", Toast.LENGTH_SHORT).show();
                    return;
                }
                SearchR(Multiarraylis);
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AssignAssets:
                if (roomNumber.getText().equals("0")){
                    Toast.makeText(getContext(), "Scan Room First !!", Toast.LENGTH_SHORT).show();
                } else if (finalList.size() == 0 ) {
                    Toast.makeText(getContext(), "There is Nothing Scanned To Assign", Toast.LENGTH_SHORT).show();
                } else {
                    room_Number = roomNumber.getText().toString();
                    for (int i = 0; i < finalList.size(); i++) {
                        finalList.get(i).setADATU(
                                (calendar.get(Calendar.DAY_OF_MONTH)) + "."
                                        + (calendar.get(Calendar.MONTH) + 1) + "."
                                        + calendar.get(Calendar.YEAR));
                        finalList.get(i).setROOMNO(room_Number);
                        String cost =finalList.get(i).KOSTL ;
                        if (finalList.get(i).KOSTL.length() == 3) {
                            finalList.get(i).setKOSTL( "0000000" + cost);
                        } else {
                            finalList.get(i).setKOSTL( "000000" + cost);
                        }
                        try {
                            new Assign_Assets().execute(adapter.list.get(i));
                        } catch (NullPointerException e) {
                            Toast.makeText(getContext(), "Not Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                    sum = 1;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SearchR(final ArrayList<String> Multiarraylis) {
        final ArrayList<Tiasset> searchList = new ArrayList<>();
        searchList.clear();
        room_Number = roomNumber.getText().toString();
        if (server.equals("teie")) {
//            if (c == 0) {
//                url = "http://" + address + ":8030/assets/";
//            } else if (c == 1) {
//                url = "http://nteie-app.tbtd-egypt.com:8030/assets/";
//            } else if (c == 2) {
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//            }
            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
            } else {
                url = AppConfig.getAssetsUrl(server);
            }

            RetrofitClient retrofitClient = new RetrofitClient();
            Retrofit retrofit = retrofitClient.connect(url, user, pass);
            SapClient sapClient = retrofit.create(SapClient.class);
            if (asset_Number.isChecked() == true) {
                for (int i = 0; i < Multiarraylis.size(); i++) {
                    final int position = i;
                    String anln = "00000" + Multiarraylis.get(position);
                    Call<List<Tiasset>> call = sapClient.getassetteie(anln);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().size() == 0)
                                return;
                            Tiasset assetTEie = response.body().get(0);
                            Log.e("size", "onResponse: " + response.body().size());
                            if (cachedlist.size() == 0) {
                                finalList.add(assetTEie);
                                cachedlist.add(assetTEie);
                                if (position == Multiarraylis.size() - 1 || position == 0) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    Multiarraylis.clear();
                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            } else {
                                boolean found = false;
                                for (int k = 0; k < cachedlist.size(); k++) {
                                    if (cachedlist.get(k).getANLN1().equals(assetTEie.getANLN1())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found)
                                    finalList.add(assetTEie);
                                cachedlist.clear();
                                cachedlist.addAll(finalList);
                                if (position == Multiarraylis.size() - 1) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    Multiarraylis.clear();
                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            }
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), t + "Network Error", Toast.LENGTH_LONG).show();
                        }

                    });
                }
            } else if (serial_Number.isChecked() == true) {
                return;
            } else {
//                if (c == 0) {
//                    url = "http://" + address + ":8030/rooms/";
//                } else if (c == 1) {
//                    url = "http://nteie-app.tbtd-egypt.com:8030/rooms/";
//                }
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
                    url = AppConfig.getRoomsUrl(server);
                }

                Retrofit retrofitt = retrofitClient.connect(url, user, pass);
                SapClient sapClientt = retrofitt.create(SapClient.class);
                Call<List<Tiasset>> call = sapClientt.getRooms(Multiarraylis.get(0));
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "No Date Retrieved", Toast.LENGTH_SHORT).show();
                        } else {
                            roomNumber.setText(response.body().get(0).getROOMNO());
                            int size = response.body().size();
                            actualAsset.setText(String.valueOf(size));
                            new ItemTouchHelper(itemTouthHelper).attachToRecyclerView(recyclerView);
                            Multiarraylis.clear();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Network Error Room :" + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
                return;
            }
        }
        //TBTD Coddddddddddddddddddddddddding
        else {
//            if (c == 0) {
//                url = "http://" + address + ":8030/assets/";
//            } else if (c == 1) {
//                url = "http://tbtd-app.tbtd-egypt.com:8040/assets/";
//            } else if (c == 2) {
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//            }
            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
            } else {
                url = AppConfig.getAssetsUrl(server);
            }

            Set<String> set = new HashSet<>(Multiarraylis);
            Multiarraylis.clear();
            Multiarraylis.addAll(set);

            if (Multiarraylis.contains("") || Multiarraylis.contains("\n")) {
                Multiarraylis.remove("");
                Multiarraylis.remove("\n");
            }

            RetrofitClient retrofitClient = new RetrofitClient();
            Retrofit retrofit = retrofitClient.connect(url, user, pass);
            SapClient sapClient = retrofit.create(SapClient.class);
            if (asset_Number.isChecked() == true) {
                for (int i = 0; i < Multiarraylis.size(); i++) {
                    final int position = i;
                    String anln = "00000" + Multiarraylis.get(position);
                    Call<List<Tiasset>> call = sapClient.getassettbtd(anln);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().size() == 0)
                                return;
                            Tiasset assetTEie = response.body().get(0);
                            actual = Integer.parseInt(actualAsset.getText().toString());
                            if (assetTEie.getROOMNO().equals(room_Number)) {
                                count++;
                            }

                            Log.e("size", "onResponse: " + response.body().size());
                            if (cachedlist.size() == 0) {
                                finalList.add(assetTEie);
                                cachedlist.add(assetTEie);
                                if (position == Multiarraylis.size() - 1 || position == 0) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    new ItemTouchHelper(itemTouthHelper).attachToRecyclerView(recyclerView);
                                    Multiarraylis.clear();
                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            } else {
                                boolean found = false;
                                for (int k = 0; k < cachedlist.size(); k++) {
                                    if (cachedlist.get(k).getANLN1().equals(assetTEie.getANLN1())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found)
                                    finalList.add(assetTEie);
                                cachedlist.clear();
                                cachedlist.addAll(finalList);
                                if (position == Multiarraylis.size() - 1) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    Multiarraylis.clear();
                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            }
                            recyclerView.setAdapter(adapter);
                            if (count > actual) {
                                diff = count - actual;
                                differenceCount.setText("+" + diff);
                            } else if (count == actual) {
                                differenceCount.setText("0");
                            } else if (count < actual) {
                                diff = actual - count;
                                differenceCount.setText("-" + diff);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), t + "Network Error", Toast.LENGTH_LONG).show();
                        }

                    });
                }
                return;
            } else if (serial_Number.isChecked() == true) {
                for (int i = 0; i < Multiarraylis.size(); i++) {
                    final int position = i;
                    String serial = Multiarraylis.get(position);
                    Call<List<Tiasset>> call = sapClient.getassetS(serial);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().size() == 0)
                                return;
                            Tiasset assetTEie = response.body().get(0);
                            actual = Integer.parseInt(actualAsset.getText().toString());
                            Log.e("size", "onResponse: " + response.body().size());
                            if (cachedlist.size() == 0) {
                                finalList.add(assetTEie);
                                cachedlist.add(assetTEie);
                                if (position == Multiarraylis.size() - 1 || position == 0) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    Multiarraylis.clear();
                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            } else {
                                boolean found = false;
                                for (int k = 0; k < cachedlist.size(); k++) {
                                    if (cachedlist.get(k).getANLHTXT().equals(assetTEie.getANLHTXT())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found)
                                    finalList.add(assetTEie);
                                cachedlist.clear();
                                cachedlist.addAll(finalList);
                                if (position == Multiarraylis.size() - 1) {
                                    adapter = new HandheldAdapter(finalList, server);
                                    recyclerView.setAdapter(adapter);
                                    new ItemTouchHelper(itemTouthHelper).attachToRecyclerView(recyclerView);
                                    Multiarraylis.clear();

                                }
                                totalTV.setVisibility(View.VISIBLE);
                                totalCount.setVisibility(View.VISIBLE);
                                totalCount.setText(finalList.size() + " ");
                            }
                            recyclerView.setAdapter(adapter);
                            if (assetTEie.getROOMNO().equals(room_Number)) {
                                count++;
                            }
                            if (count > actual) {
                                diff = count - actual;
                                differenceCount.setText("+" + diff);
                            } else if (count == actual) {
                                differenceCount.setText("0");
                            } else if (count < actual) {
                                diff = actual - count;
                                differenceCount.setText("-" + diff);
                            }
//                            differenceCount.setText(totalCount.getText().toString());
                        }

                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network Error serial :" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });
                }

                return;
            } else if (room_Scan.isChecked() == true) {
//                if (c == 0) {
//                    url = "http://" + address + ":8030/rooms/";
//                } else if (c == 1) {
//                    url = "http://10.60.120.6:8030/rooms/";
//                }
//
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
                    url = AppConfig.getRoomsUrl(server);
                }

                Retrofit retrofitt = retrofitClient.connect(url, user, pass);
                SapClient sapClientt = retrofitt.create(SapClient.class);
                Call<List<Tiasset>> call = sapClientt.getRooms(Multiarraylis.get(0));
                call.enqueue(new Callback<List<Tiasset>>() {
                    @Override
                    public void onResponse(Call<List<Tiasset>> call, Response<List<Tiasset>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().size() == 0) {
                            Toast.makeText(getContext(), "No Date Retrieved", Toast.LENGTH_SHORT).show();
                        } else {
                            roomNumber.setText(response.body().get(0).getROOMNO());
                            actualAsset.setText(response.body().get(0).getCASSETS());
                            new ItemTouchHelper(itemTouthHelper).attachToRecyclerView(recyclerView);
                            Multiarraylis.clear();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                        Toast.makeText(getContext(), "Network Error Room :" + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
                return;
            }
        }
        return;
    }

    ItemTouchHelper.SimpleCallback itemTouthHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.list.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            totalCount.setText(adapter.list.size() + "");
            int newSize = adapter.list.size();
            actual = Integer.parseInt(actualAsset.getText().toString());
            if (newSize > actual) {
                diff = newSize - actual;
                differenceCount.setText("+" + diff + "");
            } else if (newSize < actual) {
                diff = actual - newSize;
                differenceCount.setText("-" + diff + "");
            } else {
                diff = 0;
                differenceCount.setText(diff + "");
            }

        }
    };


    public class Assign_Assets extends AsyncTask<Tiasset, String, okhttp3.Response> {
        @Override
        protected okhttp3.Response doInBackground(Tiasset... tiassets) {
            Request requestget;
            okhttp3.Response responseget = null;
            okhttp3.Response response = null;
            Request request = null;
            OkHttpClient clientget = new OkHttpClient().newBuilder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();
            String urlAssign = "";
//            if (server.equals("teie")) {
//                if (c == 0) {
//                    urlAssign = "http://" + address + ":8030/assets/assets?sap-client=500";
//                } else if (c == 1) {
//                    urlAssign = "http://nteie-app.tbtd-egypt.com:8030/assets/assets?sap-client=500";
//
//                } else if (c == 2) {
//                    return null;
//                }
//            } else {
//                if (c == 2) {
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                } else {
//                    url = AppConfig.ASSETS_URL;
//                }
//            }
            if (server.equals("teie")) {
                if (c == 2) {
                    return null;
                } else {
                    urlAssign = AppConfig.getAssetsUrl(server);
                }
            } else {
                if (c == 2) {
                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                    return null;
                } else {
                    urlAssign = AppConfig.getAssetsUrl(server);
                }
            }

            String credential = Credentials.basic(user, pass);
            try {
                requestget = new Request.Builder()
                        .url(urlAssign)
                        .get()
                        .header("Authorization", credential)
                        .header("content-type", "application/json")
                        .header("X-CSRF-Token", "fetch")
                        .header("ROOMNO", "ISG01")
                        .build();
                responseget = clientget.newCall(requestget).execute();
            } catch (NullPointerException | IOException e) {
                Log.e("Errrrrror", e.getMessage());
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
                jsonToPost.put("KOSTL", costupdate);
                jsonToPost.put("WERKS", tiassets[0].WERKS);
                jsonToPost.put("EMP_NAME", tiassets[0].ENAME);
                jsonToPost.put("DEP", tiassets[0].DEPARTMENT);
                jsonToPost.put("ETITLE", tiassets[0].TITLE);
                jsonToPost.put("ASSET_STATUS", tiassets[0].STATUS);
                jsonToPost.put("ROOMNO", tiassets[0].ROOMNO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                RequestBody body = RequestBody.create(mediaType, jsonToPost.toString());
                Headers headers = new Headers.Builder()
                        .add("content-type", "application/json")
                        .add("X-CSRF-Token", responseget.headers().get("x-csrf-token"))
                        .add("Cookie", responseget.headers().get("Set-Cookie"))
                        .add("Authorization", credential)
                        .build();
                request = new Request.Builder()
                        .url(urlAssign)
                        .post(body)
                        .headers(headers)
                        .build();
            } catch (NullPointerException e) {
                Log.e("Errrrrror", e.getMessage());
            }
            try {
                response = clientget.newCall(request).execute();
            } catch (IllegalArgumentException | NullPointerException | IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(okhttp3.Response response) {
            super.onPostExecute(response);
            try {
                if (response.isSuccessful() == true) {
                    Toast.makeText(getContext(), "Asset number " + sum + " Assigned", Toast.LENGTH_SHORT).show();
                    sum++;
                }
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), "Asset number " + sum + " Not Assigned", Toast.LENGTH_SHORT).show();
                sum++;
            }
        }

        @Override
        protected void onCancelled(okhttp3.Response response) {
            super.onCancelled(response);
        }
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
                adapter = new HandheldAdapter(finalList, server);
                recyclerView.setAdapter(adapter);
            }
            cachedlist.addAll(offlineData);
        }
    }
}
