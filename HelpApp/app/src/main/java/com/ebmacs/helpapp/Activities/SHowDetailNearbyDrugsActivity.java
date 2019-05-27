package com.ebmacs.helpapp.Activities;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.OnSwipeTouchListener;
import com.ebmacs.helpapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SHowDetailNearbyDrugsActivity extends AppCompatActivity {


    String distance, imageUrl;
    CircleImageView imageSHare, imagHelp;
    Button upgradeTopremium;
    TextView txtGivemHelp;
    LinearLayout midLayout;
    TextView distanceTextView;
    ImageView drugImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_nearby_drugs);
        midLayout = findViewById(R.id.midLayout);
        inIT();
        setListeners();


        imageUrl = getIntent().getStringExtra("NearbyImageUrl");
        distance = getIntent().getStringExtra("distance");
        distanceTextView.setText(distance);
        Picasso.get()
                .load(imageUrl)
                .into(drugImage);


    }

    void inIT() {
        drugImage = findViewById(R.id.drugImage);
        distanceTextView = findViewById(R.id.distanceTextView);
        imageSHare = findViewById(R.id.imageSHare);
        imagHelp = findViewById(R.id.image_giveMeHelp);
        txtGivemHelp = findViewById(R.id.txtGivemeHelp);
        upgradeTopremium = findViewById(R.id.btnUpgradeToPremium);

    }

    void setListeners() {
        imageSHare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Medicamento para compartilhar");
                startActivity(Intent.createChooser(shareIntent, "Compartilhar link usando"));

            }
        });
        imagHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHelpDIlaog();


            }
        });
        txtGivemHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDIlaog();
            }
        });
        upgradeTopremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SHowDetailNearbyDrugsActivity.this, UpgradeTOPRemiumActivity.class));

            }
        });


        midLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent = new Intent(SHowDetailNearbyDrugsActivity.this, MedicineOwnerActivity.class);
                startActivity(intent);
            }
        });
    }

    void showHelpDIlaog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SHowDetailNearbyDrugsActivity.this);
        final AlertDialog dialog;
        builder.setView(R.layout.dialog_low_points);
        dialog = builder.create();

        dialog.show();
        TextView btnOK = dialog.findViewById(R.id.btn_ok_dlg_discard);
        TextView btnCancl = dialog.findViewById(R.id.btn_cancl_dlg_discard);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SHowDetailNearbyDrugsActivity.this, UpgradeTOPRemiumActivity.class));
                dialog.dismiss();

            }
        });
        btnCancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}
