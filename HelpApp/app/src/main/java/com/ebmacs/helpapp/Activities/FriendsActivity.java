package com.ebmacs.helpapp.Activities;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Adapters.FriendsAdapter;
import com.ebmacs.helpapp.Dialogs.NewGroupDialog;
import com.ebmacs.helpapp.Models.Groups;
import com.ebmacs.helpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    private final String URL = "http://ebmacshost.com/help/api/group/mygroups";
    private final String cURL = "http://ebmacshost.com/help/api/group/create_group";


    RecyclerView recyclerView;
    com.ebmacs.helpapp.Adapters.FriendsAdapter adapter;
    ArrayList<Groups> arrayList;
    LinearLayoutManager linearLayoutManager;

    Toolbar toolbar;
    ImageView imageMenue;

    public static String descriptin = null;
    public static String groupName = null;
    public static String creater_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolbar = findViewById(R.id.toolbar);
        imageMenue = toolbar.findViewById(R.id.icon_more_frinds);
        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);


        initializeViews();
        setRecyclerView();
        setListeneres();
        myGroups();
    }

    void setListeneres() {
        imageMenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewGroupDialog dialog = new NewGroupDialog();   // open dialog fragment
                dialog.show(getSupportFragmentManager(),"");

            }
        });

    }

    private void setRecyclerView() {
        adapter = new FriendsAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    ////////My Joined  Groups
    private void myGroups() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {
                                JSONArray array = jsonObject.getJSONArray("mygroup");
                                Log.i("------UserData", array + "");

                                arrayList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    Groups result = new Groups();
                                    result.setGroupName(object.getString("group_name"));
                                    result.setGroupDescription(object.getString("group_discription"));
                                    result.setGroupCreaterId(object.getString("group_creater_id"));
                                    result.setGroupId(object.getString("group_id"));

                                    arrayList.add(result);
                                    Log.i("-GroupDescription", result.getGroupDescription());
                                    Log.i("-GroupName", result.getGroupName());

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
                Log.i("USER_ID", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
