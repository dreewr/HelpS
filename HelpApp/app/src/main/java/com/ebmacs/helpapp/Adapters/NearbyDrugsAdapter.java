package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.Activities.SHowDetailNearbyDrugsActivity;
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.Models.NearbyDrugs;
import com.ebmacs.helpapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mudassir-ktk on 2/21/2019.
 */

public class NearbyDrugsAdapter extends RecyclerView.Adapter<NearbyDrugsAdapter.ViewHolder> implements Filterable {

    ArrayList<NearbyDrugs> arrayList;
    ArrayList<NearbyDrugs> arrayListFull;
    Context context;


    public NearbyDrugsAdapter(Context context, ArrayList<NearbyDrugs> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;

        // for temperory STATIC List is being used. will be replaced after DB.
        this.arrayListFull = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.farmacino_item, viewGroup, false);
        NearbyDrugsAdapter.ViewHolder viewHolder = new NearbyDrugsAdapter.ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        NearbyDrugs farmacino = arrayList.get(i);

        viewHolder.txtName.setText(farmacino.getName());
        viewHolder.txtExpire.setText(farmacino.getExpire());
        viewHolder.txtTime.setText(farmacino.getTime());
        viewHolder.textDescription.setText(farmacino.getDescription());
        Picasso.get()
                .load(arrayList.get(i).getImageUrl())
                .into(viewHolder.imgPicture);

        Log.i("Name", farmacino.getName());
        Log.i("Expire", farmacino.getExpire());
        Log.i("Time", farmacino.getTime());
        Log.i("descccc", farmacino.getDescription());

        viewHolder.farm_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SHowDetailNearbyDrugsActivity.class);
                intent.putExtra("NearbyImageUrl", arrayList.get(i).getImageUrl());
                intent.putExtra("distance", arrayList.get(i).getDistance());
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtTime, txtExpire, textDescription;
        private ImageView imgPicture;
        private LinearLayout farm_item_layout;
        private LinearLayout layoutDoar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime1);
            txtExpire = itemView.findViewById(R.id.txtExpire);
            imgPicture = itemView.findViewById(R.id.imgPicture);
            layoutDoar = itemView.findViewById(R.id.layoutDoar);
            farm_item_layout = itemView.findViewById(R.id.farm_item_layout);
            textDescription = itemView.findViewById(R.id.medican_description);
        }


    }


    public Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NearbyDrugs> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (NearbyDrugs item : arrayListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
