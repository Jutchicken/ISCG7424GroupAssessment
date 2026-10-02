package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PropertyDetailPage extends AppCompatActivity {

    TextView tvPropertyName, tvLocation, tvPrice, tvCategory;
    ImageView ivProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_detail_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // fetch data
        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        String category = getIntent().getStringExtra("category");
        int image = getIntent().getIntExtra("image", 0);

        tvPropertyName = findViewById(R.id.tv_cPDPropertyName);
        tvLocation = findViewById(R.id.tv_cPDLocation);
        tvPrice = findViewById(R.id.tv_cPDPrice);
        tvCategory = findViewById(R.id.tv_cPDCategory);
        ivProperty = findViewById(R.id.iv_CPDImage);

        tvPropertyName.setText(name);
        tvLocation.setText("Location: " + location);
        tvPrice.setText(price);
        tvCategory.setText("Category: " + category);
        ivProperty.setImageResource(image);
    }

    // Return to Main Page
    public void btnPDBack(View view) {
        Toast.makeText(getApplicationContext(),
                "Going Back to Main Page",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                CustomerMainPage.class);
        startActivity(intent);
        finish();
    }

    // Book Property
    public void btnPDBook(View view) {
        Toast.makeText(getApplicationContext(),
                "Property booked successfully!",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),
                CustomerProfile.class);
        startActivity(intent);
        finish();
    }
}