package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class S_AddProperty extends AppCompatActivity {

    EditText etPropertyName, etLocation, etPrice, etDescription;
    ImageView ivPropertyImage;
    Spinner spinnerCategory;
    Button btnBack, btnAddProperty, btnChooseImage;
    Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_add_property);

        ivPropertyImage = findViewById(R.id.iv_pPropertyImage);
        etPropertyName = findViewById(R.id.et_pPropertyName);
        etLocation = findViewById(R.id.et_pLocation);
        etPrice = findViewById(R.id.et_pPrice);
        etDescription = findViewById(R.id.et_pDescription);

        spinnerCategory = findViewById(R.id.spin_pCategory);

        btnBack = findViewById(R.id.btnBack);
        btnAddProperty = findViewById(R.id.btn_addProperty);
        btnChooseImage = findViewById(R.id.btn_pChooseImage);

        btnChooseImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);

            intent.setType("image/*");

            startActivityForResult(intent, 100);
        });

        // Category list

        String[] categories = {
                "House",
                "Apartment",
                "Room",
                "Studio"
        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(categoryAdapter);


        // Back button

        btnBack.setOnClickListener(v -> {
            finish();
        });


        // Add Property button
        btnAddProperty.setOnClickListener(v -> {
            String name = etPropertyName.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty()) {
                etPropertyName.setError("Enter property name");
                return;
            }

            if (location.isEmpty()) {
                etLocation.setError("Enter location");
                return;
            }

            if (price.isEmpty()) {
                etPrice.setError("Enter price");
                return;
            }

            if (description.isEmpty()) {
                etDescription.setError("Enter description");
                return;
            }

            SharedPreferences preferences = getSharedPreferences(
                    "SupplierData", MODE_PRIVATE);


            int propertyCount = preferences.getInt(
                    "propertyCount", 0);
            propertyCount++;


            // Save new count
            String propertyData =
                    preferences.getString("properties", "[]");


            try {
                JSONArray properties = new JSONArray(propertyData);

                JSONObject property = new JSONObject();

                property.put("name", name);
                property.put("location", location);
                property.put("price", price);
                property.put("category", category);
                property.put("description", description);


                if (selectedImageUri != null) {
                    property.put("imageUri",
                            selectedImageUri.toString());

                }
                else {
                    property.put(
                            "imageUri",
                            ""
                    );
                }

                properties.put(property);

                preferences.edit()
                        .putInt("propertyCount", propertyCount)
                        .putString("properties", properties.toString()).apply();

                Toast.makeText(getApplicationContext(),
                        "Property added successfully!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
            catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Failed to add property",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == 100
                && resultCode == RESULT_OK
                && data != null) {

            Uri imageUri = data.getData();

            ivPropertyImage.setImageURI(imageUri);
        }
    }
}