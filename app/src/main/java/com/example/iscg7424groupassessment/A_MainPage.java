package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class A_MainPage extends AppCompatActivity {

    CardView cvUserManagement, cvPropertyManagement, cvBookingManagement;
    Button btnALogout;
    ImageButton ibAdminProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_page);

        cvUserManagement = findViewById(R.id.cv_userManagement);
        cvPropertyManagement = findViewById(R.id.cv_propertyManagement);
        cvBookingManagement = findViewById(R.id.cv_bookingManagement);
        btnALogout = findViewById(R.id.btn_aLogout);
        ibAdminProfile = findViewById(R.id.ib_adminProfile);

        cvUserManagement.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Feature for User management",
                    Toast.LENGTH_SHORT).show();
        });

        cvPropertyManagement.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Feature for Property management",
                    Toast.LENGTH_SHORT).show();
        });

        cvBookingManagement.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Feature for Booking management",
                    Toast.LENGTH_SHORT).show();
        });

        ibAdminProfile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Feature for Admin profile",
                    Toast.LENGTH_SHORT).show();
        });

        btnALogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),
                    Login.class);
            startActivity(intent);
            finish();
        });

    }
}