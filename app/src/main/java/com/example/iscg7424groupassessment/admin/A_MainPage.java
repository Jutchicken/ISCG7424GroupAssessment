package com.example.iscg7424groupassessment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class A_MainPage extends AppCompatActivity {

    private static final String DB_URL =
            "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";

    CardView cvUserManagement, cvMenuManagement, cvTableManagement, cvTableBookingManagement;
    Button btnALogout;
    TextView tvUserCount, tvMenuItemCount, tvTableCount, tvTodayBookingCount;
    ImageButton ibAdminProfile;

    private final List<DatabaseReference> watchedRefs = new ArrayList<>();
    private final List<ValueEventListener> watchedListeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_page);

        // Keeps the header below the status bar and keeps the 16dp margin
        final int pad = (int) (16 * getResources().getDisplayMetrics().density);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left + pad, bars.top + pad, bars.right + pad, bars.bottom + pad);
                return insets;
            }
        });

        cvUserManagement = findViewById(R.id.cv_userManagement);
        cvMenuManagement = findViewById(R.id.cv_menuManagement);
        cvTableManagement = findViewById(R.id.cv_tableManagement);
        cvTableBookingManagement = findViewById(R.id.cv_tableBookingManagement);

        tvUserCount = findViewById(R.id.tv_userCount);
        tvMenuItemCount = findViewById(R.id.tv_menuItemCount);
        tvTableCount = findViewById(R.id.tv_tableCount);
        tvTodayBookingCount = findViewById(R.id.tv_todayBookingCount);

        btnALogout = findViewById(R.id.btn_aLogout);
        ibAdminProfile = findViewById(R.id.ib_adminProfile);

        cvUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_UserManagement.class);
                startActivity(intent);
            }
        });

        cvMenuManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_MenuManagement.class);
                startActivity(intent);
            }
        });

        cvTableManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_TableManagement.class);
                startActivity(intent);
            }
        });

        cvTableBookingManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainPage.this, A_BookingManagement.class);
                startActivity(intent);
            }
        });

        ibAdminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Feature for Admin profile",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnALogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        loadCounts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < watchedRefs.size(); i++) {
            watchedRefs.get(i).removeEventListener(watchedListeners.get(i));
        }
    }

    private void loadCounts() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(DB_URL);

        watch(db.getReference("menu_items"), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvMenuItemCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // keep the last number shown
            }
        });

        watch(db.getReference("tables"), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTableCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        watch(db.getReference("users"), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int staff = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String role = child.child("role").getValue(String.class);
                    if ("Staff".equals(role)) {
                        staff++;
                    }
                }
                tvUserCount.setText(String.valueOf(staff));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        watch(db.getReference("bookings"), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTodayBookingCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void watch(DatabaseReference ref, ValueEventListener listener) {
        ref.addValueEventListener(listener);
        watchedRefs.add(ref);
        watchedListeners.add(listener);
    }
}