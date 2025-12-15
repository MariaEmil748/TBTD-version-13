package com.testing.inventorytracking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

public class Roomcontrollar extends AsyncTask<Void, Void, Void> {

    Context context;
    String type;
    List<Tiasset> assets;

    public Roomcontrollar(Context context,String type) {
        this.context=context;
        this.type = type;
    }

    public Roomcontrollar(Context context,String type, List<Tiasset> assets  ) {
        this.context=context;
        this.type = type;
        this.assets = assets;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (type.equals("DELETE")) {
            MyRoomDatabase.getInstance(context).roominterface().deleteAssets();

        } else if (type.equals("INSERT")) {

            MyRoomDatabase.getInstance(context).roominterface().InsertAssets(assets);

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
    }
}
