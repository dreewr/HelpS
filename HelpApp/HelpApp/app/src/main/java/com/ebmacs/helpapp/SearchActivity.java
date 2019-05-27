package com.ebmacs.helpapp;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {

    private ArrayList<Integer> al;
    private SearchAdapter arrayAdapter;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        al = new ArrayList<>();
        al.add(R.drawable.medicine);
        al.add(R.drawable.medicine2);
        al.add(R.drawable.medicine3);
        al.add(R.drawable.medicine4);
        al.add(R.drawable.medicine);
        al.add(R.drawable.medicine3);
        al.add(R.drawable.medicine2);
        al.add(R.drawable.medicine4);


    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }



}