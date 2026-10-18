package com.example.iscg7424groupassessment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;

public class A_MainPage extends AppCompatActivity {

    CardView cvUserManagement, cvMenuManagement, cvTableManagement, cvTableBookingManagement;
    Button btnALogout;
    TextView tvUserCount, tvMenuItemCount, tvTableCount, tvTodayBookingCount;
    ImageButton ibAdminProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_page);

        cvUserManagement = findViewById(R.id.cv_userManagement);
        cvMenuManagement = findViewById(R.id.cv_menuManagement);
        cvTableManagement = findViewById(R.id.cv_tableManagement);
        cvTableBookingManagement = findViewById(R.id.cv_tableBookingManagement);

        tvUserCount = findViewById(R.id.tv_userCount);
        tvMenuItemCount = findViewById(R.id.tv_menuItemCount);
        tvTableCount = findViewById(R.id.tv_tableCount);
        tvTodayBookingCount = findViewById(R.id.tv_todayBookingCount);

        btnALogout = findViewById(R.id.btn_aLogout);
        ibAdminProfile = findViewById(R.id.ib_adminProfile);

        cvUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(A_MainPage.this, A_UserManagement.class);
               startActivity(intent);
            }
        });

        cvMenuManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_MenuManagement.class);
                startActivity(intent);
            }
        });

        cvTableManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_TableManagement.class);
                startActivity(intent);
            }
        });

        cvTableBookingManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_BookingManagement.class);
                startActivity(intent);
            }
        });

        ibAdminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Feature for Admin profile",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnALogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}