package com.ebmacs.helpapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
    }


    public void sendToChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("type", "friends");
        startActivity(intent);
    }
}
