package com.example.iscg7424groupassessment.staff;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.PSAdapter;
import com.example.iscg7424groupassessment.Property;
import com.example.iscg7424groupassessment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class S_ConfirmedBookings extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_confirmed_bookings);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void btnSBack(View view) {
        finish();
    }
}