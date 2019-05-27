package com.ebmacs.helpapp.Activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebmacs.helpapp.Chat.ChatActivity;
import com.ebmacs.helpapp.Dialogs.SharePointsDialog;
import com.ebmacs.helpapp.ProfileDetailsActivity;
import com.ebmacs.helpapp.R;

public class MyScoreActivity extends AppCompatActivity {

    LinearLayout parentLayout;
    ImageView sharePoints;
    TextView scoresTextView, levelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        inIT();
        setListeners();
        SharedPreferences prefs = getSharedPreferences("SCORE_LEVEL", MODE_PRIVATE);
        String total_score = prefs.getString("total_score", "");
        String level = prefs.getString("level", "");

        scoresTextView.setText(total_score);
        levelTextView.setText(level);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("This is On", "RESUME CALLED ");
    }

    void inIT() {
        parentLayout = findViewById(R.id.parentLayout);
        sharePoints = findViewById(R.id.sharePoints);
        scoresTextView = findViewById(R.id.scoresTextView);
        levelTextView = findViewById(R.id.levelTextView);
    }

    void setListeners() {
        sharePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePointsDialog dialog = new SharePointsDialog();
                dialog.setContext(MyScoreActivity.this);
                // open dialog fragment
                dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    public void setScoreToTextView(String score){
        scoresTextView.setText(score);
    }



    public void sendToChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("type", "friends");
        startActivity(intent);
    }
}