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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class S_MainPage extends AppCompatActivity {

    CardView cvConfirmedBooking, cvBookingStaffRequests;
    TextView tvBookingStaffCount, tvRequestStaffCount;
    DatabaseReference bookingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_main_page);

        cvConfirmedBooking = findViewById(R.id.cv_confirmedStaffBooking);
        cvBookingStaffRequests = findViewById(R.id.cv_bookingStaffRequests);
        tvBookingStaffCount = findViewById(R.id.tv_bookingStaffCount);
        tvRequestStaffCount = findViewById(R.id.tv_requestStaffCount);

        bookingDatabase = FirebaseDatabase.getInstance(
                "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/"
        ).getReference("bookings");


        loadBookedCount();


        cvConfirmedBooking.setOnClickListener(v -> {
            Intent intent = new Intent(
                    S_MainPage.this,
                    S_ConfirmedBookings.class
            );
            startActivity(intent);
        });

        cvBookingStaffRequests.setOnClickListener(v -> {
            Intent intent = new Intent(
                    S_MainPage.this,
                    S_BookingRequest.class
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookedCount();
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

    private void loadBookedCount() {
        bookingDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int requestCount = 0;
                int bookingCount = 0;
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {

                    String status = bookingSnapshot.child("status")
                            .getValue(String.class);
                    if (status == null) {
                        continue;
                    }
                    if (status.equalsIgnoreCase("Pending")) {
                        requestCount++;
                    }
                    if (status.equalsIgnoreCase("Confirmed")) {
                        bookingCount++;
                    }
                }

                tvRequestStaffCount.setText(
                        String.valueOf(requestCount)
                );

                tvBookingStaffCount.setText(
                        String.valueOf(bookingCount)
                );
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(
                        S_MainPage.this,
                        "Failed to load bookings: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}