package com.example.iscg7424groupassessment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BRHolder extends RecyclerView.ViewHolder{

    TextView tvRequestProperty, tvCustomer, tvBookingDate;
    Button btnAccept, btnReject;

    public BRHolder(@NonNull View itemView) {
        super(itemView);

        tvRequestProperty = itemView.findViewById(R.id.tv_requestProperty);
        tvCustomer = itemView.findViewById(R.id.tv_customer);
        tvBookingDate = itemView.findViewById(R.id.tv_bookingDate);
        btnAccept = itemView.findViewById(R.id.btn_accept);
        btnReject = itemView.findViewById(R.id.btn_reject);
    }
}
