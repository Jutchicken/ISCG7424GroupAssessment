package com.example.iscg7424groupassessment.admin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.List;

public class BookingTableAdapter extends RecyclerView.Adapter<BookingTableHolder> {

    public BookingTableAdapter(List<TableItem> tables, List<TableBooking> bookings, OnTableClickListener listener) {
        this.tables = tables;
        this.bookings = bookings;
        this.listener = listener;
    }

    public interface OnTableClickListener {
        void onTableClick(TableItem table);
    }

    private static final String COLOR_FREE = "#E0E0E0";
    private static final String COLOR_PENDING = "#FFE874";
    private static final String COLOR_BOOKED = "#A5D6A7";

    private final List<TableItem> tables;
    private final List<TableBooking> bookings;
    private final OnTableClickListener listener;

    @NonNull
    @Override
    public BookingTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_table_item, parent, false);
        return new BookingTableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingTableHolder holder, int position) {
        final TableItem table = tables.get(position);

        int pending = 0;
        int confirmed = 0;
        for (TableBooking b : bookings) {
            if (b.getTableNumber() == table.getTableNumber()) {
                if ("Pending".equals(b.getStatus())) {
                    pending++;
                } else if ("Confirmed".equals(b.getStatus())) {
                    confirmed++;
                }
            }
        }
    holder.tvName.setText("Table " + table.getTableNumber());
    if (pending > 0) {
        holder.tvInfo.setText(pending + " pending");
        holder.cvTable.setCardBackgroundColor(Color.parseColor(COLOR_PENDING));
    } else if (confirmed > 0) {
        holder.tvInfo.setText(confirmed + " booked");
        holder.cvTable.setCardBackgroundColor(Color.parseColor(COLOR_BOOKED));
    } else {
        holder.tvInfo.setText("Free");
        holder.cvTable.setCardBackgroundColor(Color.parseColor(COLOR_FREE));
    }

    holder.cvTable.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onTableClick(table);
        }
    });
}


    @Override
    public int getItemCount() {
        return tables.size();
    }
}
