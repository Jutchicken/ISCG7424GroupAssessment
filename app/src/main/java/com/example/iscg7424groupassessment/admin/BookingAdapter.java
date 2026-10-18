package com.example.iscg7424groupassessment.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingHolder> {

    public BookingAdapter(List<TableBooking> items, OnBookingActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public interface OnBookingActionListener {
        void onAssign(TableBooking booking);
        void onConfirm(TableBooking booking);
        void onCancel(TableBooking booking);
    }

    private final List<TableBooking> items;
    private final OnBookingActionListener listener;

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
        final TableBooking b = items.get(position);

        boolean unassigned = b.getTableNumber() <= 0;
        boolean cancelled = "Cancelled".equals(b.getStatus());
        boolean confirmed = "Confirmed".equals(b.getStatus());

        holder.tvCustomer.setText("Customer: " + b.getCustomerName());
        holder.tvDateTime.setText(safe(b.getDate()) + " " + safe(b.getTime()));
        holder.tvGuests.setText("Guests: " + b.getGuests());
        holder.tvTable.setText(unassigned ? "Table: Not assigned yet" : "Table No: " + b.getTableNumber());
        holder.tvStatus.setText("Status: " + b.getStatus());

        holder.btnAssign.setVisibility(unassigned && !cancelled ? View.VISIBLE : View.GONE);
        holder.btnConfirm.setVisibility(!unassigned && !confirmed && !cancelled ? View.VISIBLE : View.GONE);
        holder.btnCancel.setVisibility(cancelled ? View.GONE : View.VISIBLE);

        holder.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAssign(b);
            }
        });

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm(b);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel(b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

}
