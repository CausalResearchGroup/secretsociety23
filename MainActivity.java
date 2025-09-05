package com.joseph.emergencyapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

public class MainActivity extends Activity {

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private Handler handler = new Handler();

    // Enter Joseph's trusted extraction contact numbers here
    private final String[] emergencyContacts = {"+1234567890", "+1987654321"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
        }, 1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button alertButton = findViewById(R.id.btnEmergencyAlert);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmergencyAlert();
            }
        });

        startLocationUpdates();
        startPeriodicCheckIn();
    }

    private void startLocationUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                    }
                });
                handler.postDelayed(this, 60000); // repeat every 60 seconds
            }
        }, 1000);
    }
