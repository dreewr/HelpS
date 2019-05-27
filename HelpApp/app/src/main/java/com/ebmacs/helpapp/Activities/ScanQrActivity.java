package com.ebmacs.helpapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanQrActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {


    private BarcodeReader barcodeReader;
    TextView txtBarcodeTxt,btnOk;
    ImageView refreshScanning;
    LinearLayout linearLayoutShowTxt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        txtBarcodeTxt = findViewById(R.id.txt_barCode_scanner);
        btnOk = findViewById(R.id.btn_ok_scanning);
        refreshScanning = findViewById(R.id.btn_refresh_scaannig);
        refreshScanning.setColorFilter(Color.parseColor("#ffffff"));
        linearLayoutShowTxt = (LinearLayout) findViewById(R.id.layout_show_tex);
      ///  linearLayoutShowTxt.setVisibility(View.GONE);


        setListeners();
    }

    void setListeners(){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=txtBarcodeTxt.getText().toString();
                Intent intent=new Intent();
                intent.putExtra("code",message);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        refreshScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeReader.resumeScanning();
                linearLayoutShowTxt.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onScanned(Barcode barcode) {
       // barcodeReader.playBeep();
       final Barcode barcode1 = barcode;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayoutShowTxt.setVisibility(View.VISIBLE);
                txtBarcodeTxt.setText(barcode1.displayValue);
            }
        });


        barcodeReader.pauseScanning();

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {


    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }
}
