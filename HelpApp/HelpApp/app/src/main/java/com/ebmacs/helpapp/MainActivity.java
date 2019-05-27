package com.ebmacs.helpapp;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.support.v4.view.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Chat de Entrega \ne Grupos de Amigos",
            "Crie sua própria farmácia particular", "Procure qualquer remédio",
            "Converse com o proprietário do medicamento", "Adicione seus amigos e compartilhe medicamentos com eles"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        init();
    }

    private void initializeViews() {
        indicator = findViewById(R.id.indicator);
        pager = findViewById(R.id.pager);
    }


    public void nextActivity(View view){
        currentPage++;
        if(currentPage == NUM_PAGES) {
            startActivity(new Intent(this, MyFarmacinhaActivity.class));
        }
        else {
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
  
}
