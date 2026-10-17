package com.example.iscg7424groupassessment.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
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

public class A_MenuManagement extends AppCompatActivity {

    private final List<FoodItem> foodList = new ArrayList<>();
    private FoodAdapter adapter;
    private TextView tvEmptyMenu;

    private DatabaseReference menuRef;
    private ValueEventListener menuListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenu_management);

        RecyclerView rvMenuItems = findViewById(R.id.rv_menuItems);
        tvEmptyMenu = findViewById(R.id.tv_emptyMenu);
        CardView cvAddMenuItem = findViewById(R.id.cv_addMenuItem);
        Button btnMenuBack = findViewById(R.id.btn_menuBack);

        menuRef = FirebaseDatabase.getInstance("https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com").getReference("menu_items");

        adapter = new FoodAdapter(foodList, new FoodAdapter.OnFoodActionListener() {
            @Override
            public void onEdit(FoodItem item) {
                showFoodDialog(item);
            }

            @Override
            public void onDelete(FoodItem item) {
                confirmDelete(item);
            }
        });

        rvMenuItems.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItems.setAdapter(adapter);

        cvAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodDialog(null);
            }
        });

        btnMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadFoodItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuRef != null && menuListener != null) {
            menuRef.removeEventListener(menuListener);
        }
    }



    private void showFoodDialog(final FoodItem existing) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_food_item, null);

        final EditText etName = view.findViewById(R.id.et_foodName);
        final EditText etDescription = view.findViewById(R.id.et_foodDescription);
        final EditText etPrice = view.findViewById(R.id.et_foodPrice);


        //view.findViewById(R.id.iv_foodImagePreview).setVisibility(View.GONE);
        //view.findViewById(R.id.btn_chooseFoodImage).setVisibility(View.GONE);

        if (existing != null) {
            etName.setText(existing.getName());
            etDescription.setText(existing.getDescription());
            etPrice.setText(String.valueOf(existing.getPrice()));
        }

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
                            name, description, price, null);
                    saveFoodItem(newItem, true);
                } else {
                    existing.setName(name);
                    existing.setDescription(description);
                    existing.setPrice(price);
                    saveFoodItem(existing, false);
                }

                dialog.dismiss();
            }
        });
    }



    private void confirmDelete(final FoodItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete menu item")
                .setMessage("Delete \"" + item.getName() + "\"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFoodItem(item);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
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
                Toast.makeText(A_MenuManagement.this,
                        "Could not load menu: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        menuRef.addValueEventListener(menuListener);
    }

    private void saveFoodItem(FoodItem item, final boolean isNew) {
        menuRef.child(item.getId()).setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_MenuManagement.this,
                                isNew ? "Menu item added" : "Menu item updated",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_MenuManagement.this,
                                "Save failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteFoodItem(FoodItem item) {
        menuRef.child(item.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_MenuManagement.this,
                                "Menu item deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_MenuManagement.this,
                                "Delete failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void refreshList() {
        adapter.notifyDataSetChanged();
        tvEmptyMenu.setVisibility(foodList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}