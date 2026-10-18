package com.example.iscg7424groupassessment.admin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class BookingHolder extends RecyclerView.ViewHolder {

    TextView tvCustomer, tvDateTime, tvGuests, tvTable, tvStatus;
    Button btnAssign, btnConfirm, btnCancel;

    public BookingHolder(@NonNull View itemView) {
        super(itemView);
        tvCustomer = itemView.findViewById(R.id.tv_bookingCustomer);
        tvDateTime = itemView.findViewById(R.id.tv_bookingDateTime);
        tvGuests = itemView.findViewById(R.id.tv_bookingGuests);
        tvTable = itemView.findViewById(R.id.tv_bookingTable);
        tvStatus = itemView.findViewById(R.id.tv_bookingStatus);

        btnAssign = itemView.findViewById(R.id.btn_assignBooking);
        btnConfirm = itemView.findViewById(R.id.btn_confirmBooking);
        btnCancel = itemView.findViewById(R.id.btn_cancelBooking);
    }
}
