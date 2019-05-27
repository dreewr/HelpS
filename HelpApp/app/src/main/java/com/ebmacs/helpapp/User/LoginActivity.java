package com.ebmacs.helpapp.User;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Models.Farmacino2;
import com.ebmacs.helpapp.MyShardPrefernces;
import com.ebmacs.helpapp.Activities.ProfileActivity;
import com.ebmacs.helpapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ArrayList<Farmacino2> arrayList;

    private final String URL = "http://ebmacshost.com/help/api/loginwithfb";
    String fbUserName, fbOathId, fbUserEmail, fbUserImage;
    private final String gmURL = "http://ebmacshost.com/help/api/loginwithgmail";
    String gmUserName, gmOathId, gmUserEmail, gmUserImage;

    String total_score, level;


    Button btnFb, btnGoogle;
    TextView txtTerms;
    CallbackManager callbackManager;
    ProgressDialog mDailog;

    public static URL url = null;

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences.Editor editor;
    SharedPreferences mySHaredPrefernce;
    LoginButton loginButton;

    TextView txtStartSession;
    String userLogInStatus = "";
    LinearLayout btnFacebook;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        //  printKeyHash();
        createNotificationChannel();
        showNotificationOpenDialog();


        mySHaredPrefernce = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);
        userLogInStatus = mySHaredPrefernce.getString(MyShardPrefernces.UserLoginStatus, "");
        if (userLogInStatus.equals("1")) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        }

        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        intializeViews();
        setListeneres();

    }


    private void intializeViews() {


        btnGoogle = findViewById(R.id.btnGoogle);
        signInButton = findViewById(R.id.googleSignin);
        btnFacebook = findViewById(R.id.FrameLayout1);
        txtTerms = findViewById(R.id.txtTerms);

        String first = "Continuar significa que você leu e aceitou nossos ";
        String next = "Termos e Condições - Politica de Privacidade";
        txtTerms.setText(first + next, TextView.BufferType.SPANNABLE);
        //    txtStartSession = findViewById(R.id.text_start_session);

        Spannable s = (Spannable) txtTerms.getText();
        int start = first.length();
        int end = start + next.length();
        s.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    void setListeneres() {


        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInFacebook(view);
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

    }


    private void signInGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
            url = profile_picture;
//            Bundle loginBundle = new Bundle();
//            loginBundle.putString("personName", object.getString("name"));
//            loginBundle.putString("personEmail",object.getString("email"));
//            loginBundle.putString("personPhoto",url+"");

            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            // intent.putExtras(loginBundle);
            editor = mySHaredPrefernce.edit();
            editor.putString(MyShardPrefernces.UserName, object.getString("name"));
            editor.putString(MyShardPrefernces.UserEmail, object.getString("email"));
            editor.putString(MyShardPrefernces.UserImageUrl, url + "");
            editor.putString(MyShardPrefernces.UserLoginStatus, "1");
            editor.commit();
            startActivity(intent);
            finish();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            editor = mySHaredPrefernce.edit();
            assert acct != null;
            editor.putString(MyShardPrefernces.UserName, acct.getDisplayName());
            editor.putString(MyShardPrefernces.UserEmail, acct.getEmail());
            editor.putString(MyShardPrefernces.UserImageUrl, acct.getPhotoUrl() + "");
            editor.putString(MyShardPrefernces.UserLoginStatus, "1");
            editor.commit();

            gmUserName = acct.getDisplayName();
            gmUserEmail = acct.getEmail();
            gmOathId = acct.getId();
            gmUserImage = acct.getPhotoUrl() + "";
            fetchDataFromGmail();


            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }


    public void onClickGoogleButton(View view) {
        if (view == btnGoogle) {
            signInGoogle();

        }
    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String oath_id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + oath_id + "/picture?type=normal";

                            editor = mySHaredPrefernce.edit();
                            editor.putString(MyShardPrefernces.UserName, object.getString("first_name") + " " + object.getString("last_name"));
                            editor.putString(MyShardPrefernces.UserEmail, object.getString("email"));
                            editor.putString(MyShardPrefernces.UserImageUrl, image_url + "");
                            editor.putString(MyShardPrefernces.UserLoginStatus, "1");
                            editor.commit();


                            fbUserName = object.getString("first_name") + " " + object.getString("last_name");
                            fbUserEmail = object.getString("email");
                            fbOathId = object.getString("id");
                            fbUserImage = "https://graph.facebook.com/" + oath_id + "/picture?type=normal";

//                            Log.i("UserName",fbUserName);
//                            Log.i("UserEmial",fbUserEmail);
//                            Log.i("UserOathId",fbOathId);
//                            Log.i("UserProfileImage",fbUserImage);
                            fetchUserData();

                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);

                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void LogInFacebook(View view) {


        String abc = "";
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {

            getUserProfile(AccessToken.getCurrentAccessToken());
        } else {
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {


                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    getData(object);
                                    mDailog.dismiss();

                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email");
                            request.setParameters(parameters);
                            request.executeAsync();

                        }

                        @Override
                        public void onCancel() {
                            String ab = "";
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            String abc = "";
                        }
                    });

            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
        }

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


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = MyShardPrefernces.myNotificationChannelName;

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MyShardPrefernces.myNoficataionChannelID, name, importance);
            channel.setDescription("Notifications for alert Messenging");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public boolean isNotificationChannelEnabled(Context context, @Nullable String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(channelId)) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = manager.getNotificationChannel(channelId);
                return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
            }
            return false;
        } else {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }


    void showNotificationOpenDialog() {

        if (isNotificationChannelEnabled(LoginActivity.this, MyShardPrefernces.myNoficataionChannelID)) {

        } else {
            LayoutInflater factory = LayoutInflater.from(this);
            final View deleteDialogView = factory.inflate(R.layout.notification_dialog, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //your business logic
                    deleteDialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

//for Android 5-7
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);

// for Android 8 and above
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                    startActivity(intent);
                }
            });
            deleteDialogView.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            deleteDialog.show();
        }

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ebmacs.helpapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("-fb", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (Exception e) {

        }
    }


    ////////Fetching FacebookUser data and Sending To admin Panel
    private void fetchUserData() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {

                                JSONObject obj = jsonObject.getJSONObject("user_detail");
                                String user_id = obj.getString("user_id");
                                total_score = obj.getString("total_score");
                                level = obj.getString("level");

                                Farmacino2 farmacino2 = new Farmacino2();
                                farmacino2.setUserId(user_id);

                                Log.i("USer_id", user_id);
                                Log.i("UserName", fbUserName);
                                Log.i("UserEmial", fbUserEmail);
                                Log.i("UserOathId", fbOathId);
                                Log.i("UserProfileImage", fbUserImage);


                                SharedPreferences.Editor editor = getSharedPreferences("USER_DATA", MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("User_name", fbUserName);
                                editor.apply();

                                // SAVING SCORE AND LEVEL
                                SharedPreferences.Editor editor1 = getSharedPreferences("SCORE_LEVEL", MODE_PRIVATE).edit();
                                editor1.putString("total_score", total_score);
                                editor1.putString("level", level);
                                editor1.apply();


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
                params.put("user_name", fbUserName);
                params.put("oath_id", fbOathId);
                params.put("user_email", fbUserEmail);
                params.put("image_url", fbUserImage);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    ////////Fetching Gmail data and Sending To admin Panel
    private void fetchDataFromGmail() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, gmURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.wtf("Register Response", response);
                            String success = jsonObject.getString("sucess");
                            if (success.equals("1")) {

                                JSONObject obj = jsonObject.getJSONObject("user_detail");
                                String user_id = obj.getString("user_id");
                                total_score = obj.getString("total_score");
                                level = obj.getString("level");

                                Farmacino2 farmacino2 = new Farmacino2();
                                farmacino2.setUserId(user_id);
                                Log.i("USer_id", user_id);
                                Log.i("Gmail_UserName", gmUserName);
                                Log.i("Gmail_UserEmial", gmUserEmail);
                                Log.i("Gmail_UserOathId", gmOathId);
                                Log.i("Gmail_UserProfileImage", gmUserImage);
                                Log.i("TOTAL SCORE", gmUserImage);
                                Log.i("LEVEL", gmUserImage);


                                SharedPreferences.Editor editor = getSharedPreferences("USER_DATA", MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("User_name", gmUserName);
                                editor.apply();


                                // SAVING SCORE AND LEVEL
                                SharedPreferences.Editor editor1 = getSharedPreferences("SCORE_LEVEL", MODE_PRIVATE).edit();
                                editor1.putString("total_score", total_score);
                                editor1.putString("level", level);
                                editor1.apply();


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
                params.put("user_name", gmUserName);
                params.put("oath_id", gmOathId);
                params.put("user_email", gmUserEmail);
                params.put("image_url", gmUserImage);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
