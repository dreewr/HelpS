package com.ebmacs.helpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyFarmacinhaActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FarmacinoAdapter adapter;
    ArrayList<Farmacino> arrayList;
    ImageView imgHelp, imgSearch;
    LinearLayout layoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfarmacinha);


        initializeViews();
        setRecyclerView();
        setClickListeners();

    }

    private void setClickListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MyFarmacinhaActivity.this, "adicionar", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyFarmacinhaActivity.this, AddMedicineActivity.class));
            }
        });



        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFarmacinhaActivity.this, SearchActivity.class));
            }
        });

        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Minha pontuação", "Bate-papo", "Amigos"));
                showListMenu(imgHelp, arrayList);
            }
        });
    }

    private void showListMenu(View anchor, ArrayList<String> arrayList) {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        final List<String> array = (List) arrayList;

        final ListAdapter adapter = new ListPopupWindowAdapter(this, array);

        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(400);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    startActivity(new Intent(MyFarmacinhaActivity.this, MyScoreActivity.class));
                }
                else if(i==1){
                    startActivity(new Intent(MyFarmacinhaActivity.this, InboxActivity.class));
                }
                else if(i==2){
                    startActivity(new Intent(MyFarmacinhaActivity.this, FriendsActivity.class));
                }

                popupWindow.dismiss();

            }
        });
        popupWindow.show();
    }


    private void setRecyclerView() {
        arrayList = new ArrayList<>();

        Farmacino farmacino = new Farmacino();
        farmacino.setName("Dipirona Sodica");
        farmacino.setExpire("15/19");
        farmacino.setTime("6 de 6 horas");
        farmacino.setImage(R.drawable.medicine2);

        arrayList.add(farmacino);

        Farmacino farmacino1 = new Farmacino();
        farmacino1.setName("Torsilax");
        farmacino1.setExpire("15/09");
        farmacino1.setTime("11:00");
        farmacino1.setImage(R.drawable.medicine);

        arrayList.add(farmacino1);


        Farmacino farmacino2 = new Farmacino();
        farmacino2.setName("Neosaldina");
        farmacino2.setExpire("14/09");
        farmacino2.setTime("Terca e Quinta");
        farmacino2.setImage(R.drawable.medicine3);

        arrayList.add(farmacino2);

        Farmacino farmacino3 = new Farmacino();
        farmacino3.setName("Dipirona Sodica");
        farmacino3.setExpire("15/19");
        farmacino3.setTime("6 de 6 horas");
        farmacino3.setImage(R.drawable.medicine2);

        arrayList.add(farmacino3);

        Farmacino farmacino4 = new Farmacino();
        farmacino4.setName("Dipirona Sodica");
        farmacino4.setExpire("15/19");
        farmacino4.setTime("6 de 6 horas");
        farmacino4.setImage(R.drawable.medicine2);

        arrayList.add(farmacino4);
//
//        Farmacino farmacino5 = new Farmacino();
//        farmacino5.setName("Neosaldina");
//        farmacino5.setExpire("Validity: 14/09");
//        farmacino5.setTime("Tue and Thur");
//        farmacino5.setImage(R.drawable.medicine3);
//        arrayList.add(farmacino5);

        adapter = new FarmacinoAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        imgHelp = findViewById(R.id.imgHelp);
        layoutMenu = findViewById(R.id.layoutMenu);
        imgSearch = findViewById(R.id.imgSearch);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

}


