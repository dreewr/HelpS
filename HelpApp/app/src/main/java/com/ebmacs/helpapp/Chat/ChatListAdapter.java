package com.ebmacs.helpapp.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.R;
import com.ebmacs.helpapp.UserType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public ChatListAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;

    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ChatMessage message = chatMessages.get(position);

        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);

                holder1 = new ViewHolder1();

                holder1.parentLayout = v.findViewById(R.id.parentLayout);
                holder1.messageTextView = v.findViewById(R.id.message_text);
                holder1.timeTextView =  v.findViewById(R.id.time_text);
                holder1.txtDay = v.findViewById(R.id.txtDay);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }

            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            if(message.getMessageText().equals("")){
                holder1.parentLayout.setVisibility(View.GONE);
            }
            else {
                holder1.parentLayout.setVisibility(View.VISIBLE);
            }
            holder1.messageTextView.setText(message.getMessageText());

            if(!(message.getDay().contains("s_"))){
                holder1.txtDay.setVisibility(View.VISIBLE);
                holder1.txtDay.setText(message.getDay());
            }
            else {
                holder1.txtDay.setVisibility(View.GONE);
            }

        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);

                holder2 = new ViewHolder2();
                holder2.messageTextView = v.findViewById(R.id.message_text);
                holder2.timeTextView = v.findViewById(R.id.time_text);
                holder2.parentLayout = v.findViewById(R.id.parentLayout);
                holder2.txtDay = v.findViewById(R.id.txtDay);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();
            }

            if(message.getMessageText().equals("")){
                holder2.parentLayout.setVisibility(View.GONE);
            }
            else {
                holder2.parentLayout.setVisibility(View.VISIBLE);
            }
            holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));

            if(!(message.getDay().contains("s_"))){
                holder2.txtDay.setVisibility(View.VISIBLE);
                holder2.txtDay.setText(message.getDay());
            }
            else {
                holder2.txtDay.setVisibility(View.GONE);
            }
        }

        return v;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public RelativeLayout parentLayout;
        public TextView txtDay;
    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;
        public RelativeLayout parentLayout;
        public TextView txtDay;
    }

}
