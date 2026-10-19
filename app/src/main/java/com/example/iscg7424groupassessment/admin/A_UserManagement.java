package com.example.iscg7424groupassessment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.CustomerMainPage;
import com.example.iscg7424groupassessment.R;

public class A_UserManagement extends AppCompatActivity {
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_userBack);

        btnBack.setOnClickListener(v ->{
                Toast.makeText(getApplicationContext(),
                        "Going Back to Main Page",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),
                        A_MainPage.class);
                startActivity(intent);
                finish();
        });
    }
}