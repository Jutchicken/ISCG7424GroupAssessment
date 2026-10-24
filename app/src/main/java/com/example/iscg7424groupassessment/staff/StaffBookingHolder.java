package com.example.iscg7424groupassessment.staff;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class StaffBookingHolder extends RecyclerView.ViewHolder {

    TextView tvCustomer;
    TextView tvDateTime;
    TextView tvGuests;
    TextView tvTable;
    TextView tvStatus;

    Button btnConfirm;
    Button btnCancel;

    public StaffBookingHolder(@NonNull View itemView) {
        super(itemView);

        tvCustomer = itemView.findViewById(
                R.id.tv_staffBookingCustomer
        );

        tvDateTime = itemView.findViewById(
                R.id.tv_staffBookingDateTime
        );

        tvGuests = itemView.findViewById(
                R.id.tv_staffBookingGuests
        );

        tvTable = itemView.findViewById(
                R.id.tv_staffBookingTable
        );

        tvStatus = itemView.findViewById(
                R.id.tv_staffBookingStatus
        );

        btnConfirm = itemView.findViewById(
                R.id.btn_staffConfirmBooking
        );

        btnCancel = itemView.findViewById(
                R.id.btn_staffCancelBooking
        );
    }
}