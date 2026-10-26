package com.example.iscg7424groupassessment.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.staff.StaffTableBooking;

import java.util.List;

public class C_BookingAdapter
        extends RecyclerView.Adapter<C_BookingHolder> {


    private final List<C_TableBooking> items;

    public C_BookingAdapter(
            List<C_TableBooking> items
    ) {
        this.items = items;
    }

    @NonNull
    @Override
    public C_BookingHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.customer_booking_item,
                        parent,
                        false
                );

        return new C_BookingHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull C_BookingHolder holder,
            int position
    ) {
        C_TableBooking booking = items.get(position);

        holder.tvCustomer.setText(
                "Customer: " + booking.getCustomerEmail()
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




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}