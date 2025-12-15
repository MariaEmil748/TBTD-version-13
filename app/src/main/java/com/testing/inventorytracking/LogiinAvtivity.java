package com.testing.inventorytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.testing.inventorytracking.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogiinAvtivity extends AppCompatActivity {
    private Button login;
    private RadioButton teie, tbtd;
    private String urlget, ipNetwork, server, loginurl, address;
    private CheckBox showPass;
    private Intent intent = new Intent();
    private EditText username, password;
    private TextView ip_Address2;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private String user ="";
    private String pass ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logiin_avtivity);
//        ip_Address2 = findViewById(R.id.tesssssssssssssssst);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        showPass = findViewById(R.id.Check);
        login = findViewById(R.id.signin_btn);
        teie = findViewById(R.id.teieradio);
        tbtd = findViewById(R.id.tbtdradio);
        Checkconnection();
        SharedPreferences preference = getSharedPreferences("myPreference", MODE_PRIVATE);
         user = preference.getString("user", "");
         pass = preference.getString("pass", "");
        String Address = preference.getString("address", "");
        if (!TextUtils.isEmpty(user) || !TextUtils.isEmpty(pass)) {
            startActivity(new Intent(LogiinAvtivity.this, WelcomeActivity.class));
            finish();
            return;
        }
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean visible) {
                if (!visible) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        tbtd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    server = "tbtd";
                    teie.setChecked(false);
                }
            }
        });
        teie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                    server = "teie";
                    tbtd.setChecked(false);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Checkconnection() ;
                if(c == 2){
                    Toast.makeText(LogiinAvtivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
                    return;
                }
                user = username.getText().toString().toUpperCase();
                pass = password.getText().toString();
                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LogiinAvtivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                Zlogin();
            }
        });
    }

    private void Zlogin(){
        address = "10.9.9.12" ;
        if (!tbtd.isChecked() & !teie.isChecked()) {
            Toast.makeText(getApplicationContext(), "Choose Company First", Toast.LENGTH_SHORT).show();
        } else {
//            if (tbtd.isChecked()) {
//                int number = Checkconnection();
//                if (number == 0) {
//                    loginurl = "http://"+address+":8030/login/";
//                    urlget = "http://"+address+":8030/assets/";
//                } else if (number == 1) {
//                    urlget = "http://tbtd-app.tbtd-egypt.com:8040/assets/";
//                    loginurl = "http://tbtd-app.tbtd-egypt.com:8040/login/";
//
//                } else if (number == 2) {
//                    Toast.makeText(LogiinAvtivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                }
            if (tbtd.isChecked()) {
                int number = Checkconnection();
                if (number == 2) {
                    Toast.makeText(LogiinAvtivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Always use AppConfig to switch automatically between QAS/PROD
                    loginurl = AppConfig.getLoginUrl("tbtd");
                    urlget = AppConfig.getAssetsUrl("tbtd");
                    android.util.Log.d("DEBUG_URLS", "TBTD → Login: " + loginurl + ", Assets: " + urlget);
                    android.util.Log.e("ASSET_URL", "USING URL: " + urlget);


                }

                Retrofit retrofit = retrofitClient.connect(loginurl, user, pass);
                final SapClient sapClient = retrofit.create(SapClient.class);
                Call<List<Users>> call = sapClient.logintbtd(user, pass);
                call.enqueue(new Callback<List<Users>>() {
                    @Override
                    public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                        try {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty())
                                Toast.makeText(LogiinAvtivity.this, "User Or Password Not Correct ", Toast.LENGTH_SHORT).show();
                            else {
                                if (user.toUpperCase().equals(response.body().get(0).getUNAME()) && pass.equals(response.body().get(0).getPASSWORD())) {
                                    SharedPreferences.Editor editor = getSharedPreferences("myPreference", MODE_PRIVATE).edit();
                                    editor.putString("user", user);
                                    editor.putString("pass", pass);
                                    editor.putString("server", server);
                                    editor.putString("Network", ipNetwork);
                                    editor.putString("Address", address);
                                    editor.apply();
                                    intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                    intent.putExtra("company", server); // 👈 send selected company (tbtd or teie)
                                    startActivity(intent);

                                    finish();
                                    Toast.makeText(getApplicationContext(), "You are Ready For Scanning Processes", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), "Username Or Password is Not correct", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Users>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error Connection with  API " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return;
            } else {
                address = "10.9.9.12" ;
                int number = Checkconnection();
//                if (number == 0) {
//                    loginurl = "http://"+address+":8030/login/";
//                    urlget = "http://"+address+":8030/assets/";
//                    ipNetwork = "APN";
//                } else if (number == 1) {
//                    urlget = "http://nteie-app.tbtd-egypt.com:8030/assets/";
//                    loginurl = "http://nteie-app.tbtd-egypt.com:8030/login/";
//                } else if (number == 2) {
//                    Toast.makeText(LogiinAvtivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
//                }
                if (number == 2) {
                    Toast.makeText(LogiinAvtivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Use AppConfig for TEIE as well
                    loginurl = AppConfig.getLoginUrl("teie");
                    urlget = AppConfig.getAssetsUrl("teie");
                    android.util.Log.d("DEBUG_URLS", "TEIE → Login: " + loginurl + ", Assets: " + urlget);

                }
                Retrofit retrofit = retrofitClient.connect(loginurl, user, pass);
                final SapClient sapClient = retrofit.create(SapClient.class);
                Call<List<Users>> call = sapClient.login(user, pass);
                call.enqueue(new Callback<List<Users>>() {
                    @Override
                    public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                        try {
                            if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()){
                                Toast.makeText(LogiinAvtivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                if (user.toUpperCase().equals(response.body().get(0).getUNAME()) && pass.equals(response.body().get(0).getPASSWORD())) {
                                    SharedPreferences.Editor editor = getSharedPreferences("myPreference", MODE_PRIVATE).edit();
                                    editor.putString("get", urlget);
                                    editor.putString("user", user);
                                    editor.putString("pass", pass);
                                    editor.putString("server", server);
                                    editor.putString("Address", address);
                                    editor.apply();
                                    intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                    intent.putExtra("company", server); // 👈 send selected company (tbtd or teie)
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(getApplicationContext(), "You are Ready For Scanning Processes", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), "Username Or Password is Not correct", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Users>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error Connection with API " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
        }
    }


    private int Checkconnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 0;
            }
        } else {
            return 2;
        }
        return 3;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }


}
