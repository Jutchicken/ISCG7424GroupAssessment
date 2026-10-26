package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;

public class C_MainPage extends AppCompatActivity {

    CardView cvProfile;
    CardView cvBook, cvEditReservations;

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



        cvProfile = findViewById(R.id.cv_cProfile);
        cvBook = findViewById(R.id.cv_customerBooking);
        cvEditReservations = findViewById(R.id.cv_customerEditReservations);


        cvProfile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going to Profile...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    C_Profile.class);
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