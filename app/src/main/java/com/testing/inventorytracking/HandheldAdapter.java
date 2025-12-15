package com.testing.inventorytracking;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.testing.inventorytracking.R;

public class HandheldAdapter extends RecyclerView.Adapter<HandheldAdapter.HandheldViewHolder> {

    private HandheldAdapter.PassData passData;
    String server;
    List<Tiasset> list ;


    public HandheldAdapter(ArrayList<Tiasset> searchList, String server) {
        this.list = searchList ;
        this.server = server ;
    }

    public interface PassData {
        void sendData(Tiasset tiasset);
    }



    public HandheldAdapter(HandheldAdapter.PassData passData, List<Tiasset> list, String server) {
        this.passData = passData;
        this.list = list;
        this.server = server;
    }


    @NonNull
    @Override
    public HandheldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HandheldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemstyle, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final HandheldViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(list.get(position).getANLHTXT().equals("0") ) {
            holder.serialnumber.setText(list.get(position).getANLN1());
            holder.description.setText(list.get(position).getTXT50());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        passData.sendData(list.get(position));
                    }catch (NullPointerException r){
                        return;
                    }
                }
            });
        } else if (server.equals("teie")){
            holder.serialnumber.setText(list.get(position).getANLN1());
            holder.description.setText(list.get(position).getTXT50());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        passData.sendData(list.get(position));
                    }catch (NullPointerException r){
                        return;
                    }
                }
            });
        }
        else{
            holder.serialnumber.setText(list.get(position).getANLHTXT());
            holder.description.setText(list.get(position).getTXT50());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try {
                       passData.sendData(list.get(position));
                   }catch (NullPointerException r){
                       return;
                   }
                }
            });
        }
            holder.empName.setText(list.get(position).getENAME());
            holder.empHr.setText(list.get(position).getROOMNO());
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    public class HandheldViewHolder extends RecyclerView.ViewHolder {
        TextView serialnumber, description, empName , empHr;
        CardView cardView;

        public HandheldViewHolder(@NonNull View itemView) {
            super(itemView);
            serialnumber = itemView.findViewById(R.id.serialitem);
            description = itemView.findViewById(R.id.descite);
            empName = itemView.findViewById(R.id.employeename);
            empHr = itemView.findViewById(R.id.employeehr);
            cardView = itemView.findViewById(R.id.cardd);
        }
    }

    public void updatingList(List<Tiasset> newlist){
        list= new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }



}
