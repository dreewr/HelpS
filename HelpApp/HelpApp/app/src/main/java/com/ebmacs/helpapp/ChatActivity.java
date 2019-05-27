package com.ebmacs.helpapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {


    ImageView imgBack;
    String name, image="";
    private ListView chatListView;
    private EditText edtMsg;
    private ArrayList<ChatMessage> chatMessages;
    private ChatListAdapter listAdapter;
    ImageView imgSend;
    TextView txtSend;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String type;
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        initializeViews();
        setChat();
    }

    public void sendBack(View view){
        onBackPressed();
    }

    private void setChat() {
        int count=0;
        Calendar calendar = Calendar.getInstance();

        for(int i=0;i<2;i++) {
            final ChatMessage message = new ChatMessage();
            String messageText = "E um fato ha muito estabelecido que um leitor";
            if(i==1){
                message.setUserType(UserType.SELF);
            }
            else
                message.setUserType(UserType.OTHER);
            message.setName("Username");
            message.setMessageText(messageText);
            message.setMessageTime(calendar.getTimeInMillis());

            String day = ConvertDate.chatDate(calendar.getTimeInMillis());
            if (chatMessages.size() > 0) {
                String pre_day = chatMessages.get(count - 1).getDay();
                if (pre_day.contains("s_")) {
                    if (pre_day.replace("s_", "").equalsIgnoreCase(day)) {
                        day = "s_" + day;
                    }
                } else if (day.equalsIgnoreCase(pre_day)) {
                    day = "s_" + day;
                }

            }

            message.setDay(day);


            chatMessages.add(message);
            count++;
        }
    }


    private void initializeViews() {
        imgBack = findViewById(R.id.imgBack);
        edtMsg = findViewById(R.id.edtMsg);
        chatListView = findViewById(R.id.chat_list_view);
        edtMsg.setOnKeyListener(keyListener);
        chatMessages = new ArrayList<>();
        listAdapter = new ChatListAdapter(chatMessages, this);
        txtSend = findViewById(R.id.txtSend);
        chatListView.setAdapter(listAdapter);
        btnAccept = findViewById(R.id.btnAccept);

        if(type.equals("friends")){
            btnAccept.setVisibility(View.VISIBLE);
        }

    }

    private void sendMessage(final String messageText, final UserType userType)
    {
        if(messageText.trim().length()==0)
            return;

        final ChatMessage message = new ChatMessage();
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());


        String day = ConvertDate.chatDate(new Date().getTime());
        if(chatMessages.size()!=0) {
            String pre_day = chatMessages.get(chatMessages.size() - 1).getDay();
            if(pre_day.contains("s_")){
                if(pre_day.replace("s_","").equalsIgnoreCase(day)){
                    day = "s_" + day;
                }
            }
            else if(day.equalsIgnoreCase(pre_day)){
                day="s_" + day;
            }
        }

        message.setDay(day);

        chatMessages.add(message);

        if(listAdapter!=null) {
            listAdapter.notifyDataSetChanged();
            scrollMyListViewToBottom();
        }
    }

    private void scrollMyListViewToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatListView.setSelection(listAdapter.getCount() - 1);
            }
        });
    }


    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if(v==edtMsg)
                {
                    sendMessage(editText.getText().toString(), UserType.SELF);
                }

                edtMsg.setText("");

                return true;
            }
            return false;

        }
    };



}
