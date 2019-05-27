package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebmacs.helpapp.Chat.ChatActivity;

import com.ebmacs.helpapp.Models.Groups;
import com.ebmacs.helpapp.R;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<Groups> arrayList = new ArrayList<>();
    private Context context;

    public FriendsAdapter(Context context, ArrayList<Groups> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friends_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Groups groups = arrayList.get(i);
        viewHolder.txtMsg.setText(groups.getGroupDescription());
        viewHolder.txtName.setText(groups.getGroupName());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("type", "friends");
                intent.putExtra("Group_Id", arrayList.get(i).getGroupId());
                intent.putExtra("Group_Name", arrayList.get(i).getGroupName());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrayList == null) {
            return 0;
        }
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


        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMsg = (TextView) itemView.findViewById(R.id.txtMsg);
            imgSender = (ImageView) itemView.findViewById(R.id.imgSender);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);

        }


    }

    public void updateList(ArrayList<Groups> list) {
        this.arrayList = list;
        notifyDataSetChanged();
    }

}