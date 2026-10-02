package com.example.iscg7424groupassessment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class S_BookingRequest extends AppCompatActivity {

    RecyclerView recyclerBookingRequests;

    ArrayList<BookingRequest> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_booking_request);

        recyclerBookingRequests = findViewById(R.id.recyclerBookingRequests);

        recyclerBookingRequests.setLayoutManager(new LinearLayoutManager(this));

        loadRequests();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadRequests();
    }


    private void loadRequests() {

        requestList = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences(
                "SupplierData", MODE_PRIVATE
        );

        // Hardcoded data
        boolean initialized = preferences.getBoolean(
                "requestsInitialized",
                false
        );


        if (!initialized) {
            preferences.edit().putBoolean("requestsInitialized", true)
                    .putBoolean("johnRequest", true)
                    .putBoolean("sarahRequest", true)
                    .putInt("requestCount", 2)
                    .apply();
        }


        // If Request from John still exists
        boolean johnRequest = preferences.getBoolean("johnRequest", true);

        // If Request from Sarah still exists
        boolean sarahRequest = preferences.getBoolean("sarahRequest", true);


        if (johnRequest) {
            requestList.add(
                    new BookingRequest(
                            "Auckland House",
                            "John",
                            "20 Sep 2026"
                    )
            );
        }

        if (sarahRequest) {
            requestList.add(
                    new BookingRequest(
                            "Wellington Apartment",
                            "Sarah",
                            "25 Sep 2026"
                    )
            );
        }


        // 현재 실제 요청 개수 저장
        preferences.edit().putInt("requestCount", requestList.size()).apply();
        BookingRequest[] requests = requestList.toArray(new BookingRequest[0]);
        BRAdapter adapter = new BRAdapter(requests);
        recyclerBookingRequests.setAdapter(adapter);
    }

    public void btnBack(View view) {
        finish();
    }
}