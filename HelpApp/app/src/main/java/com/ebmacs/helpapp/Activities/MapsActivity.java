package com.ebmacs.helpapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.ebmacs.helpapp.GeneralHelper;
import com.ebmacs.helpapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    LatLng mlatLng;
    String address ="";
    TextView txtAddress ;
    GeneralHelper generalHelper;
    TextView btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        txtAddress = findViewById(R.id.textAddrssMap);
        btnOk = findViewById(R.id.btn_ok_map);

        generalHelper = new GeneralHelper(this);
        mlatLng = generalHelper.getLatLong();
        address = generalHelper.getCompleteAddressString(mlatLng.latitude,mlatLng.longitude);
        txtAddress.setText(address);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }






    @Override
    public void onBackPressed() {



        Intent data = new Intent();
        data.putExtra("user_lat",String.valueOf(mlatLng.latitude));
        data.putExtra("user_lng",String.valueOf(mlatLng.latitude));
        setResult(RESULT_OK,data);
        finish();
        super.onBackPressed();
    }

    void setListeneres(){

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
                // TODO Auto-generated method stub

                mlatLng = latlng;
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                address = generalHelper.getCompleteAddressString(mlatLng.latitude,mlatLng.longitude);
                txtAddress.setText(address);

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("user_lat",String.valueOf(mlatLng.latitude));
                data.putExtra("user_lng",String.valueOf(mlatLng.latitude));
                setResult(RESULT_OK,data);
                finish();

            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mlatLng.latitude,mlatLng.longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(12).build();

        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        setListeneres();
    }






}
