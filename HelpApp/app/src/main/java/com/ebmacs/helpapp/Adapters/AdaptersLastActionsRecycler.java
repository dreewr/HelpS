package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebmacs.helpapp.Models.LastAction;
import com.ebmacs.helpapp.R;

import java.util.List;

import androidx.annotation.NonNull;



public class AdaptersLastActionsRecycler extends RecyclerView.Adapter<AdaptersLastActionsRecycler.MyViewHolder> {


    List<LastAction> actionList ;
    Context context;


    public AdaptersLastActionsRecycler( Context context,List<LastAction> actionList) {
        this.actionList = actionList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recylce_last_action,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txtUserName.setText(actionList.get(position).getOtherUserName());
        holder.txtTime.setText(actionList.get(position).getActionTime());
        if(actionList.get(position).getActionType().equals("points_gvn")){
            holder.txtMessage.setText("Você deu pontos para");
            holder.txtMessage.setTextColor(Color.parseColor("#485845"));
        }
        else
            if(actionList.get(position).getActionType().equals("points_rcvd")){
                holder.txtMessage.setText("Você recebeu pontos de");
                holder.txtMessage.setTextColor(Color.parseColor("#0C7C17"));

            }
            else{
                holder.txtMessage.setText("Você tem conversar com");
                holder.txtMessage.setTextColor(Color.parseColor("#993344"));
            }

    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtMessage,txtUserName,txtTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage = itemView.findViewById(R.id.txtMessage_lastAction);
            txtUserName = itemView.findViewById(R.id.txtUserNameLastAction);
            txtTime     = itemView.findViewById(R.id.txtTimeLastAction);
        }
    }
}
