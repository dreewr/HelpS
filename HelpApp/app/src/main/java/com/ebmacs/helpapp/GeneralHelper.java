package com.ebmacs.helpapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mudassir-ktk on 2/16/2019.
 */

public class GeneralHelper {

    Context context;


    public GeneralHelper(Context context) {

        this.context = context;
    }



    public  void switchOnInternet()
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean internetNotAvailable()
    {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if(null != cm.getActiveNetworkInfo()){
            return false;
        }
        return true;

    }


    public String conVertImage64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();


        String imageString =  Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.v("image",imageString);

        return imageString;

    }

    public LatLng getLatLong() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

        Location location;

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){


            location  = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null){
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }


        }
        else{


            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }



        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

   public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
              //  Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
              //  Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
          //  Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public  Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
