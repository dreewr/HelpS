package com.ebmacs.helpapp.Chat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Activities.ProfileActivity;
import com.ebmacs.helpapp.Dialogs.InsetNewUserChatDialog;
import com.ebmacs.helpapp.Models.InboxChat;
import com.ebmacs.helpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {


    private final String URL = "http://ebmacshost.com/help/api/group/massage_list";

    RecyclerView recyclerView;
    InboxAdapter adapter;
    ArrayList<InboxChat> arrayList;
    LinearLayoutManager linearLayoutManager;
    ImageView imgHelp, new_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        initializeViews();
        setClickListeners();
        fetchAllChat();
    }

    private void setRecyclerView() {
        adapter = new InboxAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        imgHelp = findViewById(R.id.imgHelp);
        new_chat = findViewById(R.id.new_chat);
    }

    private void setClickListeners() {
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, ProfileActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsetNewUserChatDialog dialog = new InsetNewUserChatDialog();   // open dialog fragment
                dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void fetchAllChat() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {


                                JSONArray array = jsonObject.getJSONArray("messagelist");

                                arrayList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    InboxChat result = new InboxChat();
                                    result.setLastMessage(object.getString("last_massage"));


                                    String t = object.getString("date");
                                    String[] parts = t.split(" ");
                                    String date = parts[0]; // 004
                                    String time = parts[1];
                                    result.setTime(time);

                                    JSONObject object2 = array.getJSONObject(i);
                                    JSONObject userdetail = object2.getJSONObject("userdetail");
                                    result.setSenderName(userdetail.getString("user_name"));
                                    result.setSenderId(userdetail.getString("user_id"));


                                    arrayList.add(result);
                                    Log.i("Sender ID",object.getString("sender_id"));
                                    Log.i("Message Time", time);
                                    Log.i("This is UserName",userdetail.getString("user_name") );

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
                SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                String user_id = prefs.getString("user_id", "No name defined");
                params.put("user_id", user_id);
                Log.i("READ ALL iNBOX CHAT", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}