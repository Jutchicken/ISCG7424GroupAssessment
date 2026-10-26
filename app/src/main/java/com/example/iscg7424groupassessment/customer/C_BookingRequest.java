package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.R;

import java.util.Calendar;

public class C_BookingRequest extends AppCompatActivity {

    DatePicker dp;

    Button customerBookingBack, customerBook;

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

        dp = findViewById(R.id.datePicker);
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



}