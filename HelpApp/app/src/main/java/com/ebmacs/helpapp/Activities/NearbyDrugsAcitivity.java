package com.ebmacs.helpapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Adapters.NearbyDrugsAdapter;
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.Models.NearbyDrugs;
import com.ebmacs.helpapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NearbyDrugsAcitivity extends AppCompatActivity {


    private final String URL = "http://ebmacshost.com/help/api/findnearby";


    RecyclerView recyclerViewDrugs;
    EditText searchBar;

    NearbyDrugsAdapter adapter;
    ImageView imgSearch;
    ArrayList<NearbyDrugs> arrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_drugs_acitivity);

        inIt();
        setListeners();

        nearbyDrugs();



    }

    void inIt() {

        recyclerViewDrugs = findViewById(R.id.recycleNearbyDrugs);
        recyclerViewDrugs.setLayoutManager(new LinearLayoutManager(this));
        searchBar = findViewById(R.id.editTextsearchBar);
        imgSearch = findViewById(R.id.imgSearch);


    }

    void setListeners() {

        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(searchBar.getText());
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter(searchBar.getText());
                //  startActivity(new Intent(MyFarmacinhaActivity.this, ShowMedicanDetailActivity.class));
            }
        });

    }


    ////////Upload Drugs
    private void nearbyDrugs() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                arrayList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    NearbyDrugs result = new NearbyDrugs();
                                    result.setName(object.getString("madicine_name"));
                                    result.setDescription(object.getString("madicine_description"));
                                    result.setExpire(object.getString("expire_date"));
                                    result.setDistance(object.getString("distance"));
                                    String url = object.getString("image_url");
                                    String inserted_date = object.getString("inserted_date");


                                    // splitting Date and time
                                    String[] parts = inserted_date.split(" ");
                                    String date = parts[0]; // 004
                                    String time = parts[1];
                                    result.setTime(time);

                                    result.setImageUrl(url);
                                    arrayList.add(result);
                                    adapter = new NearbyDrugsAdapter(NearbyDrugsAcitivity.this, arrayList);
                                    recyclerViewDrugs.setAdapter(adapter);
                                    Log.i("DrugImage", url);
                                    Log.i("Time", time);
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

                params.put("lat", String.valueOf(MyFarmacinhaActivity.latitude));
                params.put("lng", String.valueOf(MyFarmacinhaActivity.longitude));
                params.put("distance", String.valueOf(30));
                Log.i("Nearby params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
