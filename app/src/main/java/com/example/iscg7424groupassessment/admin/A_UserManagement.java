package com.example.iscg7424groupassessment.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.iscg7424groupassessment.Login;
import com.example.iscg7424groupassessment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class A_UserManagement extends AppCompatActivity {
    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";
    private static final String ROLE_STAFF = "Staff";
    private static final String SECONDARY_APP_NAME = "PasswordChanger";
    private static final int MIN_PASSWORD_LENGTH = 6;
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
                showUserDialog(user);
            }

            @Override
            public void onDelete(UserItem user) {
                confirmDelete(user);
            }
        });

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        cvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog(null);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        loadUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userRef != null && userListener != null) {
            userRef.removeEventListener(userListener);
        }
    }

    private void showUserDialog(final UserItem existing) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_user, null);
        final boolean isNew = existing == null;

        final EditText etUsername = view.findViewById(R.id.et_userName);
        final EditText etFullName = view.findViewById(R.id.et_userFullName);
        final EditText etEmail = view.findViewById(R.id.et_userEmail);
        final EditText etPhone = view.findViewById(R.id.et_userPhone);
        final EditText etPassword = view.findViewById(R.id.et_userPassword);
        final EditText etConfirmPassword = view.findViewById(R.id.et_userConfirmPassword);

        if (!isNew) {
            etUsername.setText(existing.getUsername());
            etFullName.setText(existing.getFullName());
            etEmail.setText(existing.getEmail());
            etPhone.setText(existing.getPhone());

            etEmail.setEnabled(false);
            etPassword.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(isNew ? "Add Staff" : "Edit Staff")
                .setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null);

        final boolean canReset = !isNew && !safe(existing.getEmail()).isEmpty();
        if (canReset) {
            builder.setNeutralButton("Change Password", null);
        }

        final AlertDialog dialog = builder.create();
        dialog.show();

        if (canReset) {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangePasswordDialog(existing, dialog);
                }
            });
        }

        final Button btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim().toLowerCase();
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (username.isEmpty()) {
                    etUsername.setError("Username required");
                    return;
                }
                if (username.contains(" ")) {
                    etUsername.setError("No spaces allowed");
                    return;
                }
                if (fullName.isEmpty()) {
                    etFullName.setError("Full name required");
                    return;
                }

                String ignoreId = isNew ? null : existing.getId();
                if (loginIdExists(username, ignoreId)) {
                    etUsername.setError("This Login ID already exists");
                    return;
                }

                if (isNew) {
                    if (email.isEmpty() || phone.isEmpty()
                            || password.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(A_UserManagement.this,
                                "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        etEmail.setError("Enter valid email");
                        return;
                    }
                    if (!password.equals(confirmPassword)) {
                        etConfirmPassword.setError("Passwords do not match");
                        return;
                    }
                    if (password.length() < MIN_PASSWORD_LENGTH) {
                        etPassword.setError("Password must be at least "
                                + MIN_PASSWORD_LENGTH + " characters");
                        return;
                    }

                    createStaffAccount(username, fullName, email, phone, password,
                            dialog, btnSave);
                } else {
                    existing.setUsername(username);
                    existing.setFullName(fullName);
                    existing.setPhone(phone);
                    existing.setRole(ROLE_STAFF);
                    saveUser(existing, false);
                    dialog.dismiss();
                }
            }
        });
    }

    private void createStaffAccount(final String username, final String fullName,
                                    final String email, final String phone,
                                    String password, final AlertDialog dialog,
                                    final Button btnSave) {
        btnSave.setEnabled(false);
        stopListeningForUsers();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final FirebaseUser firebaseUser = task.getResult().getUser();

                        if (firebaseUser == null) {
                            btnSave.setEnabled(true);
                            loadUsers();
                            return;
                        }

                        String uid = firebaseUser.getUid();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", uid);
                        userData.put("fullName", fullName);
                        userData.put("username", username);
                        userData.put("email", email);
                        userData.put("phone", phone);
                        userData.put("role", ROLE_STAFF);

                        userRef.child(uid).setValue(userData)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getApplicationContext(),
                                            "Staff added. Please log in again.",
                                            Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                    goToLogin();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(),
                                            "Database Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    firebaseUser.delete().addOnCompleteListener(t -> {
                                        mAuth.signOut();
                                        goToLogin();
                                    });
                                });
                    } else {
                        btnSave.setEnabled(true);
                        loadUsers();
                        Toast.makeText(getApplicationContext(),
                                "Unable to add staff: " +
                                        (task.getException() != null
                                                ? task.getException().getMessage()
                                                : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showChangePasswordDialog(final UserItem staff, final AlertDialog editDialog) {
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(pad, pad / 3, pad, 0);

        final EditText etCurrent = passwordField("Current password");
        final EditText etNew = passwordField("New password");
        final EditText etConfirm = passwordField("Confirm new password");
        layout.addView(etCurrent);
        layout.addView(etNew);
        layout.addView(etConfirm);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(layout)
                .setPositiveButton("Change", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        final Button btnChange = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = etCurrent.getText().toString();
                String newPassword = etNew.getText().toString();
                String confirm = etConfirm.getText().toString();

                if (current.isEmpty() || newPassword.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(A_UserManagement.this,
                            "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirm)) {
                    etConfirm.setError("Passwords do not match");
                    return;
                }
                if (newPassword.length() < MIN_PASSWORD_LENGTH) {
                    etNew.setError("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
                    return;
                }

                changeStaffPassword(staff.getEmail(), current, newPassword,
                        btnChange, dialog, editDialog);
            }
        });
    }

    private EditText passwordField(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return et;
    }

    private FirebaseAuth getSecondaryAuth() {
        FirebaseApp secondary;
        try {
            secondary = FirebaseApp.getInstance(SECONDARY_APP_NAME);
        } catch (IllegalStateException e) {
            secondary = FirebaseApp.initializeApp(this,
                    FirebaseApp.getInstance().getOptions(), SECONDARY_APP_NAME);
        }
        return FirebaseAuth.getInstance(secondary);
    }

    private void changeStaffPassword(String email, String currentPassword,
                                     final String newPassword, final Button btnChange,
                                     final AlertDialog changeDialog,
                                     final AlertDialog editDialog) {
        btnChange.setEnabled(false);

        final FirebaseAuth staffAuth = getSecondaryAuth();

        staffAuth.signInWithEmailAndPassword(email, currentPassword)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        btnChange.setEnabled(true);
                        Toast.makeText(A_UserManagement.this,
                                "Current password is incorrect",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser staffUser = task.getResult().getUser();
                    if (staffUser == null) {
                        btnChange.setEnabled(true);
                        return;
                    }

                    staffUser.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        staffAuth.signOut();

                        if (updateTask.isSuccessful()) {
                            Toast.makeText(A_UserManagement.this,
                                    "Password changed",
                                    Toast.LENGTH_SHORT).show();
                            changeDialog.dismiss();
                            editDialog.dismiss();
                        } else {
                            btnChange.setEnabled(true);
                            Toast.makeText(A_UserManagement.this,
                                    "Could not change password: " +
                                            (updateTask.getException() != null
                                                    ? updateTask.getException().getMessage()
                                                    : "Unknown error"),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
    }

    private void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void stopListeningForUsers() {
        if (userRef != null && userListener != null) {
            userRef.removeEventListener(userListener);
        }
    }

    private boolean loginIdExists(String loginId, String ignoreId) {
        for (UserItem u : allUsers) {
            if (u.getUsername() != null
                    && u.getUsername().equalsIgnoreCase(loginId)
                    && (ignoreId == null || !ignoreId.equals(u.getId()))) {
                return true;
            }
        }
        return false;
    }

    private void confirmDelete(final UserItem user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete staff")
                .setMessage("Delete " + safe(user.getFullName()) + "?")
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
                        item.setId(child.getKey());
                        allUsers.add(item);
                    }
                }
                Collections.sort(allUsers, new Comparator<UserItem>() {
                    @Override
                    public int compare(UserItem a, UserItem b) {
                        return String.CASE_INSENSITIVE_ORDER.compare(
                                safe(a.getFullName()), safe(b.getFullName()));
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
        Task<Void> task;
        if (isNew) {
            task = userRef.child(user.getId()).setValue(user);
        } else {
            Map<String, Object> changes = new HashMap<>();
            changes.put("username", user.getUsername());
            changes.put("fullName", user.getFullName());
            changes.put("email", user.getEmail());
            changes.put("phone", user.getPhone());
            changes.put("role", user.getRole());
            task = userRef.child(user.getId()).updateChildren(changes);
        }

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(A_UserManagement.this,
                        isNew ? "Staff added" : "Staff updated",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
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