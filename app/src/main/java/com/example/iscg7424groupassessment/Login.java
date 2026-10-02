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

public class Login extends AppCompatActivity {

    EditText etID, etPassword;

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
        etID = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);

    }

    public void btnLogin(View view) {
        String id = etID.getText().toString().toLowerCase();
        String password = etPassword.getText().toString().toLowerCase();
        if (id.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Please enter ID or Password",
                    Toast.LENGTH_SHORT).show();

            etID.setText("");
            etPassword.setText("");
        }
        else if (id.equals("admin") && password.equals("admin")){
            Toast.makeText(getApplicationContext(),
                    "Welcome, " + id,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),
                    A_MainPage.class);
            startActivity(intent);
            finish();
        }
        else if (id.equals("supplier") && password.equals("supplier")){
            Toast.makeText(getApplicationContext(),
                    "Welcome, " + id,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),
                    S_MainPage.class);
            startActivity(intent);
            finish();

        }
        else if (id.equals("customer") && password.equals("customer")){
            Toast.makeText(getApplicationContext(),
                    "Welcome, " + id,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),
                    CustomerMainPage.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Wrong ID or Password, Please try again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void btnRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),
                Register.class);
        startActivity(intent);
    }
}