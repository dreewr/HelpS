package com.ebmacs.helpapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.OnSwipeTouchListener;
import com.ebmacs.helpapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShowMedicanDetailActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String URL = "http://ebmacshost.com/help/api/delete_medicine";


    private ArrayList<Integer> al;
    private com.ebmacs.helpapp.Adapters.SearchAdapter arrayAdapter;
    private int i;
    LinearLayout midLayout;
    ImageView itemPic, drugImage;
    Button btnInSearch;
    BottomNavigationView bottomNavigationView;

    CircleImageView iconPerson, imageSHare;

    Bitmap bitmap;
    String sharingTextWithImage = "";
    String ImageURL, madicine_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        midLayout = findViewById(R.id.midLayout);

        itemPic = findViewById(R.id.imgPictureSearch);
        btnInSearch = findViewById(R.id.buttonInSearch);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        iconPerson = findViewById(R.id.icon_person);
        iconPerson.setColorFilter(getResources().getColor(R.color.colorPrimary));
        ImageURL = getIntent().getStringExtra("MainImageURL");
        madicine_id = getIntent().getStringExtra("madicine_id");


        Picasso.get()
                .load(ImageURL)
                .into(itemPic);


//        imageSHare = findViewById(R.id.btn_share_medican);


        iconPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowMedicanDetailActivity.this, ProfileActivity.class));
                finish();
            }
        });
//        imageSHare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(MyFarmacinhaActivity.selectedFarma.foto == null){
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowMedicanDetailActivity.this);
//                    final   AlertDialog dialog;
//                    builder.setView(R.layout.dialog_befor_share_pic);
//                    dialog = builder.create();
//
//                    dialog.show();
//                    TextView btnOK = dialog.findViewById(R.id.btn_ok_dlg_discard);
//
//                    btnOK.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            dialog.dismiss();
//
//                        }
//                    });
//
//
//                }else{
//                    shareMedican();
//                }
//
//            }
//        });


        al = new ArrayList<>();
        al.add(R.drawable.medicine);
        al.add(R.drawable.medicine2);
        al.add(R.drawable.medicine3);
        al.add(R.drawable.medicine4);
        al.add(R.drawable.medicine);
        al.add(R.drawable.medicine3);
        al.add(R.drawable.medicine2);
        al.add(R.drawable.medicine4);


        Bundle extras = getIntent().getExtras();
        String itemName = extras.getString("itemName");
        String itemExpire = extras.getString("itemExpire");
        String itemTime = extras.getString("itemTime");
        //  byte[] itemPicture=extras.getByteArray("itemPicture");
        //  Bitmap bitmap = (Bitmap) extras.getParcelable("itemPicture");


        midLayout.setOnTouchListener(new OnSwipeTouchListener(ShowMedicanDetailActivity.this) {
            public void onSwipeTop() {
                //  Toast.makeText(ShowMedicanDetailActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                //  Toast.makeText(ShowMedicanDetailActivity.this, "right", Toast.LENGTH_SHORT).show();
                //    Intent intent = new Intent(ShowMedicanDetailActivity.this,MedicineOwnerActivity.class);
                ///    startActivity(intent);
            }

            public void onSwipeLeft() {
                //  Toast.makeText(ShowMedicanDetailActivity.this, "left", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ShowMedicanDetailActivity.this,ChatActivity.class);
//                startActivity(intent);

            }

            public void onSwipeBottom() {
                //  Toast.makeText(ShowMedicanDetailActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        //  Toast.makeText(this,"Name : "+itemName,Toast.LENGTH_LONG).show();


    }


    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.editar_nav) {
            Intent intent = new Intent(ShowMedicanDetailActivity.this, EditMedicanActivity.class);
            intent.putExtra("madicine_id", madicine_id);
            startActivity(intent);

            finish();
        } else if (id == R.id.compartilhr_nav) {

            startActivity(new Intent(ShowMedicanDetailActivity.this, AddMedicineActivity.class));

        } else if (id == R.id.discartar_nav) {

            showDiscardPopUp();

            //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        }
        return true;
    }


    void deletMedican() {

        ArrayList<Farmacino2> medicanlist = MyFarmacinhaActivity.fullDetailList;
        medicanlist.remove(EditMedicanActivity.SelectedMedican);
        MyFarmacinhaActivity.fullDetailList = medicanlist;


        Toast toast = Toast.makeText(ShowMedicanDetailActivity.this, "Apagado com sucesso", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        finish();


    }


    void showDiscardPopUp() {
        PopupMenu popup = new PopupMenu(ShowMedicanDetailActivity.this, findViewById(R.id.discartar_nav));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.pop_up_discard, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_disrdPop) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowMedicanDetailActivity.this);
                    final AlertDialog dialog;
                    builder.setView(R.layout.dilaog_discard_medican);
                    dialog = builder.create();

                    dialog.show();
                    TextView btnOK = dialog.findViewById(R.id.btn_ok_dlg_discard);
                    TextView btnCancl = dialog.findViewById(R.id.btn_cancl_dlg_discard);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ShowMedicanDetailActivity.this, MyFarmacinhaActivity.class);
                            startActivity(intent);
                            deleteMedicine();
                            dialog.dismiss();

                        }
                    });
                    btnCancl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                }

                if (item.getItemId() == R.id.btn_consultPop) {

                    String url = "http://www.ecycle.com.br/postos/reciclagem.php";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }


    void shareMedican() {

        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, "com.ebmacs.helpapp.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Medicamento para Compartilhar \n" + MyFarmacinhaActivity.selectedFarma.getName() + ": \n" + MyFarmacinhaActivity.selectedFarma.getDescription());
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }


    ////////deleteMedicine
    private void deleteMedicine() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.wtf("Register Response", response);

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {


                                Log.i("Drugs", "has been Deleted");


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
                SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "");

                params.put("user_id", user_id);
                params.put("madicine_id", madicine_id);
                Log.i("params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
