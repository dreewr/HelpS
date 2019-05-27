package com.ebmacs.helpapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InboxAdapter adapter;
    ArrayList<String> arrayList;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        initializeViews();
        setRecyclerView();
    }

    private void setRecyclerView() {
        arrayList = new ArrayList<>();
        for(int i=0;i<10;i++){
            arrayList.add("1");
        }

        adapter = new InboxAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


}
