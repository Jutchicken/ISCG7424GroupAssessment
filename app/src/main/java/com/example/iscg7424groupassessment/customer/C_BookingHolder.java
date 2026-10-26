package com.example.iscg7424groupassessment.customer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class C_BookingHolder extends RecyclerView.ViewHolder {

    TextView tvCustomer;
    TextView tvDateTime;
    TextView tvGuests;
    TextView tvTable;
    TextView tvStatus;


    public C_BookingHolder(@NonNull View itemView) {
        super(itemView);

        tvCustomer = itemView.findViewById(
                R.id.tv_customerBookingCustomer
        );

        tvDateTime = itemView.findViewById(
                R.id.tv_customerBookingDateTime
        );

        tvGuests = itemView.findViewById(
                R.id.tv_customerBookingGuests
        );

        tvTable = itemView.findViewById(
                R.id.tv_customerBookingTable
        );

        tvStatus = itemView.findViewById(
                R.id.tv_customerBookingStatus
        );

    }
}