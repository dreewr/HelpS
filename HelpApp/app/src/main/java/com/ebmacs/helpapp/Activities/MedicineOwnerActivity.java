package com.ebmacs.helpapp.Activities;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ebmacs.helpapp.Chat.ChatActivity;
import com.ebmacs.helpapp.R;

public class MedicineOwnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_owner);
    }


    public void sendToChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("type", "");
        startActivity(intent);
    }

    public void cancel(View view) {

        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          //  finishAffinity();
        }

    }
}
