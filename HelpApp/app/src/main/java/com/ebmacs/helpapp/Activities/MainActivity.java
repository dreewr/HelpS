package com.ebmacs.helpapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.ebmacs.helpapp.MyShardPrefernces;
import com.ebmacs.helpapp.R;
import com.ebmacs.helpapp.Adapters.TutorialAdapter;
import com.ebmacs.helpapp.Services.MyFirebaseInstanceService;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public static String contextFrom = "";

    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Cadastre seu medicamento usando o código de barras",
            "Complete os dados com a data de validade e a prescrição", "Receba alertas do seu tratamento",
            "Ao final do tratamento compartilhe o seu medicamento", "Ganhe pontos a cada ação executada",
            "Quando precisar busque medicamentos compartilhados próximo a você", "Conclua o compartihamento do medicamento via chat",
            "Receba alertas do vencimento do medicamentos", "Receba alertas da forma correta de descarte dos seus medicamentos",
            "Troque seus pontos por benefícios exculsivos"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        init();


        SharedPreferences prefs = getSharedPreferences("REFRESHED_TOKEN", MODE_PRIVATE);
        String restoredText = prefs.getString("mToken", null);


    }


    private void initializeViews() {
        indicator = findViewById(R.id.indicator);
        pager = findViewById(R.id.pager);

    }


    public void nextActivity(View view) {
        Log.i("This is method called", "This method has been called");
        currentPage++;
        if (currentPage == NUM_PAGES) {


            if (contextFrom.equals("MyFarma")) {
                contextFrom = "";
                currentPage = 0;
                finish();
            } else {
                if (contextFrom.equals("profile")) {
                    contextFrom = "";
                    currentPage = 0;
                    finish();
                } else {
                    SharedPreferences myPrefernce = getSharedPreferences(MyShardPrefernces.mPreferenceName, MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefernce.edit();
                    editor.putString(MyShardPrefernces.lectureViewd, "1");
                    editor.commit();
                    startActivity(new Intent(this, MyFarmacinhaActivity.class));
                    finish();
                }
            }
        } else {
            pager.setCurrentItem(currentPage);
        }
    }

    private void init() {

        pager.setAdapter(new TutorialAdapter(this, arrayList));

        indicator.setViewPager(pager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES = arrayList.size();


        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
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
}
