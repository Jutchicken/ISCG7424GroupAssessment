package com.example.iscg7424groupassessment.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.Login;
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

public class C_Profile extends AppCompatActivity {

    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";

    FirebaseAuth mAuth;
    TextView name, name2, email, email2, phone;
    UserItem userItem;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.tv_customerName);
        name2 = findViewById(R.id.tv_customerName2);
        email = findViewById(R.id.tv_customerEmail);
        email2 = findViewById(R.id.tv_customerEmail2);
        phone = findViewById(R.id.tv_customerPhone);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        userRef = FirebaseDatabase.getInstance(DB_URL).getReference("users/"+uid);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userItem = dataSnapshot.getValue(UserItem.class);
                Log.d("StringOutput", "Value is: " + userItem.getId());
                name.setText(userItem.getFullName());
                email.setText(userItem.getEmail());
                name2.setText(userItem.getFullName());
                email2.setText(userItem.getEmail());
                phone.setText(userItem.getPhone());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Database Error", "Failed to read value." + error.toException());
            }
        });
    }

    public void btnCBack(View view) {
        Toast.makeText(getApplicationContext(),
                "Going Back to Main Page",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                C_MainPage.class);
        startActivity(intent);
        finish();
    }

    public void btnCLogout(View view) {
        Toast.makeText(getApplicationContext(),
                "Logging out",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                Login.class);
        startActivity(intent);
        finish();
    }
}