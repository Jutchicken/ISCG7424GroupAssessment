package com.example.iscg7424groupassessment.staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class S_BookingRequest extends AppCompatActivity {

    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";
    private RecyclerView recyclerBookingRequests;
    private TextView tvEmptyRequests;

    private final List<StaffTableBooking> bookingList =
            new ArrayList<>();

    private StaffBookingAdapter SBAdapter;
    private Button btnStaffBRBack;
    private DatabaseReference bookingRef;
    private ValueEventListener bookingListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_booking_request);

        btnStaffBRBack = findViewById(R.id.btn_staffBRBack);

        recyclerBookingRequests = findViewById(R.id.recyclerBookingRequests);

        tvEmptyRequests = findViewById(R.id.tv_emptyBookingRequests);

        recyclerBookingRequests.setLayoutManager(new LinearLayoutManager(this));

        SBAdapter = new StaffBookingAdapter(
                bookingList,
                new StaffBookingAdapter.OnBookingActionListener() {

                    @Override
                    public void onConfirm(StaffTableBooking booking) {
                        confirmBooking(booking);
                    }

                    @Override
                    public void onCancel(StaffTableBooking booking) {
                        cancelBooking(booking);
                    }
                }
        );

        recyclerBookingRequests.setAdapter(SBAdapter);

        bookingRef = FirebaseDatabase.getInstance(DB_URL).getReference("bookings");

        loadBookingRequests();

        btnStaffBRBack.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going Back to Main Page",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    S_MainPage.class);
            startActivity(intent);
            finish();
        });
    }
    private void loadBookingRequests() {
        bookingListener = new ValueEventListener() {

            @Override
            public void onDataChange(
                    @NonNull DataSnapshot snapshot
            ) {
                bookingList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    StaffTableBooking booking =
                            child.getValue(StaffTableBooking.class);

                    if (booking != null) {
                        booking.setId(child.getKey());

                        if ("Pending".equals(booking.getStatus())) {
                            bookingList.add(booking);
                        }
                    }
                }

                SBAdapter.notifyDataSetChanged();

                boolean empty = bookingList.isEmpty();

                tvEmptyRequests.setVisibility(empty ? View.VISIBLE : View.GONE);

                recyclerBookingRequests.setVisibility(
                        empty ? View.GONE : View.VISIBLE
                );
            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError error
            ) {
                Toast.makeText(S_BookingRequest.this,
                        "Failed to load bookings: "
                                + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };

        bookingRef.addValueEventListener(bookingListener);
    }

    private void confirmBooking(StaffTableBooking booking) {
        if (booking.getId() == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Approve Booking")
                .setMessage(
                        "Approve the booking for "
                                + booking.getCustomerName() + "?"
                )
                .setNegativeButton("No", null)
                .setPositiveButton("Approve", (dialog, which) -> {
                    updateBookingStatus(
                            booking,
                            "Confirmed"
                    );
                })
                .show();
    }

    private void cancelBooking(StaffTableBooking booking) {
        if (booking.getId() == null) {
            return;
        }

        new AlertDialog.Builder(this).setTitle("Cancel Booking")
                .setMessage("Cancel the booking for " + booking.getCustomerName() + "?")
                .setNegativeButton("No", null)
                .setPositiveButton("Cancel", (dialog, which) -> {
                    updateBookingStatus( booking, "Cancelled");
                }).show();
    }

    private void updateBookingStatus(StaffTableBooking booking, String status) {
        bookingRef.child(booking.getId())
                .child("status")
                .setValue(status)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            S_BookingRequest.this,
                            "Booking status updated",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(
                            S_BookingRequest.this,
                            "Update failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    public void btnBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bookingRef != null && bookingListener != null) {
            bookingRef.removeEventListener(bookingListener);
        }
    }
}