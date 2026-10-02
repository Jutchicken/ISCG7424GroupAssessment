package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomerProfile extends AppCompatActivity {

    Button btnVBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnVBD = findViewById(R.id.btn_cViewBookingDetail);

        btnVBD.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going to Booking Detail",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    CustomerBookingDetail.class);
            startActivity(intent);
        });

    }

    public void btnCBack(View view) {
        Toast.makeText(getApplicationContext(),
                "Going Back to Main Page",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                CustomerMainPage.class);
        startActivity(intent);
        finish();
    }

    public void btnCLogout(View view) {
        Toast.makeText(getApplicationContext(),
                "Logging out",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                Login.class);
        startActivity(intent);
        finish();
    }
}