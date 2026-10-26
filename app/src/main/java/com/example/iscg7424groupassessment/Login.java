package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424groupassessment.admin.A_MainPage;
import com.example.iscg7424groupassessment.staff.S_MainPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    FirebaseAuth mAuth;
    DatabaseReference database;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance(
                "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/"
        ).getReference("users");

    }

    public void btnLogin(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Please enter Email and Password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Unable to get user information",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = firebaseUser.getUid();

                        database.child(uid).get()
                                .addOnSuccessListener(snapshot -> {
                                    if (!snapshot.exists()) {
                                        Toast.makeText(getApplicationContext(),
                                                "User information not found",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        return;
                                    }

                                    String role = snapshot.child("role").getValue(String.class);

                                    if (role == null) {
                                        Toast.makeText(getApplicationContext(),
                                                "User role not found",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        return;
                                    }

                                    if (role.equalsIgnoreCase("Admin")) {
                                        intent = new Intent(getApplicationContext(),
                                                A_MainPage.class);
                                    } else if (role.equalsIgnoreCase("Staff")) {
                                        intent = new Intent(getApplicationContext(),
                                                S_MainPage.class);
                                    } else if (role.equalsIgnoreCase("Customer")) {
                                        intent = new Intent(getApplicationContext(),
                                                CustomerMainPage.class);
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Invalid user role",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        return;
                                    }
                                    Toast.makeText(getApplicationContext(),
                                            "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(),
                                            "Database Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Wrong Email or Password",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void btnRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),
                Register.class);
        startActivity(intent);
    }
};