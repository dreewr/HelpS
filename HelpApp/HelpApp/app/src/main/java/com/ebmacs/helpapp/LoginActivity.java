package com.ebmacs.helpapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    Button btnFb;
    TextView txtTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intializeViews();

    }

    private void intializeViews() {
        txtTerms = findViewById(R.id.txtTerms);

        String first = "Continuar significa que voce leu e aceitou nossos ";
        String next = "Termos e Condicoes - Politica de Privacidade";

        txtTerms.setText(first + next, TextView.BufferType.SPANNABLE);
        Spannable s = (Spannable)txtTerms.getText();
        int start = first.length();
        int end = start + next.length();
        s.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public void fbClicked(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }

}
