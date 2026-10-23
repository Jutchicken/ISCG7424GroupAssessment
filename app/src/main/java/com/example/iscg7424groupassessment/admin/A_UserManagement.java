package com.example.iscg7424groupassessment.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.CustomerMainPage;
import com.example.iscg7424groupassessment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class A_UserManagement extends AppCompatActivity {
    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";
    private static final String ROLE_STAFF = "Staff";
    private final List<UserItem> allUsers = new ArrayList<>();
    private final List<UserItem> staffList = new ArrayList<>();

    private UserAdapter adapter;
    private TextView tvEmptyUsers;

    private DatabaseReference userRef;
    private ValueEventListener userListener;

    CardView cvAddUser;
    RecyclerView rvUsers;
    Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvUsers = findViewById(R.id.rv_users);
        tvEmptyUsers = findViewById(R.id.tv_emptyUsers);
        cvAddUser = findViewById(R.id.cv_addUser);
        btnBack = findViewById(R.id.btn_userBack);

        userRef = FirebaseDatabase.getInstance(DB_URL).getReference("users");

        adapter = new UserAdapter(staffList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(UserItem user) {
                //showUserDialog(user);
            }

            @Override
            public void onDelete(UserItem user) {
                //confirmDelete(user);
            }
        });

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        cvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showUserDialog(null);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //loadUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userRef != null && userListener != null) {
            userRef.removeEventListener(userListener);
        }
    }

    private void showUserDialog(final UserItem existing){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_user, null);

        final EditText etLoginId = view.findViewById(R.id.et_userLoginId);
        final EditText etName = view.findViewById(R.id.et_userName);
        final EditText etEmail = view.findViewById(R.id.et_userEmail);
        final EditText etPhone = view.findViewById(R.id.et_userPhone);

        if(existing != null) {
            etLoginId.setText(existing.getLoginId());
            etLoginId.setEnabled(false);

            etName.setText(existing.getName());
            etEmail.setText(existing.getEmail());
            etPhone.setText(existing.getPhone());
        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(existing == null ? "Add Staff" : "Edit Staff")
                .setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginId = etLoginId.getText().toString().trim().toLowerCase();
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if(loginId.isEmpty()) {
                    etLoginId.setError("Login ID required");
                    return;
                }
                if (loginId.contains(" ")) {
                    etLoginId.setError("No spaces allowed");
                }
                if(name.isEmpty()) {
                    etName.setError("Name required");
                    return;
                }
                if(!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter valid email");
                    return;
                }

                String ignoreId = existing == null ? null : existing.getId();
                if(loginIdExists(loginId, ignoreId)) {
                    etLoginId.setError("This Login ID already exists");
                    return;
                }
                if(existing == null) {
                    UserItem newStaff = new UserItem(
                            UUID.randomUUID().toString(),
                            loginId, name, email, phone, ROLE_STAFF);

                    saveUser(newStaff, true);
                } else {
                    existing.setName(name);
                    existing.setEmail(email);
                    existing.setPhone(phone);
                    existing.setRole(ROLE_STAFF);
                    saveUser(existing, false);
                }
                dialog.dismiss();
            }
        });
    }
    private boolean loginIdExists(String loginId, String ignoreId) {
        for (UserItem u : allUsers) {
            if (u.getLoginId() != null
                    && u.getLoginId().equalsIgnoreCase(loginId)
                    && (ignoreId == null || !ignoreId.equals(u.getId()))) {
                return true;
            }
        }
        return false;
    }

    private void confirmDelete(final UserItem user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete staff")
                .setMessage("Delete " + safe(user.getName()) + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(user);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void loadUsers() {
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUsers.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserItem item = child.getValue(UserItem.class);
                    if (item != null) {
                        item.setId(child.getKey());   // the key is always the real id
                        allUsers.add(item);
                    }
                }
                Collections.sort(allUsers, new Comparator<UserItem>() {
                    @Override
                    public int compare(UserItem a, UserItem b) {
                        return String.CASE_INSENSITIVE_ORDER.compare(
                                safe(a.getName()), safe(b.getName()));
                    }
                });
                applyFilter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(A_UserManagement.this,
                        "Could not load staff: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        userRef.addValueEventListener(userListener);
    }

    private void saveUser(UserItem user, final boolean isNew) {
        userRef.child(user.getId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_UserManagement.this,
                                isNew ? "Staff added" : "Staff updated",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_UserManagement.this,
                                "Save failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteUser(UserItem user) {
        userRef.child(user.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_UserManagement.this,
                                "Staff deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_UserManagement.this,
                                "Delete failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    // Keeps only the staff for the list
    private void applyFilter() {
        staffList.clear();
        for (UserItem u : allUsers) {
            if (ROLE_STAFF.equals(u.getRole())) {
                staffList.add(u);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmptyUsers.setVisibility(staffList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}