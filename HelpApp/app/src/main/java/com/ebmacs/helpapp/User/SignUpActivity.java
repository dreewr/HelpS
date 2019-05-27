package com.ebmacs.helpapp.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.GeneralHelper;
import com.ebmacs.helpapp.MyShardPrefernces;
import com.ebmacs.helpapp.Activities.ProfileActivity;
import com.ebmacs.helpapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {


    @NotEmpty(message = "Please fill this field")
    @Length(max = 12, min = 2, message = "Input must be between 2 and 12 characters")
    EditText editFistName,editLastName;
    @NotEmpty(message = "Please fill this Email")
    @Email
    EditText editEmail;
    @NotEmpty(message = "Please enter Contact")
    @Length(max = 12, min = 7, message = "Input must be between 7 and 12 characters")
    EditText editContact;
    @NotEmpty(message = "Please provide password")
    @Password(min = 4, scheme = Password.Scheme.ANY)
    EditText editPass;
    @ConfirmPassword
    @NotEmpty(message = "please provide confirm password")
    EditText editConfirmPass;

    String responseCode = "";
    String user_id;
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    String url ="";


    Button btnSignUp;
    com.mobsandgeeks.saripaar.Validator validator;

    ProgressDialog progDialog;

    GeneralHelper generalHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        generalHelper = new GeneralHelper(this);
        validator = new com.mobsandgeeks.saripaar.Validator(this);
        validator.setValidationListener(this);
        intializeViews();
        setClickListeners();


    }


    private void intializeViews() {


        editFistName = findViewById(R.id.edtFNameUser);
        editLastName = findViewById(R.id.edtLNameUser);
        editEmail = findViewById(R.id.edtEmailUser);
        editContact = findViewById(R.id.edtPhoneUser);
        editPass = findViewById(R.id.edtPasswordUser);
        editConfirmPass = findViewById(R.id.edtConfirmPasswordUser);
        btnSignUp = findViewById(R.id.btnLoginUser);

    }
    private void setClickListeners() {

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                validator.validate();
                //      userLatLong = generalHelper.getLatLong();

            }

        });

    }



    @Override
    public void onValidationSucceeded() {

        if(generalHelper.internetNotAvailable()){
            generalHelper.switchOnInternet();
        }
        else {

            signUpUser();

        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {


        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }





    void signUpUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        url = getResources().getString(R.string.website_url1)+"register";
        // String url1 = "https://ebmacshost.com/services_app_api/register.php";
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progDialog.cancel();
                    JSONObject jsonObject = new JSONObject(response);

                    responseCode = jsonObject.getString("sucess");

                    if(responseCode.equals("409")){
                        editEmail.setError("User already exist");
                    }
                    else
                    if(responseCode.equals("1")){
                        JSONObject jsonArray = new JSONObject(jsonObject.getString("user_detail"));
                        user_id = jsonArray.getString("user_id");
                        myPreferences = getSharedPreferences(MyShardPrefernces.mPreferenceName, Context.MODE_PRIVATE);
                        myEditor = myPreferences.edit();
                        myEditor.putString(MyShardPrefernces.UserEmail,editEmail.getText().toString());
                        myEditor.putString(MyShardPrefernces.UserID,user_id);
                        myEditor.putString(MyShardPrefernces.UserContact,jsonArray.getString("phone_no"));
                        myEditor.putString(MyShardPrefernces.UserName,jsonArray.getString("user_name"));
                        myEditor.putString(MyShardPrefernces.UserImageUrl,jsonArray.getString("image_url"));
                        myEditor.putString(MyShardPrefernces.UserLoginStatus,"1");

                        myEditor.commit();



                        Intent intent = new Intent(SignUpActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        finish();


                    }
                    else {
                        if(responseCode.equals("0")){
                            editEmail.setError("User already exist");
                        }
                        else{
                            Toast toast = Toast.makeText(SignUpActivity.this,"Ooops some unexpected behavoir occured",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("error",error.getMessage());
                progDialog.cancel();

                myPreferences = getSharedPreferences(MyShardPrefernces.mPreferenceName, Context.MODE_PRIVATE);
                myEditor = myPreferences.edit();
                myEditor.putString(MyShardPrefernces.UserEmail,editEmail.getText().toString());
                myEditor.putString(MyShardPrefernces.UserID,user_id);
                myEditor.putString(MyShardPrefernces.UserContact,editContact.getText().toString());
                myEditor.putString(MyShardPrefernces.UserName,editFistName.getText().toString()+" "+editLastName.getText().toString());
                myEditor.putString(MyShardPrefernces.UserImageUrl,"sfgfg");
                myEditor.putString(MyShardPrefernces.UserLoginStatus,"1");
                myEditor.commit();

                Intent intent = new Intent(SignUpActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                //  map.put("api_key",getResources().getString(R.string.api_key));
                map.put("user_name",editFistName.getText().toString()+" "+editLastName.getText().toString());
                map.put("user_email",editEmail.getText().toString());
                map.put("user_password",editPass.getText().toString());
                map.put("phone_no",editContact.getText().toString());
                return map;
            }
        };
        request.setShouldCache(false);
        queue.add(request);
        progDialog = ProgressDialog.show(SignUpActivity.this, null, null, false, false );
        progDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        progDialog.setContentView( R.layout.progress_style);
    }
}
