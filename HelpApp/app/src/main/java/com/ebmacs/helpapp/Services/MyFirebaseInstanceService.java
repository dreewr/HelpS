package com.ebmacs.helpapp.Services;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebmacs.helpapp.Models.Groups;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    private static final String TAG = "----MyFirebasService";
    String token = null;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor editor = getSharedPreferences("REFRESHED_TOKEN", MODE_PRIVATE).edit();
        editor.putString("mToken", s);
        editor.apply();

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.i("===========NEW_TOKEN", s);
        Log.i("Old Method Token", refreshedToken);

        Log.i("SERVICE HAS BEEN CALLED", "THIS IS THE SERVICE CALL");

        // getToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.i("NOTIFICATION ", " RECEIVED");


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.i("NOTIFICATION ", "DID NOT RECEIVED");

        }
        else {
            Log.i("NOTIFICATION ", "DID NOT RECEIVED");

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]
    }
