package com.ebmacs.helpapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class FarmacinoAdapter extends RecyclerView.Adapter<FarmacinoAdapter.ViewHolder> {

    ArrayList<Farmacino> arrayList;
    Context context;
    String language;
    public FarmacinoAdapter(Context context, ArrayList<Farmacino> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.farmacino_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        Farmacino farmacino = arrayList.get(i);

        viewHolder.txtName.setText(farmacino.getName());
        viewHolder.txtExpire.setText(farmacino.getExpire());
        viewHolder.txtTime.setText(farmacino.getTime());
        viewHolder.imgPicture.setImageResource(farmacino.getImage());


        viewHolder.layoutDoar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Doar", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtTime, txtExpire;
        private ImageView imgPicture;
        private RelativeLayout linearLayout;
        private LinearLayout layoutDoar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtExpire = itemView.findViewById(R.id.txtExpire);
            imgPicture = itemView.findViewById(R.id.imgPicture);
            layoutDoar = itemView.findViewById(R.id.layoutDoar);
        }


    }
}