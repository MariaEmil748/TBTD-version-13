package com.testing.inventorytracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class WelcomeActivity extends AppCompatActivity {

    CardView NewAsset,AssetTransfer,Assetaudit,AssetSearch, RoomMapping;
    Bundle bundle = new Bundle();
    private SharedPreferences preference;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logoutmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.Search_item);
        MenuItem menuItem2 = menu.findItem(R.id.savaRoom);
        MenuItem menuItem3 = menu.findItem(R.id.savasummary);
        MenuItem menuItem4 = menu.findItem(R.id.delete);
        MenuItem menuItem5 = menu.findItem(R.id.AssignAssets);

        menuItem.setVisible(false);
        menuItem2.setVisible(false);
        menuItem3.setVisible(false);
        menuItem4.setVisible(false);
        menuItem5.setVisible(false);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView companyLogo = findViewById(R.id.companyLogo); // add this in layout later

// get selected company from intent
        String company = getIntent().getStringExtra("company");

// or from SharedPreferences if needed
        if (company == null) {
            SharedPreferences preference = getSharedPreferences("myPreference", MODE_PRIVATE);
            company = preference.getString("server", "tbtd");
        }

// set logo depending on selection
        if (company.equalsIgnoreCase("teie")) {
            companyLogo.setImageResource(R.drawable.eteie); // change to your TEIE logo file
        } else {
            companyLogo.setImageResource(R.drawable.eytbtd); // change to your TBTD logo file
        }



        preference =getSharedPreferences("myPreference",MODE_PRIVATE);
        final String server = preference.getString("server", "");
        final String user = preference.getString("user", "");
        final String pass = preference.getString("pass", "");
        final String address = preference.getString("Address", "");


        bundle.putString("server",server);
        bundle.putString("user",user);
        bundle.putString("pass",pass);
        bundle.putString("Address",address);

        NewAsset = findViewById(R.id.NewAsset);
        Assetaudit = findViewById(R.id.AssetAudit);
        AssetTransfer = findViewById(R.id.AssetTransfer);
        AssetSearch = findViewById(R.id.AssetSearch);
        RoomMapping = findViewById(R.id.Assign);
        NewAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAsset newAsset = new NewAsset();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameW, newAsset).addToBackStack(null).commit();
                newAsset.setArguments(bundle);
            }
        });

        Assetaudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDataDetailsFragment readDetailsFragment = new ReadDataDetailsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameW,readDetailsFragment ).addToBackStack(null).commit();
                bundle.putString("notaudit","not_transfer");
                readDetailsFragment.setArguments(bundle);
            }
        });

        AssetTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDataDetailsFragment readDetailsFragment = new ReadDataDetailsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameW,readDetailsFragment ).addToBackStack(null).commit();
                bundle.putString("notaudit","transfer");
                readDetailsFragment.setArguments(bundle);
            }
        });

        AssetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetHistory assetHistory = new AssetHistory();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameW, assetHistory).addToBackStack(null).commit();
                assetHistory.setArguments(bundle);
            }
        });
        RoomMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               RoomAssetMapping roomAssetMapping =new RoomAssetMapping();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameW, roomAssetMapping).addToBackStack(null).commit();
                roomAssetMapping.setArguments(bundle);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.logout){
            SharedPreferences.Editor editor = preference.edit();
            editor.clear();
            editor.apply();
            new Roomcontrollar(getApplicationContext(), "DELETE").execute();
            finish();
            startActivity(new Intent(WelcomeActivity.this,LogiinAvtivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void setActionBarTitle(String title , String subtitle) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(Html.fromHtml("<h4>" + subtitle + "</h4>"));
    }

    public int Checkconnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(null!= activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return  1 ;

            }else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return  0 ;
            }
        }else{
            return  2 ;
        }
        return 2 ;
    }
}
