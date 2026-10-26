package com.example.iscg7424groupassessment.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.List;

public class StaffBookingAdapter
        extends RecyclerView.Adapter<StaffBookingHolder> {

    public interface OnBookingActionListener {
        void onConfirm(StaffTableBooking booking);

        void onCancel(StaffTableBooking booking);
    }

    private final List<StaffTableBooking> items;
    private final OnBookingActionListener listener;

    public StaffBookingAdapter(
            List<StaffTableBooking> items,
            OnBookingActionListener listener
    ) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StaffBookingHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.staff_booking_item,
                        parent,
                        false
                );

        return new StaffBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull StaffBookingHolder holder,
            int position
    ) {
        StaffTableBooking booking = items.get(position);

        holder.tvCustomer.setText(
                "Customer: " + booking.getCustomerName()
        );

        holder.tvDateTime.setText(
                safe(booking.getDate()) + " "
                        + safe(booking.getTime())
        );

        holder.tvGuests.setText(
                "Guests: " + booking.getGuests()
        );

        if (booking.getTableNumber() > 0) {
            holder.tvTable.setText(
                    "Table No: " + booking.getTableNumber()
            );
        } else {
            holder.tvTable.setText("Table: Not assigned yet");
        }

        holder.tvStatus.setText(
                "Status: " + booking.getStatus()
        );

        boolean pending =
                "Pending".equalsIgnoreCase(booking.getStatus());

        boolean confirmed =
                "Confirmed".equalsIgnoreCase(booking.getStatus());

        holder.btnConfirm.setVisibility(
                pending ? View.VISIBLE : View.GONE
        );

        holder.btnCancel.setVisibility(
                (pending || confirmed) ? View.VISIBLE : View.GONE
        );

        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm(booking);
            }
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}