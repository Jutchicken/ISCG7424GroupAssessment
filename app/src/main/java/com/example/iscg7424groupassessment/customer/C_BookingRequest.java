package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.admin.UserItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;

public class C_BookingRequest extends AppCompatActivity {


    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference parentReference, userRef;
    FirebaseAuth mAuth;
    DatePicker dp;
    TimePicker tp;
    NumberPicker np;
    Button customerBookingBack, customerBook;

    private static final int tableNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_booking_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        String customerEmail = firebaseUser.getEmail();

        firebaseDatabase = FirebaseDatabase.getInstance();
        parentReference = firebaseDatabase.getReference();
        userRef = FirebaseDatabase.getInstance(DB_URL).getReference("users");
        dp = findViewById(R.id.datePicker);
        tp = findViewById(R.id.tpCustomer);
        np = findViewById(R.id.npCustomer);
        customerBookingBack = findViewById(R.id.btn_customerBookingBack);
        customerBook = findViewById(R.id.btn_customerBook);




        Calendar today = Calendar.getInstance();

        // Initialize DatePicker with the current date
        dp.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int day) {


                        String msg = "You Selected: " + day + "/" + (month + 1) + "/" + year;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        np.setMaxValue(6);
        np.setMinValue(1);



        customerBook.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Booking Entered. Going Back to Main Page",
                    Toast.LENGTH_SHORT).show();


            addBooking(uid, customerEmail);
            Intent intent = new Intent(getApplicationContext(),
                    C_MainPage.class);
            startActivity(intent);
            finish();
        });

        customerBookingBack.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going Back to Main Page",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_MainPage.class);
            startActivity(intent);
            finish();
        });
    }

    public void addBooking(String uid, String customerEmail){
        String bookingId = parentReference.child("bookings").push().getKey();

        C_TableBooking booking = new C_TableBooking(
                bookingId,
                uid,
                customerEmail,
                tableNumber,
                dp.getDayOfMonth() +"/"+ (dp.getMonth()+1) +"/"+ dp.getYear(),
                tp.getHour() +":"+ tp.getMinute(),
                np.getValue(),
                "Pending"

                );
        parentReference.child("bookings").child(bookingId).setValue(booking);
        Toast.makeText(this,"added new fruit",
                Toast.LENGTH_LONG).show();
    }

}