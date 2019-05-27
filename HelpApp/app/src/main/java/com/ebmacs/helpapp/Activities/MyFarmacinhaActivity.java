package com.ebmacs.helpapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Services.MyFirebaseInstanceService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ebmacs.helpapp.Adapters.FarmacinoAdapter;
import com.ebmacs.helpapp.Adapters.ListPopupWindowAdapter;
import com.ebmacs.helpapp.BuildConfig;
import com.ebmacs.helpapp.Chat.InboxActivity;
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.GridSpacingItemDecoration;
import com.ebmacs.helpapp.MyShardPrefernces;
import com.ebmacs.helpapp.ProfileDetailsActivity;
import com.ebmacs.helpapp.R;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;


public class MyFarmacinhaActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    int PLAY_SERVICES_RESOLUTION_REQUEST;

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    com.ebmacs.helpapp.Adapters.FarmacinoAdapter adapter;
    public static ArrayList<Farmacino2> fullDetailList = new ArrayList<>();

    ImageView imgHelp, imgSearch, iconMenu, imgSORT, imgListGrid;
    LinearLayout layoutMenu;
    EditText searchBar;
    View view;

    private FusedLocationProviderClient fusedLocationClient;
    public static double latitude;
    public static double longitude;
    private final String URL = "http://ebmacshost.com/help/api/getMadicine";
    private static final String token_URL = "http://ebmacshost.com/help/api/add_token";


    static PopupWindow popupWindow;
    static LinearLayout myFarmichaLayout;
    static LayoutInflater layoutInflater;

    public static boolean somethingEmpty = false;
    public static Farmacino2 selectedFarma = null;

    int checkSort = 0;
    int checkList = 0;
    public static boolean checkPopUp = false;


    RapidFloatingActionContentLabelList rfaContent;


    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private static RelativeLayout back_dim_layout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfarmacinha);
        searchBar = findViewById(R.id.editTextsearchBar);


        initializeViews();
        setRecyclerView();
        setClickListeners();
        checkFirstRun();
        fetchDrugs();
        sendingFCMtoken();



//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//
//            return;
//        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            Log.i("Nearby Lati", String.valueOf(latitude));
//                            Log.i("Nearby Longi", String.valueOf(longitude));
//
//                            // Logic to handle location object
//                        }
//                    }
//                });
//
//
//    }
    }



    @Override
    protected void onResume() {

        //  setRecyclerView();
        super.onResume();
    }

    private void initializeViews() {

        // floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        rfaContent = new RapidFloatingActionContentLabelList(MyFarmacinhaActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(MyFarmacinhaActivity.this);

        rfaLayout = findViewById(R.id.activity_main_rfal);
        rfaBtn = findViewById(R.id.activity_main_rfab);
        iconMenu = findViewById(R.id.iconMenu);

        layoutMenu = findViewById(R.id.layoutMenu);
        imgHelp = findViewById(R.id.imgHelp);
        imgSearch = findViewById(R.id.imgSearch);

        myFarmichaLayout = findViewById(R.id.myFarmichaLayout);
        layoutInflater = (LayoutInflater) MyFarmacinhaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        imgSORT = findViewById(R.id.imgSort);
        imgListGrid = findViewById(R.id.imgListGrid);


    }

    private void initializeViewsList() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


    }

    private void initializeViewsGrid() {

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        int spanCount = 2; // 2 columns
        int spacing = 1; // 20px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }


    private void setClickListeners() {
        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(searchBar.getText());
            }
        });
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                adapter.getFilter().filter(searchBar.getText());
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter(searchBar.getText());
                //  startActivity(new Intent(MyFarmacinhaActivity.this, ShowMedicanDetailActivity.class));
            }
        });

        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Meu perfil", "Minha pontuação", "Mensagens", "Grupos", "Adicionar medicamentos", "Medicamento mais próximo", "Tutoriais"));
                showListMenu(iconMenu, arrayList);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFarmacinhaActivity.super.onBackPressed();
            }
        });

        imgSORT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSort == 0) {
                    setRecyclerViewAsc();
                    checkSort = 1;
                    imgSORT.setImageResource(R.drawable.sortdesc);
                } else if (checkSort == 1) {
                    setRecyclerViewDesc();
                    checkSort = 2;
                    imgSORT.setImageResource(R.drawable.sortasc);
                } else if (checkSort == 2) {
                    setRecyclerViewAsc();
                    checkSort = 1;
                    imgSORT.setImageResource(R.drawable.sortdesc);
                }

            }
        });

        imgListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkList == 0) {
                    initializeViewsGrid();
                    setRecyclerView();
                    checkList = 1;
                    imgListGrid.setImageResource(R.drawable.grid);
                } else if (checkList == 1) {
                    initializeViewsList();
                    setRecyclerView();
                    checkList = 0;
                    imgListGrid.setImageResource(R.drawable.list);
                }
            }
        });
    }


    ////////Fetching Data
    private void fetchDrugs() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {
                                JSONArray array = jsonObject.getJSONArray("madincine");
                                fullDetailList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    Farmacino2 result = new Farmacino2();
                                    result.setName(object.getString("madicine_name"));
                                    result.setDescription(object.getString("madicine_description"));
                                    result.setExpire(object.getString("expire_date"));
                                    result.setMadicinId(object.getString("madicine_id"));
                                    String url = object.getString("image_url");
                                    String inserted_date = object.getString("inserted_date");

                                    // splitting Date and time
                                    String[] parts = inserted_date.split(" ");
                                    String date = parts[0]; // 004
                                    String time = parts[1];
                                    result.setTime(time);


                                    result.setImageUrl(url);
                                    fullDetailList.add(result);
                                    Log.i("DrugImage", url);
                                    Log.i("Splited Time", time);

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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void showListMenu(View anchor, ArrayList<String> arrayList) {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        final List<String> array = (List) arrayList;

        final ListAdapter adapter = new ListPopupWindowAdapter(this, array);

        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(400);


        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    startActivity(new Intent(MyFarmacinhaActivity.this, MyScoreActivity.class));
                } else if (i == 2) {
                    startActivity(new Intent(MyFarmacinhaActivity.this, InboxActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(MyFarmacinhaActivity.this, FriendsActivity.class));
                } else if (i == 4) {
                    startActivity(new Intent(MyFarmacinhaActivity.this, AddMedicineActivity.class));
                } else if (i == 5) {
                    startActivity(new Intent(MyFarmacinhaActivity.this, NearbyDrugsAcitivity.class));
                } else if (i == 6) {
                    MainActivity.contextFrom = "MyFarma";
                    startActivity(new Intent(MyFarmacinhaActivity.this, MainActivity.class));
                } else if (i == 0) {

                    startActivity(new Intent(MyFarmacinhaActivity.this, ProfileDetailsActivity.class));
                }

                popupWindow.dismiss();

            }
        });
        popupWindow.show();
    }

    private void setRecyclerView() {

        //  Collections.sort(fullDetailList, Farmacino2.ascending_comparater);
        adapter = new FarmacinoAdapter(this, fullDetailList);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerViewAsc() {

        Collections.sort(fullDetailList, Farmacino2.ascending_comparater);
        adapter = new FarmacinoAdapter(this, fullDetailList);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerViewDesc() {

        Collections.sort(fullDetailList, Farmacino2.descending_comparater);
        adapter = new FarmacinoAdapter(this, fullDetailList);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if (position == 0) {
            rfaLayout.collapseContent();
            startActivity(new Intent(MyFarmacinhaActivity.this, AddMedicineActivity.class));
        } else {
            startActivity(new Intent(MyFarmacinhaActivity.this, NearbyDrugsAcitivity.class));
            rfaLayout.collapseContent();
        }

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position == 0) {
            rfaLayout.collapseContent();
            startActivity(new Intent(MyFarmacinhaActivity.this, AddMedicineActivity.class));
        } else {
            startActivity(new Intent(MyFarmacinhaActivity.this, NearbyDrugsAcitivity.class));
            rfaLayout.collapseContent();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        setFloatingButton();
        setRecyclerView();

    }


    void setFloatingButton() {
        List<RFACLabelItem> items = new ArrayList<>();
        if (fullDetailList.size() == 0) {
            items.add(new RFACLabelItem<Integer>()
                    .setLabel("Adicionar medicamento")
                    .setResId(R.drawable.add_medicen)
                    .setIconNormalColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setIconPressedColor(0xffbf360c)
                    .setLabelColor(Color.BLACK)
                    .setWrapper(0)
            );
        } else {
            items.add(new RFACLabelItem<Integer>()
                    .setLabel("Adicionar medicamento")
                    .setResId(R.drawable.add_medicen)
                    .setIconNormalColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setIconPressedColor(0xffbf360c)
                    .setLabelColor(Color.BLACK)
                    .setWrapper(0)
            );

            items.add(new RFACLabelItem<Integer>()
                    .setLabel("Buscar medicamentos")
                    .setResId(R.drawable.add_medicen)
                    .setIconNormalColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setIconPressedColor(0xff3e2723)
                    .setLabelColor(Color.BLACK)
                    .setLabelSizeSp(14)
                    // .setLabelBackgroundDrawable()
                    .setWrapper(1)
            );

        }

        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                MyFarmacinhaActivity.this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();


    }

    void checkForTutorials() {

        if (somethingEmpty) {
            MainActivity.contextFrom = "profile";
            startActivity(new Intent(this, com.ebmacs.helpapp.Activities.MainActivity.class));
            somethingEmpty = false;
        } else {

            SharedPreferences myPrefernce = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);
            String lectureViewed = myPrefernce.getString(MyShardPrefernces.lectureViewd, "");
            if (!lectureViewed.equals("1")) {

                startActivity(new Intent(this, com.ebmacs.helpapp.Activities.MainActivity.class));

            }


        }

    }

    public static void popup() {
        //instantiate the popup.xml layout file

        // back_dim_layout.setVisibility(View.VISIBLE);
        View customView = layoutInflater.inflate(R.layout.popup, null);

        Button closePopupBtn = (Button) customView.findViewById(R.id.btnClosePopup);

        //instantiate popup window
        popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //display the popup window
        popupWindow.showAtLocation(myFarmichaLayout, Gravity.CENTER, 0, 0);


        //close the popup window on button click
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //  back_dim_layout.setVisibility(View.GONE);
            }

        });

    }


    // this method will check first installation/upgrade and show tutorial only once
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)


            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    checkForTutorials();
                }

            }.start();
        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade

            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    checkForTutorials();
                }

            }.start();
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }


    ////////Sending fcm Token to server
    private void sendingFCMtoken() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, token_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
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
                SharedPreferences preferences = getSharedPreferences("REFRESHED_TOKEN", MODE_PRIVATE);
                String mToken =preferences.getString("mToken", "");

                params.put("user_id", user_id);
                params.put("token_id", mToken);
                Log.i("-----Service call",params.toString());
                Log.i("-----Service call token",mToken);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}


