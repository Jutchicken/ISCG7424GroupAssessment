package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

// Just UI, no actual working register feature in this

public class Register extends AppCompatActivity {
    Spinner spinnerRole;
    EditText etName, etEmail, etPhone, etID, etPassword, etConfirmPassword;
    Button btnRegister, btnBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        spinnerRole = findViewById(R.id.spin_accountType);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etID = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirmPassword);

        btnRegister = findViewById(R.id.btn_register);
        btnBackLogin = findViewById(R.id.btn_backLogin);

        // Account types
        String[] roles = {
                "Customer",
                "Supplier",
                "Admin"
        };

        ArrayAdapter<String> roleAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roles
                );

        spinnerRole.setAdapter(roleAdapter);

        // Register
        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String id = etID.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword =
                    etConfirmPassword.getText().toString();

            String role =
                    spinnerRole.getSelectedItem().toString();

            // Empty field check
            if (name.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || id.isEmpty()
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
                Toast.makeText(
                        getApplicationContext(),
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Toast.makeText(
                    getApplicationContext(),
                    "Registration successful!",
                    Toast.LENGTH_SHORT
            ).show();

            // Go to Login
            Intent intent = new Intent(
                    getApplicationContext(),
                    Login.class
            );
            startActivity(intent);
            finish();
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