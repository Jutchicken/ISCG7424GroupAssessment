package com.example.iscg7424groupassessment.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class S_ConfirmedBookings extends AppCompatActivity {
    private static final String DB_URL =
            "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/";

    RecyclerView recyclerConfirmedBookings;
    TextView tvEmptyBookings;

    List<StaffTableBooking> bookingList = new ArrayList<>();
    StaffBookingAdapter SBAdapter;

    DatabaseReference bookingRef, userRef;
    ValueEventListener bookingListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_confirmed_bookings);

        recyclerConfirmedBookings = findViewById(R.id.rv_properties);
        tvEmptyBookings = findViewById(R.id.tv_emptyConfirmedBookings);

        bookingRef = FirebaseDatabase.getInstance(DB_URL).getReference("bookings");

        userRef = FirebaseDatabase.getInstance(DB_URL).getReference("users");

        recyclerConfirmedBookings.setLayoutManager(new LinearLayoutManager(this));

        SBAdapter = new StaffBookingAdapter(
                bookingList,
                new StaffBookingAdapter.OnBookingActionListener() {

                    @Override
                    public void onConfirm(StaffTableBooking booking) {

                    }

                    @Override
                    public void onCancel(StaffTableBooking booking) {
                        cancelBooking(booking);
                    }
                }
        );

        recyclerConfirmedBookings.setAdapter(SBAdapter);
        loadConfirmedBookings();
    }

    private void loadConfirmedBookings() {
        bookingListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    StaffTableBooking booking = child.getValue(StaffTableBooking.class);

                    if (booking != null) {
                        booking.setId(child.getKey());

                        if ("Confirmed".equalsIgnoreCase(booking.getStatus())) {
                            bookingList.add(booking);
                            String customerId = booking.getCustomerId();

                            if (customerId != null && !customerId.isEmpty()) {

                                userRef.child(customerId)
                                    .child("fullName")
                                    .get()
                                    .addOnSuccessListener(userSnapshot -> {
                                        if (!bookingList.contains(booking)) {
                                            return;
                                        }

                                        String fullName = userSnapshot.getValue(String.class);

                                        if (fullName != null && !fullName.isEmpty()) {
                                            booking.setCustomerName(fullName);
                                            SBAdapter.notifyDataSetChanged();
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(),
                                            "Failed to load customer name: "
                                            + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    });
                            }
                        }
                    }
                }
                SBAdapter.notifyDataSetChanged();
                updateEmptyMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),
                    "Failed to load bookings: "
                    + error.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
        };
        bookingRef.addValueEventListener(bookingListener);
    }

    private void updateEmptyMessage() {
        boolean empty = bookingList.isEmpty();

        tvEmptyBookings.setVisibility(
            empty ? View.VISIBLE : View.GONE
        );

        recyclerConfirmedBookings.setVisibility(
            empty ? View.GONE : View.VISIBLE
        );
    }

    public void btnSBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bookingRef != null && bookingListener != null) {
            bookingRef.removeEventListener(bookingListener);
        }
    }

    private void cancelBooking(StaffTableBooking booking) {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage(
                    "Are you sure you want to cancel the booking for "
                    + booking.getCustomerName() + "?"
                ).setPositiveButton("Yes", (dialog, which) -> {
                    String bookingId = booking.getId();

                    if (bookingId == null || bookingId.isEmpty()) {
                        Toast.makeText(getApplicationContext(),
                            "Booking ID not found",
                            Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bookingRef.child(bookingId)
                        .child("status")
                        .setValue("Cancelled")
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getApplicationContext(),
                                    "Booking Cancelled",
                                    Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(),
                                    "Failed to cancel booking: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }
}