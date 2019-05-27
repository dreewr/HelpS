package com.ebmacs.helpapp.Chat;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.Activities.individualChatActivity;
import com.ebmacs.helpapp.Models.InboxChat;
import com.ebmacs.helpapp.Models.UserDetails;
import com.ebmacs.helpapp.R;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    ArrayList<InboxChat> arrayList;
    Context context;

    public InboxAdapter(Context context, ArrayList<InboxChat> arrayList) {
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

        viewHolder.txtName.setText(arrayList.get(i).getSenderName());

        viewHolder.txtTime.setText(arrayList.get(i).getTime());
        viewHolder.txtMsg.setText(arrayList.get(i).getLastMessage());
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, individualChatActivity.class);
                intent.putExtra("type", "inbox");
                intent.putExtra("senderId",arrayList.get(i).getSenderId());
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
        private TextView txtMsg;
        private ImageView imgSender;
        private TextView txtTime;
        private ImageView imgReceiver;
        private LinearLayout linearLayout;
        private CardView cardViewReceiver;
        RelativeLayout relativeLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime =  itemView.findViewById(R.id.txtTime);
            txtMsg = (TextView) itemView.findViewById(R.id.txtMsg);
            imgSender = (ImageView) itemView.findViewById(R.id.imgSender);
            relativeLayout=itemView.findViewById(R.id.parentLayout);

        }


    }

    public void updateList(ArrayList<InboxChat> list) {
        this.arrayList = list;
        notifyDataSetChanged();
    }

}