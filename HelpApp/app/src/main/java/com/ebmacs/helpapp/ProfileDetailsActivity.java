package com.ebmacs.helpapp;

import de.hdodenhof.circleimageview.CircleImageView;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Adapters.AdaptersLastActionsRecycler;
import com.ebmacs.helpapp.Models.LastAction;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity {

    private static final String URL = "http://ebmacshost.com/help/api/update_profile";


    List<LastAction> lastActionList;
    String userName;
    String userEmail;
    String userPhoto;
    String userContact;
    String userAddress;
    String CPF;

    RecyclerView recyclerView;
    AdaptersLastActionsRecycler adaptersLastActionsRecycler;

    CircleImageView profile_pic;
    EditText edtUserName, edtUserEmail, edtUserAddress, edtContact, edtNmaeEdit, edtEmailEdit, edtContEdt, edtAdrresEdit, edtCPF, edtCPF2;

    ImageView imageEdit, imageEditDone;

    LinearLayout layoutNoramal, layoutEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        prepareLastActionsList();
        inIT();
        updatValuse();

        setListeneres();
    }

    void inIT() {

        recyclerView = findViewById(R.id.recycle_last_commits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptersLastActionsRecycler = new AdaptersLastActionsRecycler(ProfileDetailsActivity.this, lastActionList);
        recyclerView.setAdapter(adaptersLastActionsRecycler);

        profile_pic = findViewById(R.id.imageProfileDetail);
        edtUserName = findViewById(R.id.edtNamePdtl);
        edtUserEmail = findViewById(R.id.edtEmlPdtl);
        edtUserAddress = findViewById(R.id.edtAdrPdtl);
        edtContact = findViewById(R.id.edtContPdtl);
        edtCPF = findViewById(R.id.edtCPF);

        edtNmaeEdit = findViewById(R.id.edtNamePdtlEdit);
        edtEmailEdit = findViewById(R.id.edtEmlPdtlEdit);
        edtContEdt = findViewById(R.id.edtContPdtlEdit);
        edtAdrresEdit = findViewById(R.id.edtAdrPdtlEdit);
        edtCPF2 = findViewById(R.id.edtCPF2);

        imageEdit = findViewById(R.id.imageEdit);
        imageEdit.setColorFilter(getResources().getColor(R.color.colorPrimary));

        imageEditDone = findViewById(R.id.imageEditDone);
        imageEditDone.setColorFilter(getResources().getColor(R.color.colorPrimary));

        layoutNoramal = findViewById(R.id.layoutNormal);
        layoutEdit = findViewById(R.id.layoutEdit);

    }


    void setListeneres() {
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtNmaeEdit.setText(edtUserName.getText().toString());
                edtEmailEdit.setText(edtUserEmail.getText().toString());
                edtContEdt.setText(edtContact.getText().toString());
                edtAdrresEdit.setText(edtUserAddress.getText().toString());
                edtCPF2.setText(edtCPF.getText().toString());
                layoutNoramal.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.VISIBLE);

            }
        });

        imageEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfile();
                updatingProfile();
            }
        });
    }


    void prepareLastActionsList() {

        lastActionList = new ArrayList<>();
        LastAction lastAction = new LastAction();
        lastAction.setActionType("msg");
        lastAction.setOtherUserName("Hamid");
        lastAction.setOtherUserID("3");
        lastAction.setActionTime("10:30");
        lastActionList.add(lastAction);

        LastAction lastAction1 = new LastAction();
        lastAction1.setActionType("points_gvn");
        lastAction1.setOtherUserName("Shani");
        lastAction1.setOtherUserID("3");
        lastAction1.setActionTime("14:32");
        lastActionList.add(lastAction1);

        LastAction lastAction2 = new LastAction();
        lastAction2.setActionType("points_rcvd");
        lastAction2.setOtherUserName("Junaid");
        lastAction2.setOtherUserID("3");
        lastAction2.setActionTime("12:10");
        lastActionList.add(lastAction2);
        LastAction lastAction3 = new LastAction();
        lastAction3.setActionType("msg");
        lastAction3.setOtherUserName("Hamid");
        lastAction3.setOtherUserID("3");
        lastAction3.setActionTime("10:30");
        lastActionList.add(lastAction3);

        LastAction lastAction4 = new LastAction();
        lastAction4.setActionType("points_gvn");
        lastAction4.setOtherUserName("Shani");
        lastAction4.setOtherUserID("3");
        lastAction4.setActionTime("14:32");
        lastActionList.add(lastAction4);

        LastAction lastAction5 = new LastAction();
        lastAction5.setActionType("points_rcvd");
        lastAction5.setOtherUserName("Junaid");
        lastAction5.setOtherUserID("3");
        lastAction5.setActionTime("12:10");
        lastActionList.add(lastAction5);
    }

    void updatValuse() {
        SharedPreferences prefs = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);
        userName = prefs.getString(MyShardPrefernces.UserName, null);//"No name defined" is the default value.
        userEmail = prefs.getString(MyShardPrefernces.UserEmail, null);
        userPhoto = prefs.getString(MyShardPrefernces.UserImageUrl, null);
        userContact = prefs.getString(MyShardPrefernces.UserContact, null);
        userAddress = prefs.getString(MyShardPrefernces.UserAddressr, null);
        String cpf = prefs.getString(MyShardPrefernces.UserCpf, null);


        if (userPhoto != null) {
            Picasso.get().load(userPhoto).into(profile_pic);

        }
        edtUserName.setText(userName);
        edtUserEmail.setText(userEmail);
        edtContact.setText(userContact);
        edtUserAddress.setText(userAddress);
        edtCPF.setText(cpf);
    }


    void editProfile() {
        if (!Empty()) {

            if (!isshort()) {

                SharedPreferences prefs = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(MyShardPrefernces.UserName, edtNmaeEdit.getText().toString());
                editor.putString(MyShardPrefernces.UserEmail, edtEmailEdit.getText().toString());
                editor.putString(MyShardPrefernces.UserContact, edtContEdt.getText().toString());
                editor.putString(MyShardPrefernces.UserAddressr, edtAdrresEdit.getText().toString());
                editor.putString(MyShardPrefernces.UserCpf, edtCPF2.getText().toString());
                editor.apply();
                CPF = edtCPF2.getText().toString();
                edtUserName.setText(edtNmaeEdit.getText().toString());
                edtUserEmail.setText(edtEmailEdit.getText().toString());
                edtContact.setText(edtContEdt.getText().toString());
                edtUserAddress.setText(edtAdrresEdit.getText().toString());
                edtCPF.setText(edtCPF2.getText().toString());
                layoutNoramal.setVisibility(View.VISIBLE);
                layoutEdit.setVisibility(View.GONE);

            }
        }
    }


    boolean Empty() {

        if (edtNmaeEdit.getText().toString().equals("")) {
            edtNmaeEdit.setError("Não deve estar vazio");
            return true;
        } else {
            if (edtEmailEdit.getText().toString().equals("")) {
                edtEmailEdit.setError("Não deve estar vazio");
                return true;
            }
        }


        return false;
    }

    boolean isshort() {
        if (edtNmaeEdit.getText().toString().length() < 3) {
            edtNmaeEdit.setError("não deve ter menos que 3 dígitos");
            return true;
        } else if (edtEmailEdit.getText().toString().length() < 3) {
            edtEmailEdit.setError("não deve ter menos que 3 dígitos");
            return true;
        }
        return false;
    }

    ////////Sending User Profile Update
    private void updatingProfile() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {

                                Log.d("============TOKEN ", "has been Updated");

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
                String user_id = prefs.getString("user_id", "");

                params.put("user_id", user_id);
                params.put("user_name", userName);
                params.put("user_email", userEmail);
                params.put("address", userAddress);
                params.put("phone_no", userContact);
                params.put("cpm", CPF);


                Log.i("Updating Profile", params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
