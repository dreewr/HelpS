package com.ebmacs.helpapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.Models.Medicine;
import com.ebmacs.helpapp.R;
import com.ebmacs.helpapp.Repository.Resource;
import com.ebmacs.helpapp.Repository.ResourceUtils;
import com.ebmacs.helpapp.Repository.RetrofitService;
import com.ebmacs.helpapp.Repository.RetrofitServiceFactory;
import com.ebmacs.helpapp.ViewModel.AddMedicineViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;
//import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddMedicineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    double latitude;
    double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private final String URL = "http://ebmacshost.com/help/api/upload_madicine";
    Uri uri;
    private int PICK_IMAGE_REQUEST = 1;
    EditText editTextName, editTextPresentation, editTextValidity,
            editTextFotos, editTextIndicationDays, editTextIndicationDaysHours, editTextMaufacturer,
            editTextQuantDose, editTexEANCOde, editTextDescription, editTextDose, editTextMg;
    ImageView image_calender, imagScanner, imageManufacturer;


    AddMedicineViewModel viewModel;
    Button btnShare, btnScan;

    String name, presentation, validity, encoded, ean, indicationDays,
            indicationHours, manufacturer, medType, description, dose, mg, expireDate = null;

    Bitmap fotos = null;
    IntentIntegrator qrScan;
    int quantDose = 0;

    PickSetup setup;
    String timeNow = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);



        inflateViews();
        initScanner();
        initListeners();

        viewModel = ViewModelProviders
                .of(this).get(AddMedicineViewModel.class);

        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        timeNow = dateFormat.format(date);


        if (viewModel.getMedicine().getValue()!=null)
        {
            Log.d("medicine","not null");
            updateMedicine(viewModel.medicine.getValue());
        }

        setPickSetUP();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void handleMedicineFetched(Resource<Medicine> resource){

        Log.i("teste", "teste");
        if (resource.getStatus().equals(ResourceUtils.SUCCESS)){
            editTextName.setText(resource.getData().getNome());

            Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT ).show();
        } else if (resource.getStatus().equals(ResourceUtils.LOADING)){
            Toast.makeText(this, "Carregando", Toast.LENGTH_SHORT ).show();
        }else if (resource.getStatus().equals(ResourceUtils.ERROR)){

            Toast.makeText(this, "ERRO", Toast.LENGTH_SHORT ).show();
        }

    }

    private void inflateViews(){

        editTextName = findViewById(R.id.editTextName);
        editTextPresentation = findViewById(R.id.editTextApres);
        editTextValidity = findViewById(R.id.editTextValidity);

        editTextDose = findViewById(R.id.editTextDose);
        editTextMg = findViewById(R.id.editTextMg);

        editTextFotos = findViewById(R.id.editTextFoto);
        editTextIndicationDays = findViewById(R.id.editTextIndicationdays);
        editTextIndicationDaysHours = findViewById(R.id.editTextIndicationhours);
        editTextMaufacturer = findViewById(R.id.editTextCompany);
        editTexEANCOde = findViewById(R.id.editTextEAN);

        editTextQuantDose = findViewById(R.id.editTextQuantDose);
        editTextDescription = findViewById(R.id.editTextDescription);


        image_calender = findViewById(R.id.icon_calnder);
        imagScanner = findViewById(R.id.iconScannerAdmedican);
        imageManufacturer = findViewById(R.id.icon_manufacturer);


        editTextName.setHint("*Nome do medicamento ");
        editTextPresentation.setHint("Apresentação: Apresentação(MG)(opcional) ");
        editTextValidity.setHint("*Data de validade ");
        editTextFotos.setHint("Envie fotos de toda a caixa");
        editTextMaufacturer.setHint("Fabricante/laboratório ");
        editTexEANCOde.setHint("EAN: Códigode Barra(opcional)");
        editTextDescription.setHint("Descrição: Princípio Ativo (opcional)");

        btnShare = findViewById(R.id.btnShare);
        btnScan = findViewById(R.id.btnScan);

    }

    private void initListeners(){
        image_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCalenderDialog();
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivityForResult(new Intent(AddMedicineActivity.this, ScanQrActivity.class), 233);
                qrScan.initiateScan();

                //viewModel.fetchMedicine("7896004713366");
//                getMedicine("7896004713366");
//                editTexEANCOde.setText("7896004713366");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateMedicine(Medicine medicine){

        //Atualizo o valor do viewModel
        viewModel.medicine.setValue(medicine);
        Log.i("Update Medicine", "entrou");
        editTextQuantDose.setText(medicine.getDoses());
        editTextName.setText(medicine.getNome());
        editTextMaufacturer.setText(medicine.getLaboratorio());
        editTextPresentation.setText(medicine.getApresentacao());
        editTextDescription.setText(medicine.getPrincipioAtivo());
        Picasso.get().load(medicine.getLogoUrl()).into(imageManufacturer);

    }
    private void getMedicine(String barcode){

        //Loading...
        Toast.makeText(this, "Carregando...", Toast.LENGTH_SHORT).show();
        RetrofitServiceFactory.createService(RetrofitService.class)
                .getMedicine(barcode)
                .enqueue(new Callback<List<Medicine>>() {
                    @Override
                    public void onResponse(Call<List<Medicine>> call, retrofit2.Response<List<Medicine>> response) {

                        if (response.isSuccessful()&&response.body()!= null){

                            updateMedicine(response.body().get(0));
                            Log.i("getMedicine","Sucesso");
                            //resource.setStatus(SUCCESS);

                        } else if (response.isSuccessful()&&response.body()== null){
                            Log.i("getMedicine","Vazio");
                            //resource.setStatus(EMPTY);
                        } else {
                            Log.i("getMedicine","Erro");
                            //resource.setStatus(ERROR);  //Erro inesperado
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Medicine>> call, Throwable t) {
                        Log.i("getMedicine","Erro");
                    }

                });

    }
    @SuppressLint("WrongConstant")
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
                .setButtonOrientation(LinearLayoutCompat.VERTICAL)
                .setSystemDialog(false);

    }

    public void shareButton(View view) {

        name = editTextName.getText().toString();
        presentation = editTextPresentation.getText().toString();
        validity = editTextValidity.getText().toString();
        description = editTextDescription.getText().toString();
        ean = editTexEANCOde.getText().toString();
        indicationDays = editTextIndicationDays.getText().toString();
        indicationHours = editTextIndicationDaysHours.getText().toString();
        indicationDays = editTextIndicationDays.getText().toString();
        indicationHours = editTextIndicationDaysHours.getText().toString();
        manufacturer = editTextMaufacturer.getText().toString();
        dose = editTextDose.getText().toString();
        mg = editTextMg.getText().toString();
        expireDate = editTextValidity.getText().toString();


        boolean allFilled = true;

        if (name == null || name.trim().equalsIgnoreCase("")) {

            Toast.makeText(this, "Insira o nome do medicamento", Toast.LENGTH_SHORT).show();
            editTextName.setHintTextColor(Color.parseColor("#ff0000"));
            allFilled = false;

        }

        if (validity == null || validity.trim().equalsIgnoreCase("")) {

            Toast.makeText(this, "Insira a data de validade", Toast.LENGTH_SHORT).show();
            editTextValidity.setHintTextColor(Color.parseColor("#ff0000"));
            allFilled = false;

        }

        findViewById(android.R.id.content).invalidate();

        if (allFilled == true) {

            Farmacino2 obj = new Farmacino2();
            obj.setName(name);
            obj.setApres(presentation);
            obj.setExpire(validity);
            obj.setFoto(fotos);
            obj.setDescription(description);
            obj.setIndicationDays(indicationDays);
            obj.setIndicationHours(indicationHours);
            obj.setFabricante(manufacturer);
            obj.setType(medType);
            obj.setTime(timeNow);
            obj.setQuantToTake(quantDose);
            obj.setEanCode(editTexEANCOde.getText().toString());


            MyFarmacinhaActivity.fullDetailList.add(obj);
            uploadDrugs();

            startActivity(new Intent(AddMedicineActivity.this, MyFarmacinhaActivity.class));

        }


    }

    public void initScanner(){
        qrScan = new IntentIntegrator(this).setBeepEnabled(false)
                .setPrompt("Coloque o código QR na linha vermelha para escaneá-lo")
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
              //  .setTimeout(15000);
//                .addExtra("USER_ID", userInfo.userID)
//                .addExtra("USER_SALDO", userInfo.userBalance)
//                .addExtra("USER_NAME", userInfo.userName)
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            medType = "Tablet";
            Toast.makeText(this, "Tablet", Toast.LENGTH_SHORT).show();

        }
        if (position == 1) {
            medType = "Dose";
            Toast.makeText(this, "Dose", Toast.LENGTH_SHORT).show();

        }
        if (position == 2) {
            medType = "ml";
            Toast.makeText(this, "ml", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void PicPicture(View v) {


        //     PickImageDialog.build(setup).show(AddMedicineActivity.this);

        CropImage.activity()
                .setCropMenuCropButtonTitle("Cortar").setAllowFlipping(false)
                .start(this);
    }


    void showCalenderDialog() {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddMedicineActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {

                Calendar date = Calendar.getInstance();

                date.set(Calendar.YEAR, i);
                date.set(Calendar.MONTH, i1);
                date.set(Calendar.DAY_OF_MONTH, i2);


                String myFormat = "MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editTextValidity.setText(sdf.format(date.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText("Cancelar");
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setText("Está bem");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if ( scanResult != null){
            String barcode = scanResult.getContents();
            if (barcode != null && !"".equals(barcode))
            {
                Log.i("Barcode", barcode);
                getMedicine(barcode);
                editTexEANCOde.setText(barcode);
            }

        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0xC0DE) {
                Log.i("","");

                //Chama o viewModel
                //String message = data.getStringExtra("code");
                //editTexEANCOde.setText(message);
            }

            if (requestCode == 233) {
                String message = data.getStringExtra("code");
                editTexEANCOde.setText(message);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        fotos = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        Log.i("PHOTTTTTT", String.valueOf(fotos));

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        fotos.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        Log.i("Photo in base 64", String.valueOf(encoded));


                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        fotos.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        uri = MediaStore.Images.Media.getContentUri("internal");
                        Log.i("PHOTOOO", String.valueOf(fotos));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }

    }

    ////////Upload Drugs
    private void uploadDrugs() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.wtf("Register Response", response);

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {


                                Log.i("DrugsUploaded", "Drugs has been uploaded");


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
                params.put("userfile", encoded);
                Log.i("params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}