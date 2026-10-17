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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class A_TableManagement extends AppCompatActivity {

    private final List<TableItem> tableList = new ArrayList<>();
    private TableAdapter adapter;
    private TextView tvEmptyTables;

    private DatabaseReference tableRef;
    private ValueEventListener tableListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_table_management);

        RecyclerView rvTables = findViewById(R.id.rv_tables);
        tvEmptyTables = findViewById(R.id.tv_emptyTables);
        CardView cvAddTable = findViewById(R.id.cv_addTable);
        Button btnTableBack = findViewById(R.id.btn_tableBack);

        tableRef = FirebaseDatabase.getInstance("https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com").getReference("tables");

        adapter = new TableAdapter(tableList, new TableAdapter.OnTableActionListener() {
            @Override
            public void onEdit(TableItem item) {
                showTableDialog(item);
            }

            @Override
            public void onDelete(TableItem item) {
                confirmDelete(item);
            }
        });

        rvTables.setLayoutManager(new LinearLayoutManager(this));
        rvTables.setAdapter(adapter);

        cvAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTableDialog(null);
            }
        });

        btnTableBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadTables();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tableRef != null && tableListener != null) {
            tableRef.removeEventListener(tableListener);
        }
    }


    private void showTableDialog(final TableItem existing) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_table, null);

        final EditText etTableNumber = view.findViewById(R.id.et_tableNumber);
        final EditText etTableSeats = view.findViewById(R.id.et_tableSeats);

        if (existing != null) {
            etTableNumber.setText(String.valueOf(existing.getTableNumber()));
            etTableSeats.setText(String.valueOf(existing.getSeats()));
        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(existing == null ? "Add Table" : "Edit Table")
                .setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberText = etTableNumber.getText().toString().trim();
                String seatsText = etTableSeats.getText().toString().trim();

                if (numberText.isEmpty()) {
                    etTableNumber.setError("Table number is required");
                    return;
                }
                if (seatsText.isEmpty()) {
                    etTableSeats.setError("Seats is required");
                    return;
                }

                int number;
                int seats;
                try {
                    number = Integer.parseInt(numberText);
                    seats = Integer.parseInt(seatsText);
                } catch (NumberFormatException e) {
                    etTableSeats.setError("Enter whole numbers only");
                    return;
                }

                if (number <= 0) {
                    etTableNumber.setError("Table number must be more than 0");
                    return;
                }
                if (seats <= 0) {
                    etTableSeats.setError("Seats must be more than 0");
                    return;
                }

                String ignoreId = existing == null ? null : existing.getId();
                if (tableNumberExists(number, ignoreId)) {
                    etTableNumber.setError("This table number already exists");
                    return;
                }

                if (existing == null) {
                    TableItem newItem = new TableItem(
                            UUID.randomUUID().toString(), number, seats);
                    saveTable(newItem, true);
                } else {
                    existing.setTableNumber(number);
                    existing.setSeats(seats);
                    saveTable(existing, false);
                }

                dialog.dismiss();
            }
        });
    }

    private boolean tableNumberExists(int number, String ignoreId) {
        for (TableItem t : tableList) {
            if (t.getTableNumber() == number
                    && (ignoreId == null || !ignoreId.equals(t.getId()))) {
                return true;
            }
        }
        return false;
    }

    private void confirmDelete(final TableItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete table")
                .setMessage("Delete Table No: " + item.getTableNumber() + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTable(item);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void loadTables() {
        tableListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    TableItem item = child.getValue(TableItem.class);
                    if (item != null) {
                        tableList.add(item);
                    }
                }
                Collections.sort(tableList, new Comparator<TableItem>() {
                    @Override
                    public int compare(TableItem a, TableItem b) {
                        return Integer.compare(a.getTableNumber(), b.getTableNumber());
                    }
                });
                refreshList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(A_TableManagement.this,
                        "Could not load tables: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        tableRef.addValueEventListener(tableListener);
    }

    private void saveTable(TableItem item, final boolean isNew) {
        tableRef.child(item.getId()).setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_TableManagement.this,
                                isNew ? "Table added" : "Table updated",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_TableManagement.this,
                                "Save failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteTable(TableItem item) {
        tableRef.child(item.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_TableManagement.this,
                                "Table deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_TableManagement.this,
                                "Delete failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void refreshList() {
        adapter.notifyDataSetChanged();
        tvEmptyTables.setVisibility(tableList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}