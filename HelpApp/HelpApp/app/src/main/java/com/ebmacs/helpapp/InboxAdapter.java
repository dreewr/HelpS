package com.ebmacs.helpapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public InboxAdapter(Context context, ArrayList<String> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inbox_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("type", "inbox");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private  TextView txtMsg;
        private ImageView imgSender;
        private TextView txtTime;
        private ImageView imgReceiver;
        private LinearLayout linearLayout;
        private CardView cardViewReceiver;


        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMsg = (TextView) itemView.findViewById(R.id.txtMsg);
            imgSender = (ImageView) itemView.findViewById(R.id.imgSender);
            imgReceiver = (ImageView) itemView.findViewById(R.id.imgReciever);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
            cardViewReceiver = (CardView) itemView.findViewById(R.id.cardViewReceiver);

        }


    }

    public void updateList(ArrayList<String> list){
        this.arrayList=list;
        notifyDataSetChanged();
    }

}