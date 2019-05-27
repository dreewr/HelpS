package com.ebmacs.helpapp.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Adapters.FriendsAdapter;
import com.ebmacs.helpapp.Adapters.MessageListAdapter;
import com.ebmacs.helpapp.ConvertDate;
import com.ebmacs.helpapp.Models.Groups;
import com.ebmacs.helpapp.Models.Messages;
import com.ebmacs.helpapp.R;
import com.ebmacs.helpapp.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {


    private final String cURL = "http://ebmacshost.com/help/api/group/insertChat";
    private final String URL = "http://ebmacshost.com/help/Api/group/readgroup_chat";
    private ArrayList<Messages> arrayList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    MessageListAdapter adapter;
    String Group_Id;
    String Group_Name;
    TextView txtName;


    ImageView imgBack;
    private EditText edtMsg;
    private com.ebmacs.helpapp.Chat.ChatListAdapter listAdapter;
    TextView txtSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeViews();


        Group_Id = getIntent().getStringExtra("Group_Id");
        Group_Name = getIntent().getStringExtra("Group_Name");
        txtName.setText(Group_Name);
        readAllChat();
    }

    public void sendBack(View view) {
        onBackPressed();
    }





    private void initializeViews() {

        txtName = findViewById(R.id.txtName);
        imgBack = findViewById(R.id.imgBack);
        edtMsg = findViewById(R.id.edtMsg);
        txtSend = findViewById(R.id.txtSend);



        recyclerView = findViewById(R.id.msgRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);


        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertGroupChat();
                readAllChat();

                edtMsg.setText("");
            }
        });

    }




    private void insertGroupChat() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, cURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {
                                Log.i("Message sent", "DONE");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                String user_id = prefs.getString("user_id", "No name defined");
                String msg = edtMsg.getText().toString();
                params.put("massage_text", msg);
                params.put("user_id", user_id);
                params.put("group_id", Group_Id);

                Log.i("CHAT_MSG", params.toString()
                );

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    ////////Read All chat
    private void readAllChat() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {


                                JSONArray array = jsonObject.getJSONArray("chatgroup");
                                Log.i("------UserData", array + "");

                                arrayList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    Messages result = new Messages();
                                    result.setMemberId(object.getString("member_id"));
                                    result.setReceivedMessages(object.getString("massage_text"));
                                    result.setUserName(object.getString("user_name"));
                                    String dateNtime = object.getString("inserted_date");
                                    String[] parts = dateNtime.split(" ");
                                    String date = parts[0]; // 004
                                    String time = parts[1];
                                    result.setMsgTime(time);

                                    arrayList.add(result);
                                    Log.i("---GroupData", arrayList.toString());
                                    setRecyclerView();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", Group_Id);
                Log.i("GroupIdforAllchat", Group_Id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setRecyclerView() {
        adapter = new MessageListAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}