package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.admin.A_MenuManagement;
import com.example.iscg7424groupassessment.admin.UserItem;
import com.example.iscg7424groupassessment.staff.S_MainPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class C_MainPage extends AppCompatActivity {

    CardView cvProfile, cvBook, cvEditReservations, cvMenu;
    TextView tvBookingCustomerCount, tvHeader, tvSubHeader;
    DatabaseReference bookingDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        bookingDatabase = FirebaseDatabase.getInstance(
                "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/"
        ).getReference("bookings");


        cvMenu = findViewById(R.id.cv_customerMenu);
        cvProfile = findViewById(R.id.cv_cProfile);
        cvBook = findViewById(R.id.cv_customerBooking);
        cvEditReservations = findViewById(R.id.cv_customerEditReservations);
        tvBookingCustomerCount = findViewById(R.id.tv_customerBookings);
        tvHeader = findViewById(R.id.tv_customerHeader);
        tvSubHeader = findViewById(R.id.tv_customerSubHeader);




        displayUser();
        loadBookedCount();

        cvProfile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going to Profile...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_Profile.class);
            startActivity(intent);
        });

        cvMenu.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Viewing Menu...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_Menu.class);
            startActivity(intent);
        });


        cvBook.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Booking...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_BookingRequest.class);
            startActivity(intent);
        });

        cvEditReservations.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Viewing Reservations...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_BookingList.class);
            startActivity(intent);
        });
    }

    private void loadBookedCount() {
        bookingDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int bookingCount = 0;
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {

                    String status = bookingSnapshot.child("status")
                            .getValue(String.class);
                    if (status == null) {
                        continue;
                    }
                    if (status.equalsIgnoreCase("Pending")) {
                        bookingCount++;
                    }
                    if (status.equalsIgnoreCase("Confirmed")) {
                        bookingCount++;
                    }
                }


                tvBookingCustomerCount.setText(
                        String.valueOf(bookingCount)
                );
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(
                        C_MainPage.this,
                        "Failed to load bookings: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void displayUser(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance(
                "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/"
        ).getReference("users/"+uid);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserItem userItem = dataSnapshot.getValue(UserItem.class);
                Log.d("StringOutput", "Value is: " + userItem.getId());
                String name = userItem.getFullName();
                tvHeader.setText(name+ "'s Dashboard");
                tvSubHeader.setText("Welcome, " +name);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Database Error", "Failed to read value." + error.toException());
            }
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

}