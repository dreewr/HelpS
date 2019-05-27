package com.ebmacs.helpapp.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.GeneralHelper;
import com.ebmacs.helpapp.MyShardPrefernces;
import com.ebmacs.helpapp.R;
import com.ebmacs.helpapp.User.LoginActivity;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements IPickResult, Spinner.OnItemSelectedListener {

    private static final String URL = "http://ebmacshost.com/help/api/update_profile/add_cpm";
    private static final String URL_CELL = "http://ebmacshost.com/help/api/update_profile/add_cellular";


    ImageView profile_pic;
    TextView profName;
    String personName = "David";
    String personEmail;

    String personPhoto = null;
    Button btnLogOut;
    Bitmap bitmap;
    String responseCode = "";
    String user_id = "";
    ProgressDialog progDialog;
    String image_url = "";

    String medicanRedius = "";
    String cellNo = "";
    String cpfno = "";

    Button btnMyLocation, btnMedicaianRedius, btnCell, btnCPF;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);

        personName = prefs.getString(MyShardPrefernces.UserName, null);//"No name defined" is the default value.
        personEmail = prefs.getString(MyShardPrefernces.UserEmail, null);
        personPhoto = prefs.getString(MyShardPrefernces.UserImageUrl, null);


        SharedPreferences preferences = getSharedPreferences("REFRESHED_TOKEN", MODE_PRIVATE);
        String mToken = preferences.getString("mToken", null);
        if (mToken != null) {
            Log.i("OkKAY THIS IS TOKEN", mToken);
        } else {
            Log.i("OkKAY THIS IS TOKEN", "Token is null");

        }


        inIT();
        setListeners();


        if (personPhoto != null) {
            Picasso.get().load(personPhoto).into(profile_pic);

        }
        profName.setText(personName);


    }

    void inIT() {
        btnLogOut = findViewById(R.id.btnLogOut);
        profile_pic = findViewById(R.id.profile_pic);
        profName = findViewById(R.id.prof_Name);

        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnMedicaianRedius = findViewById(R.id.btnMedicianRedius);
        btnCell = findViewById(R.id.btnaddCell);
        btnCPF = findViewById(R.id.btnAddCpf);


    }

    void setListeners() {

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pickImage();
            }
        });

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!hasPermissions(ProfileActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ProfileActivity.this, PERMISSIONS, PERMISSION_ALL);
                } else {


                    Intent i = new Intent(ProfileActivity.this, MapsActivity.class);
                    startActivityForResult(i, 123);


                }

            }
        });

        btnMedicaianRedius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_drug_search);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_dilog_radius);
                dialog.setTitle("Select Search Radius");
                dialog.show();

                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

                // Spinner click listener
                spinner.setOnItemSelectedListener(ProfileActivity.this);
                final List<String> categories = new ArrayList<String>();
                categories.add("5 Km");
                categories.add("10 Km");
                categories.add("15 Km");
                categories.add("20 Km");
                categories.add("30 Km");
                categories.add("40 Km");
                categories.add("50 Km");
                categories.add("100 Km");
                categories.add("500 Km");


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        medicanRedius = categories.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        btnCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_cell);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_dilog_radius);
                dialog.show();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText celtEdit = dialog.findViewById(R.id.edtCellPrfDilg);
                        cellNo = celtEdit.getText().toString();
                        SharedPreferences.Editor editor = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE).edit();
                        editor.putString(MyShardPrefernces.UserContact, cellNo);
                        editor.apply();
                        updatingCellular();
                        dialog.dismiss();
                    }
                });
            }
        });
        btnCPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_cpf);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_dilog_radius);
                dialog.show();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText cpF = dialog.findViewById(R.id.edtCpfPrfDilg);
                        cpfno = cpF.getText().toString();
                        SharedPreferences.Editor editor = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE).edit();
                        editor.putString(MyShardPrefernces.UserCpf, cpfno);
                        editor.apply();
                        Log.i("CPF", cpfno);
                        updatingCPF();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {

            String user_lat = data.getStringExtra("user_lat");
            String user_lng = data.getStringExtra("user_lng");
            //  Toast.makeText(this, user_lat+" "+user_lng, Toast.LENGTH_SHORT).show();

        }
    }


    void logOutUser() {

        SharedPreferences sharedPreferences = this.getSharedPreferences(MyShardPrefernces.mPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyShardPrefernces.UserLoginStatus, "0");
        editor.commit();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }


//    private void pickImage() {
//
//        PickImageDialog.build().show(ProfileActivity.this);
//
//
//    }


    public void nextActivity(View view) {


        if (cellNo.equals("") || cpfno.equals("") || medicanRedius.equals("")) {

            MyFarmacinhaActivity.somethingEmpty = true;


        }

        startActivity(new Intent(ProfileActivity.this, MyFarmacinhaActivity.class));
    }

    public void onBackPressed() {
        //  Toast.makeText(this,"Back Pressed", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle("Realmente sair?")
                .setMessage("Você tem certeza que quer sair?")
                .setNegativeButton("CANCELAR", null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProfileActivity.super.onBackPressed();
                        quit();
                    }
                }).create().show();

    }


    public void quit() {
//        Intent start = new Intent(Intent.ACTION_MAIN);
//        start.addCategory(Intent.CATEGORY_HOME);
//        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(start);
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity(); // or finish();
        }
        // System.exit(0);
    }


    @Override
    public void onPickResult(PickResult pickResult) {

        bitmap = pickResult.getBitmap();
        GeneralHelper generalHelper = new GeneralHelper(ProfileActivity.this);
        profile_pic.setImageBitmap(bitmap);

        String userImage = generalHelper.conVertImage64(bitmap);
        uploadImage(userImage);


    }

    void uploadImage(final String userImage) {

        String url = getResources().getString(R.string.website_url1) + "update_profile_photo";
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progDialog.cancel();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.v("image_response", response);
                    responseCode = jsonObject.getString("sucess");
                    if (responseCode.equals("1")) {

                        //  String image_url = jsonObject.getString("image_path");
                        //  image_url = jsonObject.getString("image_url");
                        //   Glide.with(getContext()).load(image_url).apply(RequestOptions.placeholderOf(R.drawable.profile_image).error(R.drawable.profile_image)).into(profileImageVw);

                        SharedPreferences preferences = getSharedPreferences(MyShardPrefernces.mPreferenceName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEditor = preferences.edit();
                        myEditor.putString("image_url", image_url);
                        myEditor.commit();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progDialog.cancel();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("image", userImage);
                map.put("user_id", user_id);
                //     map.put("api_key",getResources().getString(R.string.api_key));
                return map;
            }
        };

        request.setShouldCache(false);
        queue.add(request);
        progDialog = ProgressDialog.show(ProfileActivity.this, null, null, false, false);
        progDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progDialog.setContentView(R.layout.progress_style);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void updatingCPF() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {

                                Log.d("CPF", "has been Updated");

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
                params.put("cpm", cpfno);


                Log.i("Updating Profile", params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updatingCellular() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CELL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {

                                Log.d("cellular", "has been Updated");

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
                params.put("cellular", cellNo);
                Log.i("Updating Profile", params.toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
