package com.example.iscg7424groupassessment.staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;

public class S_MainPage extends AppCompatActivity {

    CardView cvConfirmedBooking, cvBookingStaffRequests;
    TextView tvBookingStaffCount, tvRequestStaffCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_main_page);

        cvConfirmedBooking = findViewById(R.id.cv_confirmedStaffBooking);
        cvBookingStaffRequests = findViewById(R.id.cv_bookingStaffRequests);
        tvBookingStaffCount = findViewById(R.id.tv_bookingStaffCount);
        tvRequestStaffCount = findViewById(R.id.tv_requestStaffCount);

        loadBookedCount();


        cvConfirmedBooking.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Loading properties...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    S_ConfirmedBookings.class);
            startActivity(intent);
        });

        cvBookingStaffRequests.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Loading requests...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    S_BookingRequest.class
            );
            startActivity(intent);
        });
    }

    public void btnLogout(View view) {
        Toast.makeText(getApplicationContext(),
                "Going Back to Main Page",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookedCount();
    }

    private void loadBookedCount() {
        SharedPreferences preferences = getSharedPreferences(
                "SupplierData", MODE_PRIVATE );

        int propertyCount = preferences.getInt(
                "propertyCount", 0
        );

        int requestCount = preferences.getInt(
                "requestCount", 0
        );

        tvBookingStaffCount.setText(String.valueOf(propertyCount));

        tvRequestStaffCount.setText(String.valueOf(requestCount));
    }
}