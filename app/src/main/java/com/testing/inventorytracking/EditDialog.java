package com.testing.inventorytracking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Observable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import static android.content.ContentValues.TAG;

import com.testing.inventorytracking.R;

public class EditDialog extends AppCompatDialogFragment implements sendImageName {
    private EditText serialnumber, employeename, mainassetnumber, brand, model, employeehrcode, department, validfrom, empPosition;
    private Spinner statusSpinner, plant, costcenter,Room;
    private Button btn;
    private ArrayAdapter<String> spinner_adapter, spinner_adapter2, spinner_adapter3,spinner_adapter4;
    private TextView signature, plantdes, costcenterdes,roomTV;
    private Tiasset tiasset;
    private String server, Transferasset, user, pass, url, address;
    private String imagename;
    private int c = 0 ;
    private RetrofitClient retrofitClient = new RetrofitClient();
    int spinnerPosition , spinnerPosition2 , spinnerPosition3 , spinnerPosition4 ;
    final ArrayList<String> room_list = new ArrayList<>();
    final ArrayList<String> costcenter_list = new ArrayList<>();
    final ArrayList<String> plant_list = new ArrayList<>();
    final ArrayList<String> status_list = new ArrayList<>();


    @Override
    public void imageName(String imageName) {
        this.imagename = imageName;
        // Show update button only after signature is saved
        if (imageName != null && !imageName.isEmpty()) {
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ipAddress(String ip, boolean value) {

    }


    public static EditDialog newInstanse(Tiasset tiasset, String server, String transfer, String user, String pass, String url,String addres) {
        EditDialog editDialog = new EditDialog();
        editDialog.tiasset = tiasset;
        editDialog.Transferasset = transfer;
        editDialog.server = server;
        editDialog.user = user;
        editDialog.pass = pass;
        editDialog.url = url;
        editDialog.address=addres;
        return editDialog;
    }

    String fulldate;


    @SuppressLint("WrongConstant")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dailog, null);
        Calendar calendar = Calendar.getInstance();
        builder.setView(view)
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        c=((WelcomeActivity)getActivity()).Checkconnection();
        signature = view.findViewById(R.id.signature);
        btn = view.findViewById(R.id.Edit);
        serialnumber = view.findViewById(R.id.serialnumberD);
        mainassetnumber = view.findViewById(R.id.mainassetsnumberD);
        employeename = view.findViewById(R.id.empNameD);
        brand = view.findViewById(R.id.branETD);
        model = view.findViewById(R.id.modelD);
        employeehrcode = view.findViewById(R.id.hrCodeD);
        department = view.findViewById(R.id.dePtD);
        statusSpinner = view.findViewById(R.id.assetstatusD);
        Room = view.findViewById(R.id.RoomNumberD);
        roomTV=  view.findViewById(R.id.RoomTVD);
        validfrom = view.findViewById(R.id.dateED);
        plant = view.findViewById(R.id.plantspin);
        plantdes = view.findViewById(R.id.plantdes);
        costcenter = view.findViewById(R.id.costspinn);
        costcenterdes = view.findViewById(R.id.costcenterdes);
        empPosition = view.findViewById(R.id.empPosition);
        final sendImageName ref = this;
//        if (server.equals("teie")) {
//            if(c == 0 ){
//                url = "http://"+address+":8030/assets/";
//            }else if ( c == 1){
//                url= "http://nteie-app.tbtd-egypt.com:8030/assets/";
//            }else if (c == 2){
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                return  null ;
//            }
        if (server.equals("teie")) {
            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                url = AppConfig.getRanUrl(server);   // ✅ TEIE uses RAN endpoint from AppConfig
            }
            spinner_adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status));
            spinner_adapter2 = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.plantteie));
            spinner_adapter3 = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.costcenterteie));
            if(server.equals("teie")){
                spinner_adapter4 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.TEIERoom));
            }else{
            spinner_adapter4 = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, room_list);
            }
        } else {
//            if(c == 0 ){
//                url = "http://"+address+":8030/assets/";
//            }else if ( c == 1){
//                url= "http://tbtd-app.tbtd-egypt.com:8040/assets/";
//            }else if (c == 2){
//                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                return  null;
//            }
            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                return null;
            } else {
//                url = AppConfig.ASSETS_URL;
               url = AppConfig.getAssetsUrl(server);
            }
            spinner_adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status));

            spinner_adapter2 = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Plant));
            spinner_adapter3 = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.CostCenter));

            if(server.equals("teie")){
//                Retrofit retrofit = retrofitClient.connect("http://nteie-app.tbtd-egypt.com:8030/", user, pass);
                Retrofit retrofit = retrofitClient.connect(AppConfig.getRanUrl(server).replace("ran/ran?sap-client=500", ""), user, pass);



                
                SapClient sapClient = retrofit.create(SapClient.class);
                try {
                    Call<List<RoomParamter>> call = sapClient.getRoomList();
                    call.enqueue(new Callback<List<RoomParamter>>() {
                        @Override
                        public void onResponse(Call<List<RoomParamter>> call, retrofit2.Response<List<RoomParamter>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                for (int i = 0 ; i<response.body().size();i++){
                                    room_list.add(response.body().get(i).getROOMNO().toString());
                                }
                                Collections.sort(room_list);
                                Log.i("room Name: ", room_list.toString());
                                Room.setAdapter(spinner_adapter4);
                                Room.setSelection(spinner_adapter4.getPosition(tiasset.getROOMNO()),true);

                            }
                        }

                        @Override
                        public void onFailure(Call<List<RoomParamter>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }

                spinner_adapter4 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, room_list);


                try {
                    Call<List<String>> call = sapClient.getCostCenter();
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                costcenter_list.addAll(response.body());
                                Collections.sort(costcenter_list);
                                Log.i("Cost Center: ", tiasset.getKOSTL());
                                costcenter.setAdapter(spinner_adapter3);
                                costcenter.setSelection(spinner_adapter3.getPosition(tiasset.getKOSTL()),true);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }

                spinner_adapter3 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, costcenter_list);



                try {
                    Call<List<String>> call = sapClient.getplantNo();
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                plant_list.addAll(response.body());
                                Collections.sort(plant_list);
                                Log.i("Plant No. : ", tiasset.getWERKS());
                                plant.setAdapter(spinner_adapter2);
                                plant.setSelection(spinner_adapter2.getPosition(tiasset.getWERKS()),true);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }

                spinner_adapter2 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, plant_list);


                spinner_adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status));

            }else{
//                        Retrofit retrofit = retrofitClient.connect("http://tbtd-app.tbtd-egypt.com:8040/", user, pass);
                Retrofit retrofit = retrofitClient.connect(AppConfig.getAssetsUrl(server).replace("assets/", ""), user, pass);

                SapClient sapClient = retrofit.create(SapClient.class);
                        try {
                            Call<List<RoomParamter>> call = sapClient.getRoomList();
                            call.enqueue(new Callback<List<RoomParamter>>() {
                                @Override
                                public void onResponse(Call<List<RoomParamter>> call, retrofit2.Response<List<RoomParamter>> response) {
                                    if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                        return;
                                    else {
                                        for (int i = 0 ; i<response.body().size();i++){
                                            room_list.add(response.body().get(i).getROOMNO().toString());
                                        }
                                        Collections.sort(room_list);
                                        Log.i("room Name: ", room_list.toString());
                                        Room.setAdapter(spinner_adapter4);
                                        Room.setSelection(spinner_adapter4.getPosition(tiasset.getROOMNO()),true);

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<RoomParamter>> call, Throwable t) {
                                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }catch (NullPointerException e){
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }

                spinner_adapter4 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, room_list);


                try {
                    Call<List<String>> call = sapClient.getCostCenter();
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                costcenter_list.addAll(response.body());
                                Collections.sort(costcenter_list);
                                Log.i("Cost Center: ", tiasset.getKOSTL());
                                costcenter.setAdapter(spinner_adapter3);
                                costcenter.setSelection(spinner_adapter3.getPosition(tiasset.getKOSTL()),true);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }

                                spinner_adapter3 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, costcenter_list);



                try {
                    Call<List<String>> call = sapClient.getplantNo();
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                plant_list.addAll(response.body());
                                Collections.sort(plant_list);
                                Log.i("Plant No. : ", tiasset.getWERKS());
                                plant.setAdapter(spinner_adapter2);
                                plant.setSelection(spinner_adapter2.getPosition(tiasset.getWERKS()),true);
//                                plant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                        Call<List<String>> call = sapClient.getStatuses(plant_list.get(i));
//                                        call.enqueue(new Callback<List<String>>() {
//                                            @Override
//                                            public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
//
//                                                Log.i("New Data: ",plant_list.get(i));
//                                                status_list.clear();
//                                                status_list.addAll(response.body());
//                                                statusSpinner.setAdapter(spinner_adapter);
//                                                statusSpinner.setSelection(spinner_adapter.getPosition(tiasset.getSTATUS()));
//                                                spinner_adapter.notifyDataSetChanged();
//
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<List<String>> call, Throwable throwable) {
//
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }

                spinner_adapter2 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, plant_list);



//                try {
//                    Call<List<String>> call = sapClient.getStatuses(tiasset.getWERKS());
//                    call.enqueue(new Callback<List<String>>() {
//                        @Override
//                        public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {
//                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
//                                return;
//                            else {
//                                status_list.addAll(response.body());
////                                Collections.sort(status_list);
//                                Log.i("Asset Status: ", tiasset.getSTATUS());
//                                statusSpinner.setAdapter(spinner_adapter);
//                                statusSpinner.setSelection(spinner_adapter.getPosition(tiasset.getSTATUS()),true);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<String>> call, Throwable t) {
//                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }catch (NullPointerException e){
//                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
//                }

                spinner_adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status));

//
//                spinner_adapter4 = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_spinner_item, room_list);



            }
        }
        // Calculate spinner positions based on asset data
        spinnerPosition = spinner_adapter.getPosition(tiasset.getSTATUS());
        spinnerPosition2 = spinner_adapter2.getPosition(tiasset.getWERKS());
        spinnerPosition3 = spinner_adapter3.getPosition(tiasset.getKOSTL());
        spinnerPosition4 = spinner_adapter4.getPosition(tiasset.getROOMNO());
        
        Log.i("EditDialog", "Asset Plant: " + tiasset.getWERKS() + ", Position: " + spinnerPosition2);
        Log.i("EditDialog", "Asset CostCenter: " + tiasset.getKOSTL() + ", Position: " + spinnerPosition3);
        Log.i("EditDialog", "Asset Room: " + tiasset.getROOMNO() + ", Position: " + spinnerPosition4);

        statusSpinner.setAdapter(spinner_adapter);
        plant.setAdapter(spinner_adapter2);
        costcenter.setAdapter(spinner_adapter3);
        Room.setAdapter(spinner_adapter4);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment signature = Signature.newInstance(employeehrcode.getText().toString(), ref);
                signature.show(getFragmentManager(), "Signature");
            }
        });

        if (Transferasset.equals("not_transfer")) {
            Room.setEnabled(false);
            btn.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            serialnumber.setEnabled(false);
            employeehrcode.setEnabled(false);
            employeename.setEnabled(false);
            validfrom.setEnabled(false);
            statusSpinner.setEnabled(false);
            plant.setEnabled(false);
            costcenter.setEnabled(false);
            Room.setSelection(spinnerPosition4);
            validfrom.setText(tiasset.getADATU());
        } else {
            // Asset Transfer Mode - signature is REQUIRED
            RetrofitClient retrofitClient = new RetrofitClient();
            // Use Assets URL for API calls, not RAN URL
            String apiUrl = AppConfig.getAssetsUrl(server);
            Retrofit retrofit = retrofitClient.connect(apiUrl, user, pass);
            final SapClient sapClient = retrofit.create(SapClient.class);
            validfrom.setEnabled(true);
            plant.setEnabled(true);
            Room.setEnabled(true);
            costcenter.setEnabled(true);
            btn.setVisibility(View.GONE); // Hide update button until signature is provided
            signature.setVisibility(View.VISIBLE); // Show signature button
            Room.setSelection(spinnerPosition4);
            validfrom.setText(
                    (calendar.get(Calendar.DAY_OF_MONTH)) + "."
                            + (calendar.get(Calendar.MONTH) + 1) + "."
                            + calendar.get(Calendar.YEAR));

            employeehrcode.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                int id = Integer.valueOf(employeehrcode.getText().toString());
                                if(id == 0){
                                    employeename.setText("");
                                    empPosition.setText("");
                                    department.setText("");
                                    return true ;
                                }
                                Call<List<Tiasset>> call ;
                                if(server.equals("teie")){
                                    call  = sapClient.getid(id);
                                }else{

                                    call  = sapClient.getinfo(id);
                                }
                                call.enqueue(new Callback<List<Tiasset>>() {
                                    @Override
                                    public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
                                        if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()){
                                            Toast.makeText(getContext(), "Hr-code Not Found", Toast.LENGTH_SHORT).show();
                                        } else {
                                            employeename.setText(response.body().get(0).getENAME());
                                            empPosition.setText(response.body().get(0).getTITLE());
                                            department.setText(response.body().get(0).getDEPARTMENT());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });
            plant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String x = plant.getSelectedItem().toString();
                    Call<List<Tiasset>> call = sapClient.getPlant(x);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                plantdes.setText(response.body().get(0).getNAME1());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            costcenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String x = costcenter.getSelectedItem().toString();
                    if (x.length() == 3) {
                        x = "0000000" + x;
                    } else {
                        x = "000000" + x;
                    }
                    Call<List<Tiasset>> call = sapClient.getcostcenter(x);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                costcenterdes.setText(response.body().get(0).getKTEXT());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String x = Room.getSelectedItem().toString();
                    Call<List<Tiasset>> call = sapClient.getRoomAsset(x);
                    call.enqueue(new Callback<List<Tiasset>>() {
                        @Override
                        public void onResponse(Call<List<Tiasset>> call, retrofit2.Response<List<Tiasset>> response) {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                return;
                            else {
                                roomTV.setText(response.body().get(0).getROOMNAME());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Tiasset>> call, Throwable t) {
                            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        plantdes.setText(tiasset.getNAME1());
        statusSpinner.setSelection(spinnerPosition);
        plant.setSelection(spinnerPosition2);
        costcenter.setSelection(spinnerPosition3);
        serialnumber.setEnabled(false);
        serialnumber.setText(tiasset.getANLHTXT());
        employeehrcode.setText(tiasset.getPERNR());
        employeename.setText(tiasset.getENAME());
        department.setText(tiasset.getDEPARTMENT());
        roomTV.setText(tiasset.getROOMNAME());
        if(server.equals("teie")) {
            employeename.setText(tiasset.getNCHMC() + tiasset.getVNAMC());
            department.setText(tiasset.getORGTX());
        }
        department.setEnabled(false);
        mainassetnumber.setText(tiasset.getANLN1());
        empPosition.setText(tiasset.getTITLE());
        empPosition.setEnabled(false);
        mainassetnumber.setEnabled(false);
        brand.setText(tiasset.getORD41());
        brand.setEnabled(false);
        model.setText(tiasset.getORD42() + " " + tiasset.getORD43());
        model.setEnabled(false);
        validfrom.setEnabled(false);
        costcenterdes.setText(tiasset.getKTEXT());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = employeehrcode.getText().toString();
                String name = employeename.getText().toString();
                String assetnumber = mainassetnumber.getText().toString();
                String vaildfrom = validfrom.getText().toString();
                String assetstatus = statusSpinner.getSelectedItem().toString();
                String plantt = plant.getSelectedItem().toString();
                String costcenterr = costcenter.getSelectedItem().toString();
                // ✅ Send cost center as-is without adding leading zeros
                String serlnumber = serialnumber.getText().toString();
                String brandd = brand.getText().toString();
                String modell = model.getText().toString();
                String image_name = (imagename != null && !imagename.isEmpty()) ? imagename : "";
                String roomnumber = Room.getSelectedItem().toString();
                Log.e(TAG, "Asset Update - HR: " + id + ", Signature: " + image_name);
                new Connect().execute(id, name, assetnumber, vaildfrom, assetstatus, plantt, costcenterr, image_name, user, pass, brandd, serlnumber, modell,roomnumber);
                btn.setEnabled(false);
                if (c == 2){
                    btn.setEnabled(true);
                }

            }
        });
        return builder.create();
    }


    public class Connect extends AsyncTask<String, String, Response> {
        @Override
        protected Response doInBackground(String... assetvalues) {
//            String urlupdate = "" ;
//            if (server.equals("teie")) {
//                if(c == 0 ){
//                    urlupdate = "http://"+address+":8030/assets/assets?sap-client=500";
//                }else if ( c == 1){
//                    urlupdate = "http://nteie-app.tbtd-egypt.com:8030/assets/assets?sap-client=500";
//                }else if (c == 2){
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                    return null ;
//                }
//            } else {
//                if(c == 0 ){
//                    urlupdate = "http://"+address+":8030/assets/assets?sap-client=200";
//                }else if ( c == 1){
//                    urlupdate = "http://10.60.100.100:8040/assets/assets?sap-client=200";
//                }else if (c == 2){
//                    Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                    return  null ;
//                }
//            }
            String urlupdate;

            if (c == 2) {
                Toast.makeText(getContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
                return null;
            }

            // ✅ FIXED: Use correct base URL + assets endpoint for both companies
            if (server.equals("teie")) {
                urlupdate = AppConfig.getBaseUrl(server) + "assets/assets?sap-client=500";
            } else {
                urlupdate = AppConfig.getBaseUrl(server) + "assets/assets?sap-client=200";
            }


            OkHttpClient clientget = new OkHttpClient().newBuilder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

            String credential = Credentials.basic(assetvalues[8], assetvalues[9]);

            Request requestget = new Request.Builder()
                    .url(urlupdate)
                    .get()
                    .header("Authorization", credential)
                    .header("content-type", "application/json")
                    .header("X-CSRF-Token", "fetch")
                    .header("ANLN1", "000005300008")
                    .build();
            Response responseget = null;
            try {
                responseget = clientget.newCall(requestget).execute();
            } catch (IOException e) {
                Log.e("Errrrrror", e.getMessage());
            }
            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonToPost = new JSONObject();
            try {
                jsonToPost.put("EMP_ID", assetvalues[0]);
                jsonToPost.put("EMP_NAME", assetvalues[1]);
                jsonToPost.put("ASSET_NO", assetvalues[2]);
                jsonToPost.put("VALIDDATE", assetvalues[3]);
                jsonToPost.put("ASSET_STATUS", assetvalues[4]);
                jsonToPost.put("WERKS", assetvalues[5]);
                jsonToPost.put("KOSTL", assetvalues[6]);
                jsonToPost.put("SIGNATURE", assetvalues[7]);
                jsonToPost.put("SERIAL_NO", assetvalues[11]);
                jsonToPost.put("BRAND", assetvalues[10]);
                jsonToPost.put("MODEL", assetvalues[12]);
                jsonToPost.put("ROOMNO", assetvalues[13]);
                
                // ✅ Log the JSON payload being sent
                Log.e(TAG, "JSON Payload: " + jsonToPost.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "JSON Error: " + e.getMessage());
            }
            RequestBody body = RequestBody.create(mediaType, jsonToPost.toString());
            Headers headers = new Headers.Builder()
                    .add("content-type", "application/json")
                    .add("X-CSRF-Token", responseget.headers().get("x-csrf-token"))
                    .add("Cookie", responseget.headers().get("Set-Cookie"))
                    .add("Authorization", credential)
                    .build();
            Request request = new Request.Builder()
                    .url(urlupdate)
                    .post(body)
                    .headers(headers)
                    .build();
            
            Log.e(TAG, "POST URL: " + urlupdate);
            Log.e(TAG, "CSRF Token: " + responseget.headers().get("x-csrf-token"));
            
            Response response = null;
            try {
                response = clientget.newCall(request).execute();
                Log.e(TAG, "POST Response Code: " + (response != null ? response.code() : "null"));
                if (response != null && response.body() != null) {
                    String responseBody = response.body().string();
                    Log.e(TAG, "POST Response Body: " + responseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "POST Error: " + e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(okhttp3.Response response) {
            super.onPostExecute(response);
            if (response != null && response.isSuccessful()) {
                Toast.makeText(getContext(), "Asset Updated Successfully", Toast.LENGTH_LONG).show();
                dismiss(); // Close the dialog after successful update
            } else {
                String errorMsg = "Update Failed";
                if (response != null) {
                    errorMsg += " (Code: " + response.code() + ")";
                }
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Update failed - Response: " + (response != null ? response.code() : "null"));
            }
        }
    }


}

