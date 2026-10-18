package com.example.iscg7424groupassessment.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class A_BookingManagement extends AppCompatActivity {

    private static final String DB_URL = "https://mobdevgroupassessment-664a8-default-rtdb.firebaseio.com";

    private final List<TableItem> tableList = new ArrayList<>();
    private final List<TableBooking> bookingList = new ArrayList<>();
    private final List<TableBooking> dialogList = new ArrayList<>();

    private BookingTableAdapter tableAdapter;
    private BookingAdapter dialogAdapter;
    private TextView tvEmptyTables, tvUnassignedInfo, dialogEmpty;

    private TableItem currentTable;
    private DatabaseReference tableRef, bookingRef;
    private ValueEventListener tableListener, bookingListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_booking_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvTables = findViewById(R.id.rv_bookingTables);
        tvEmptyTables = findViewById(R.id.tv_emptyBookingTables);
        tvUnassignedInfo = findViewById(R.id.tv_unassignedInfo);
        Button btnBookingBack = findViewById(R.id.btn_bookingBack);

        tableRef = FirebaseDatabase.getInstance(DB_URL).getReference("tables");
        bookingRef = FirebaseDatabase.getInstance(DB_URL).getReference("bookings");

        tableAdapter = new BookingTableAdapter(tableList, bookingList, new BookingTableAdapter.OnTableClickListener() {
            @Override
            public void onTableClick(TableItem table) {
                showTableDialog(table);
            }
        });

        rvTables.setLayoutManager(new GridLayoutManager(this, 3));
        rvTables.setAdapter(tableAdapter);

        btnBookingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadTables();
        loadBookings();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(tableRef != null && tableListener != null) {
            tableRef.removeEventListener(tableListener);
        }
        if(bookingRef != null && bookingListener != null) {
            bookingRef.removeEventListener(bookingListener);
        }
    }

    private void showTableDialog(TableItem table) {
        currentTable = table;

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_table_booking, null);
        TextView tvSeats = view.findViewById(R.id.tv_dialogSeats);
        dialogEmpty = view.findViewById(R.id.tv_dialogEmpty);
        RecyclerView rvDialogBookings = view.findViewById(R.id.rv_dialogBookings);

        tvSeats.setText("Seats: " + table.getSeats());

        dialogAdapter = new BookingAdapter(dialogList, new BookingAdapter.OnBookingActionListener() {
            @Override
            public void onAssign(TableBooking booking) {
                assignBooking(booking);
            }

            @Override
            public void onConfirm(TableBooking booking) {
                confirmBooking(booking);
            }

            @Override
            public void onCancel(TableBooking booking) {
                confirmCancel(booking);
            }
        });

        rvDialogBookings.setLayoutManager(new LinearLayoutManager(this));
        rvDialogBookings.setAdapter(dialogAdapter);

        rebuildDialogList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Table No: " + table.getTableNumber())
                .setView(view)
                .setNegativeButton("Close", null)
                .create();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                currentTable = null;
                dialogAdapter = null;
            }
        });
        dialog.show();
        }

    private void rebuildDialogList() {
        if (currentTable == null || dialogAdapter == null) return;

        dialogList.clear();
        for (TableBooking b : bookingList) {
            boolean forThisTable = b.getTableNumber() == currentTable.getTableNumber();
            boolean waiting = b.getTableNumber() <= 0 && !"Cancelled".equals(b.getStatus());
            if (forThisTable || waiting) {
                dialogList.add(b);
            }
        }
        dialogAdapter.notifyDataSetChanged();
        dialogEmpty.setVisibility(dialogList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void assignBooking(TableBooking booking) {
        TableItem table = currentTable;
        if (table == null) return;

        if (booking.getGuests() > table.getSeats()) {
            Toast.makeText(this,
                    "Table No: " + table.getTableNumber() + " only seats " + table.getSeats(),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (hasConflict(booking, table.getTableNumber())) {
            Toast.makeText(this,
                    "This table already has a confirmed booking at that date and time",
                    Toast.LENGTH_LONG).show();
            return;
        }

        updateBooking(booking.getId(), "tableNumber", table.getTableNumber(),
                "Booking assigned to Table No: " + table.getTableNumber());
    }

    private void confirmBooking(TableBooking booking) {
        TableItem table = findTable(booking.getTableNumber());
        if (table != null && booking.getGuests() > table.getSeats()) {
            Toast.makeText(this,
                    "Table No: " + table.getTableNumber() + " only seats " + table.getSeats(),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (hasConflict(booking, booking.getTableNumber())) {
            Toast.makeText(this,
                    "This table already has a confirmed booking at that date and time",
                    Toast.LENGTH_LONG).show();
            return;
        }

        updateBooking(booking.getId(), "status", "Confirmed", "Booking confirmed");
    }

    private void confirmCancel(final TableBooking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel booking")
                .setMessage("Cancel the booking for " + booking.getCustomerName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateBooking(booking.getId(), "status", "Cancelled", "Booking cancelled");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean hasConflict(TableBooking booking, int tableNumber) {
        for (TableBooking other : bookingList) {
            if (!other.getId().equals(booking.getId())
                    && other.getTableNumber() == tableNumber
                    && "Confirmed".equals(other.getStatus())
                    && safe(other.getDate()).equals(safe(booking.getDate()))
                    && safe(other.getTime()).equals(safe(booking.getTime()))) {
                return true;
            }
        }
        return false;
    }

    private TableItem findTable(int tableNumber) {
        for (TableItem t : tableList) {
            if (t.getTableNumber() == tableNumber) {
                return t;
            }
        }
        return null;
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
                refreshTables();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(A_BookingManagement.this,
                        "Could not load tables: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        tableRef.addValueEventListener(tableListener);
    }

    private void loadBookings() {
        bookingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    TableBooking item = child.getValue(TableBooking.class);
                    if (item != null) {
                        item.setId(child.getKey());   // the key is always the real id
                        bookingList.add(item);
                    }
                }
                Collections.sort(bookingList, new Comparator<TableBooking>() {
                    @Override
                    public int compare(TableBooking a, TableBooking b) {
                        int byDate = safe(a.getDate()).compareTo(safe(b.getDate()));
                        if (byDate != 0) return byDate;
                        return safe(a.getTime()).compareTo(safe(b.getTime()));
                    }
                });
                refreshTables();
                rebuildDialogList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(A_BookingManagement.this,
                        "Could not load bookings: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        bookingRef.addValueEventListener(bookingListener);
    }

    // Changes only one field, so nothing else the customer side saved is overwritten
    private void updateBooking(String bookingId, String field, Object value,
                               final String successMessage) {
        bookingRef.child(bookingId).child(field).setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(A_BookingManagement.this,
                                successMessage, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_BookingManagement.this,
                                "Update failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ---------- Helpers ----------

    private void refreshTables() {
        tableAdapter.notifyDataSetChanged();
        tvEmptyTables.setVisibility(tableList.isEmpty() ? View.VISIBLE : View.GONE);

        int waiting = 0;
        for (TableBooking b : bookingList) {
            if (b.getTableNumber() <= 0 && !"Cancelled".equals(b.getStatus())) {
                waiting++;
            }
        }
        if (waiting > 0 && !tableList.isEmpty()) {
            tvUnassignedInfo.setText(waiting == 1
                    ? "1 booking request needs a table. Tap a table to assign it."
                    : waiting + " booking requests need a table. Tap a table to assign them.");
            tvUnassignedInfo.setVisibility(View.VISIBLE);
        } else {
            tvUnassignedInfo.setVisibility(View.GONE);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}