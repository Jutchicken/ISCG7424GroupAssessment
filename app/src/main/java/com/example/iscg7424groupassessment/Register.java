package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

// Just UI, no actual working register feature in this

public class Register extends AppCompatActivity {
    Spinner spinnerRole;
    EditText etFullName,etUName, etEmail, etPhone, etPassword, etConfirmPassword;
    Button btnRegister, btnBackLogin;
    FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        spinnerRole = findViewById(R.id.spin_accountType);

        etFullName = findViewById(R.id.et_Fname);
        etUName = findViewById(R.id.et_Uname);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirmPassword);
        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btn_register);
        btnBackLogin = findViewById(R.id.btn_backLogin);

        // Account types
        String[] roles = {
                "Customer",
                "Staff",
                "Admin"
        };

        database = FirebaseDatabase.getInstance(
                "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com/"
        ).getReference("users");

        ArrayAdapter<String> roleAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roles
                );

        spinnerRole.setAdapter(roleAdapter);

        // Register
        btnRegister.setOnClickListener(v -> {

            String Fullname = etFullName.getText().toString().trim();
            String Username = etUName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String role = spinnerRole.getSelectedItem().toString();

            // Empty field check
            if (Fullname.isEmpty()
                    || Username.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                Toast.makeText(
                        getApplicationContext(),
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Password check
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }

            if (password.length() < 3) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }

            btnRegister.setEnabled(false);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = task.getResult().getUser();

                                // if the user is not successfully created
                                if (firebaseUser == null) {
                                    btnRegister.setEnabled(true);
                                    return;
                                }

                                String uid = firebaseUser.getUid();

                                Map<String, Object> userData = new HashMap<>();

                                userData.put("id", uid);
                                userData.put("fullName", Fullname);
                                userData.put("username", Username);
                                userData.put("email", email);
                                userData.put("phone", phone);
                                userData.put("role", role);

                                database.child(uid).setValue(userData)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "User Register Successful",
                                                    Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();

                                            Intent intent = new Intent(
                                                    getApplicationContext(),
                                                    Login.class
                                            );
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            btnRegister.setEnabled(true);
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Database Error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show();

                                        });

                            }
                            else {
                                btnRegister.setEnabled(true);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Unable to Register User: " +
                                                (task.getException() != null
                                                        ? task.getException().getMessage()
                                                        : "Unknown error"),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        // Back to Login
        btnBackLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    getApplicationContext(),
                    Login.class
            );

            startActivity(intent);
            finish();
        });
    }
}