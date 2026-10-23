package com.example.iscg7424groupassessment.staff;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.ArrayList;

public class S_BookingRequest extends AppCompatActivity {

    RecyclerView recyclerBookingRequests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_booking_request);

        recyclerBookingRequests = findViewById(R.id.recyclerBookingRequests);

        recyclerBookingRequests.setLayoutManager(new LinearLayoutManager(this));

    }



}