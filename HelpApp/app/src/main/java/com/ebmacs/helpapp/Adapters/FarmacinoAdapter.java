package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ebmacs.helpapp.Activities.EditMedicanActivity;
import com.ebmacs.helpapp.Activities.MyFarmacinhaActivity;
import com.ebmacs.helpapp.Activities.ShowMedicanDetailActivity;
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FarmacinoAdapter extends RecyclerView.Adapter<FarmacinoAdapter.ViewHolder> implements Filterable {
  //  ArrayList<Farmacino> arrayList;
  //  ArrayList<Farmacino> arrayListFull;
    ArrayList<Farmacino2> arrayList;
    ArrayList<Farmacino2> arrayListFull;
    Context context;
    int popUpCount = 0;


    String language;
    public FarmacinoAdapter(Context context, ArrayList<Farmacino2> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;

        // for temperory STATIC List is being used. will be replaced after DB.
        this.arrayListFull = MyFarmacinhaActivity.fullDetailList;
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
//        popUpCount++;

//        if(popUpCount % 2 == 0){
//            MyFarmacinhaActivity.popup();
//        }

        if(MyFarmacinhaActivity.checkPopUp == false && i == 1){
            MyFarmacinhaActivity.popup();
            MyFarmacinhaActivity.checkPopUp = true;
        }


        Farmacino2 farmacino = arrayList.get(i);

        viewHolder.txtName.setText(farmacino.getName());
        viewHolder.txtExpire.setText(farmacino.getExpire());
        viewHolder.txtTime.setText(farmacino.getTime());
        viewHolder.textDescription.setText(farmacino.getDescription());
        viewHolder.txtTime.setText(farmacino.getTime());


        Picasso.get()
                .load(arrayList.get(i).getImageUrl())
                .into(viewHolder.imgPicture);

        viewHolder.farm_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditMedicanActivity.SelectedMedican = i;

                Farmacino2 farmacinoSnd = arrayList.get(i);
                EditMedicanActivity.medicanDetail = farmacinoSnd;

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                farmacinoSnd.foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
                MyFarmacinhaActivity.selectedFarma = farmacinoSnd;
                Intent intent = new Intent(context,ShowMedicanDetailActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtra("MainImageURL",arrayList.get(i).getImageUrl());
                intent.putExtra("madicine_id",arrayList.get(i).getMadicinId());

                bundle.putString("itemName",farmacinoSnd.getName());
                bundle.putString("itemExpire",farmacinoSnd.getExpire());
                bundle.putString("itemTime","6 de 6 horas");
              //  bundle.putByteArray("itemPicture",byteArray);
              //  bundle.putParcelable("itemPicture",farmacinoSnd.foto);
              //  intent.putExtra("BitmapImage", farmacinoSnd.foto);

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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

    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    public Filter searchFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Farmacino2> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Farmacino2 item : arrayListFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtTime, txtExpire,textDescription;
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
}