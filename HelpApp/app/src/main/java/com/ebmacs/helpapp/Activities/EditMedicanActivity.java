package com.ebmacs.helpapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditMedicanActivity extends AppCompatActivity implements Validator.ValidationListener {

    private final String URL = "http://ebmacshost.com/help/api/edit_medicine/edit";
    double latitude;
    double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    String name, description, expireDate, ean, indicationDays, indicationHours, manufacturer, dose, madicine_id, mg;


    @NotEmpty
    EditText editTextName, editTextValidity;

    EditText editTextActiveP, editDescription, editTextDose,
            editTextFotos, editTextIndicationDays, edtTextIndcationHours, editTexEANCOde, editTextQuantDose, editTextMaufacturer, editTextMg;
    Button btnShare;
    ImageView imageCamera, image_calender;
    int quantDose = 0;
    boolean clicked = false;
    ImageView image_camera, imageScanner;
    Validator validator;
    PickSetup setup;

    Bitmap bitmap;

    Farmacino2 editedMEdican = new Farmacino2();

    public static int SelectedMedican = 0;

    public static Farmacino2 medicanDetail = new Farmacino2();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medican);


        setPickSetUP();
        itIT();
        setListeners();
        updateLayout();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.i("Lati", String.valueOf(latitude));
                            Log.i("Longi", String.valueOf(longitude));

                            // Logic to handle location object
                        }
                    }
                });


    }

    void itIT() {
        editTextName = findViewById(R.id.editTextName);
        editTextValidity = findViewById(R.id.editTextValidity);
        editTextActiveP = findViewById(R.id.editTextActivePr);
        editTextFotos = findViewById(R.id.editTextFoto);
        editTextIndicationDays = findViewById(R.id.editTextIndicationdays);
        edtTextIndcationHours = findViewById(R.id.editTextIndicationhours);
        editTextMaufacturer = findViewById(R.id.editTextCompany);
        editTexEANCOde = findViewById(R.id.editTextEAN);
        editTextQuantDose = findViewById(R.id.editTextQuantDose);
        editDescription = findViewById(R.id.editTextDescription);
        editTextDose = findViewById(R.id.editTextDose);
        editTextMg = findViewById(R.id.editTextMg);

        //  myspinner = findViewById(R.id.spinner);
        //   myspinner.setOnItemSelectedListener(this);
        imageCamera = findViewById(R.id.image_came);
        image_calender = findViewById(R.id.icon_calnder);
        btnShare = findViewById(R.id.btnShare);
        imageScanner = findViewById(R.id.iconScannerAdmedican);
        madicine_id = getIntent().getStringExtra("madicine_id");
    }

    void setListeners() {


        image_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCalenderDialog();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator = new Validator(EditMedicanActivity.this);
                validator.setValidationListener(EditMedicanActivity.this);
                validator.validate();
                name = editTextName.getText().toString();
                description = editDescription.getText().toString();
                expireDate = editTextValidity.getText().toString();
                ean = editTexEANCOde.getText().toString();
                indicationDays = editTextIndicationDays.getText().toString();
                indicationHours = edtTextIndcationHours.getText().toString();
                dose = editTextDose.getText().toString();
                mg = editTextMg.getText().toString();
                manufacturer = editTextMaufacturer.getText().toString();

                editMedicine();

            }
        });

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  PickImageDialog.build(setup).show(EditMedicanActivity.this);

                CropImage.activity()
                        .setCropMenuCropButtonTitle("Cortar").setAllowFlipping(false)
                        .start(EditMedicanActivity.this);
            }
        });

        imageScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(EditMedicanActivity.this, ScanQrActivity.class), 234);

            }
        });

    }


    private void setPickSetUP() {

        setup = new PickSetup()
                .setTitle("Escolher")
                .setTitleColor(getResources().getColor(R.color.colorPrimary))
                .setCancelText("Não")
                .setCancelTextColor(getResources().getColor(R.color.colorPrimary))
                .setFlip(false)
                .setMaxSize(500)
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setCameraButtonText("Câmera")
                .setGalleryButtonText("Galeria")
                .setIconGravity(Gravity.LEFT)
                .setButtonOrientation(LinearLayout.VERTICAL)
                .setSystemDialog(false);

    }


    void showCalenderDialog() {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditMedicanActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {

                Calendar date = Calendar.getInstance();

                date.set(Calendar.YEAR, i);
                date.set(Calendar.MONTH, i1);
                date.set(Calendar.DAY_OF_MONTH, i2);


                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editTextValidity.setText(sdf.format(date.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText("Cancelar");
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setText("Está bem");


    }


    void updateLayout() {

        editTextName.setText(medicanDetail.getName());

        editTextValidity.setText(medicanDetail.getExpire());
        editTextActiveP.setText(medicanDetail.getApres());
        // editTextFotos.setText(medicanDetail.get);
        editTextIndicationDays.setText(medicanDetail.getIndicationDays());
        edtTextIndcationHours.setText(medicanDetail.getIndicationHours());
        editTextMaufacturer.setText(medicanDetail.getFabricante());
        editTexEANCOde.setText(medicanDetail.getEanCode());
        editTextQuantDose.setText(String.valueOf(medicanDetail.getQuantToTake()));
        editDescription.setText(medicanDetail.getDescription());

        bitmap = medicanDetail.getFoto();


        //  setSpinnerSelection();


    }

    void editMEdican() {


        editedMEdican.setName(editTextName.getText().toString());

        editedMEdican.setExpire(editTextValidity.getText().toString());
        editedMEdican.setIndicationDays(editTextIndicationDays.getText().toString());
        editedMEdican.setIndicationHours(edtTextIndcationHours.getText().toString());
        editedMEdican.setFabricante(editTextMaufacturer.getText().toString());
        editedMEdican.setEanCode(editTexEANCOde.getText().toString());

        editedMEdican.setQuantToTake(Integer.parseInt(editTextQuantDose.getText().toString()));
        // editedMEdican.setType(myspinner.getSelectedItem().toString());
        editedMEdican.setDescription(editDescription.getText().toString());
        editedMEdican.setTime(medicanDetail.getTime());


        ArrayList<Farmacino2> medicanList = MyFarmacinhaActivity.fullDetailList;
        medicanList.remove(SelectedMedican);
        medicanList.add(SelectedMedican, editedMEdican);

        MyFarmacinhaActivity.fullDetailList = medicanList;
        finish();

    }

    @Override
    public void onValidationSucceeded() {

        editMEdican();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 234) {
                String message = data.getStringExtra("code");
                editTexEANCOde.setText(message);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    editedMEdican.setFoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    ////////editMedicine
    private void editMedicine() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.wtf("Register Response", response);

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {


                                Log.i("Drugs", "Drugs has been Edited");


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


                params.put("madicine_name", name);
                params.put("madicine_description", description);
                params.put("expire_date", expireDate);
                params.put("bar_code", ean);
                params.put("mg", mg);
                params.put("Posology_day", indicationDays);
                params.put("Posology_day_horse", indicationHours);
                params.put("labortory_name", manufacturer);
                params.put("amount_of_dose", dose);
                params.put("user_id", user_id);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("userfile", String.valueOf(bitmap));
                params.put("madicine_id", madicine_id);
                Log.i("params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
