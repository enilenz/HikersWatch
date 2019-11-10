package com.example.android.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager =  (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("location", location.toString());
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Location lastKnow = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnow != null){
                updateLocationInfo(lastKnow);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           startListening();
        }
    }

    public void startListening(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
    }
    public void updateLocationInfo(Location location){
        Log.i("loww",location.toString());
        TextView latText = findViewById(R.id.latTextView);
        TextView longText = findViewById(R.id.longTextView);
        TextView accText = findViewById(R.id.accuracyTextView);
        TextView altText = findViewById(R.id.altitudeTextView);
        TextView addressText = findViewById(R.id.addressTextView);

        latText.setText("Latitude : " + location.getLatitude());
        longText.setText("Longitude : " + location.getLatitude() );
        accText.setText("Accuracy : " + location.getLatitude() );
        addressText.setText("Altitude : " + location.getLatitude() );

        String address = "no address";

        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (addressList != null && addressList.size() >0) {
                address = "Address : \n";
                if (addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }
                if (addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + " ";
                }
                if (addressList.get(0).getPostalCode() != null){
                    address += addressList.get(0).getPostalCode() + " ";
                }
                if (addressList.get(0).getAdminArea() != null){
                    address += addressList.get(0).getAdminArea() + " ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressText.setText(address);

    }




}
