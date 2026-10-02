package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomerMainPage extends AppCompatActivity {

    RecyclerView recyclerProperties;
    Button btnCApartment, btnCHouse, btnCStudio, btnCAll;
    EditText etSearch;
    CardView cvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /* Dummy Data */

        Property[] properties = {
                new Property(
                        "Modern 2 Bedroom House",
                        "Auckland",
                        "$550 / week",
                        R.drawable.house1,
                        "House"
                ),

                new Property(
                        "Modern Apartment",
                        "Newmarket",
                        "$620 / week",
                        R.drawable.house2,
                        "Apartment"
                ),

                new Property(
                        "Cozy Studio",
                        "Mount Eden",
                        "$430 / week",
                        R.drawable.house3,
                        "Studio"
                )
        };


        /* Visualize Cardview */

        recyclerProperties = findViewById(R.id.recyclerProperties);
        PCAdapter adapter = new PCAdapter(properties);
        recyclerProperties.setLayoutManager(
                new LinearLayoutManager(this)
        );
        recyclerProperties.setAdapter(adapter);


        /* Update Cardview by Category */

        btnCApartment = findViewById(R.id.btn_cApartment);
        btnCHouse = findViewById(R.id.btn_cHouse);
        btnCStudio = findViewById(R.id.btn_cStudio);
        btnCAll = findViewById(R.id.btn_cAll);

        // Show Apartment only
        btnCApartment.setOnClickListener(v -> {
            Property[] filtered = filterProperties(properties, "Apartment");
            adapter.setProperties(filtered);
        });

        // Show House only
        btnCHouse.setOnClickListener(v -> {
            Property[] filtered = filterProperties(properties, "House");
            adapter.setProperties(filtered);
        });

        // Show Studio only
        btnCStudio.setOnClickListener(v -> {
            Property[] filtered = filterProperties(properties, "Studio");
            adapter.setProperties(filtered);
        });

        // Show all types
        btnCAll.setOnClickListener(v -> {
            adapter.setProperties(properties);
        });


        /* Update Cardview by Search Result */

        etSearch = findViewById(R.id.et_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.searchProperties(properties, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cvProfile = findViewById(R.id.cv_cProfile);

        cvProfile.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),
                    "Going to Profile...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),
                    CustomerProfile.class);
            startActivity(intent);
        });
    }


    /* Filter Property by category */
    private Property[] filterProperties(Property[] properties, String category) {
        int count = 0;

        for (Property property : properties) {
            if (property.getCategory().equals(category)) {
                count++;
            }
        }

        Property[] result = new Property[count];
        int index = 0;

        for (Property property : properties) {
            if (property.getCategory().equals(category)) {
                result[index] = property;
                index++;
            }
        }

        return result;
    }
}