package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.staff.S_BookingRequest;
import com.example.iscg7424groupassessment.staff.StaffBookingAdapter;
import com.example.iscg7424groupassessment.staff.StaffTableBooking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class C_BookingList extends AppCompatActivity {


    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";

    Button btnCustomerListBack, btnCustomerCancel;

    RecyclerView recyclerView;

    C_BookingAdapter adapter;

    private TextView tvEmptyRequests;

    private DatabaseReference bookingRef;


    private ValueEventListener bookingListener;

    private final List<C_TableBooking> bookingList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_booking_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCustomerListBack = findViewById(R.id.btn_customerListBack);



        tvEmptyRequests = findViewById(R.id.tv_emptyCustomerBookings);
        recyclerView = findViewById(R.id.recyclerBookingRequests);
        adapter = new C_BookingAdapter(bookingList, new C_BookingAdapter.OnBookingActionListener() {
                    @Override
                    public void onCancel(C_TableBooking booking) {
                        cancelBooking(booking);
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getApplicationContext()
        ));
        recyclerView.setAdapter(adapter);

        bookingRef = FirebaseDatabase.getInstance(DB_URL).getReference("bookings");

        loadBookingRequests();

        btnCustomerListBack.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going Back to Main Page",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_MainPage.class);
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
                    C_TableBooking booking =
                            child.getValue(C_TableBooking.class);

                    if (booking != null) {
                        booking.setId(child.getKey());

                        if ("Pending".equals(booking.getStatus())) {
                            bookingList.add(booking);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

                boolean empty = bookingList.isEmpty();

                tvEmptyRequests.setVisibility(empty ? View.VISIBLE : View.GONE);

                recyclerView.setVisibility(
                        empty ? View.GONE : View.VISIBLE
                );
            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError error
            ) {
                Toast.makeText(C_BookingList.this,
                        "Failed to load bookings: "
                                + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };


        bookingRef.addValueEventListener(bookingListener);
    }

    private void cancelBooking(C_TableBooking booking) {
        if (booking.getId() == null) {
            return;
        }

        new AlertDialog.Builder(this).setTitle("Cancel Booking")
                .setMessage("Cancel the booking for " + booking.getCustomerEmail() + "?")
                .setNegativeButton("No", null)
                .setPositiveButton("Cancel", (dialog, which) -> {
                    updateBookingStatus( booking, "Cancelled");
                }).show();
    }

    private void updateBookingStatus(C_TableBooking booking, String status) {
        bookingRef.child(booking.getId())
                .child("status")
                .setValue(status)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            C_BookingList.this,
                            "Booking status updated",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(
                            C_BookingList.this,
                            "Update failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
