package com.example.iscg7424groupassessment.customer;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.admin.FoodAdapter;
import com.example.iscg7424groupassessment.admin.FoodImage;
import com.example.iscg7424groupassessment.admin.FoodItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class C_Menu extends AppCompatActivity {

    private final List<FoodItem> foodList = new ArrayList<>();
    private C_FoodAdapter adapter;
    private TextView tvEmptyMenu;

    private DatabaseReference menuRef;
    private ValueEventListener menuListener;

    private ImageView dialogPreview;
    private String pendingImage;

    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri == null) return;
                            String encoded = FoodImage.uriToBase64(C_Menu.this, uri);
                            if (encoded == null) {
                                Toast.makeText(C_Menu.this,
                                        "Could not load that image",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            pendingImage = encoded;
                            if (dialogPreview != null) {
                                dialogPreview.setImageBitmap(FoodImage.base64ToBitmap(encoded));
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        RecyclerView rvMenuItems = findViewById(R.id.rv_customerMenuItems);
        tvEmptyMenu = findViewById(R.id.tv_customerEmptyMenu);
        Button btnMenuBack = findViewById(R.id.btn_menuBack);

        menuRef = FirebaseDatabase.getInstance("https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com")
                .getReference("menu_items");

        adapter = new C_FoodAdapter(foodList, new C_FoodAdapter.OnFoodActionListener() {
            @Override
            public void onEdit(FoodItem item) {
                showFoodDialog(item);
            }

            @Override
            public void onDelete(FoodItem item) {
            }
        });

        rvMenuItems.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItems.setAdapter(adapter);



        btnMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadFoodItems();
    }


    private void showFoodDialog(final FoodItem existing) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_food_item, null);

        dialogPreview = view.findViewById(R.id.iv_foodImagePreview);
        Button btnChooseImage = view.findViewById(R.id.btn_chooseFoodImage);
        final EditText etName = view.findViewById(R.id.et_foodName);
        final EditText etDescription = view.findViewById(R.id.et_foodDescription);
        final EditText etPrice = view.findViewById(R.id.et_foodPrice);

        pendingImage = null;

        if (existing != null) {
            etName.setText(existing.getName());
            etDescription.setText(existing.getDescription());
            etPrice.setText(String.valueOf(existing.getPrice()));

            pendingImage = existing.getImage();
            if (pendingImage != null && !pendingImage.isEmpty()) {
                Bitmap bitmap = FoodImage.base64ToBitmap(pendingImage);
                if (bitmap != null) {
                    dialogPreview.setImageBitmap(bitmap);
                }
            }
        }

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.launch(new String[]{"image/*"});
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(existing == null ? "Add Menu Item" : "Edit Menu Item")
                .setView(view)
                .setPositiveButton("Save", null)   // set below so we can validate first
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String priceText = etPrice.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Name is required");
                    return;
                }
                if (priceText.isEmpty()) {
                    etPrice.setError("Price is required");
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    etPrice.setError("Enter a valid price");
                    return;
                }

                if (existing == null) {
                    FoodItem newItem = new FoodItem(
                            UUID.randomUUID().toString(),
                            name, description, price, pendingImage);

                } else {
                    existing.setName(name);
                    existing.setDescription(description);
                    existing.setPrice(price);
                    existing.setImage(pendingImage);

                }

                dialog.dismiss();
            }
        });
    }



    private void loadFoodItems() {
        menuListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FoodItem item = child.getValue(FoodItem.class);
                    if (item != null) {
                        foodList.add(item);
                    }
                }
                refreshList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(C_Menu.this,
                        "Could not load menu: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        menuRef.addValueEventListener(menuListener);
    }





    private void refreshList() {
        adapter.notifyDataSetChanged();
        tvEmptyMenu.setVisibility(foodList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}