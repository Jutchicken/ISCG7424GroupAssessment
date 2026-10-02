package com.example.iscg7424groupassessment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SupplierMyProperties extends AppCompatActivity {

    RecyclerView recyclerMyProperties;
    ArrayList<Property> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_my_properties);

        recyclerMyProperties = findViewById(R.id.rv_properties);

        recyclerMyProperties.setLayoutManager(new LinearLayoutManager(this));

        loadProperties();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProperties();
    }


    private void loadProperties() {
        propertyList = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences(
                "SupplierData", MODE_PRIVATE);

        String propertyData = preferences.getString("properties", "[]");

        try {
            JSONArray properties = new JSONArray(propertyData);

            for (int i = 0; i < properties.length(); i++) {
                JSONObject property = properties.getJSONObject(i);

                String name = property.getString("name");
                String location = property.getString("location");
                String price = property.getString("price");
                String category = property.getString("category");
                String imageUri = property.getString("imageUri");


                Property newProperty =
                        new Property(
                                name,
                                location,
                                price,
                                R.drawable.house1,
                                category,
                                imageUri
                        );


                propertyList.add(newProperty);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        Property[] propertiesArray = propertyList.toArray(new Property[0]);


        PSAdapter adapter = new PSAdapter(propertiesArray);


        recyclerMyProperties.setAdapter(
                adapter
        );
    }

    public void btnSBack(View view) {
        finish();
    }
}