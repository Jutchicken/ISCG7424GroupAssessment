package com.example.iscg7424groupassessment.admin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class BookingTableHolder extends RecyclerView.ViewHolder {
    CardView cvTable;
    TextView tvName, tvInfo;


    public BookingTableHolder(@NonNull View itemView) {
        super(itemView);
        cvTable = itemView.findViewById(R.id.cv_bookingTable);
        tvName = itemView.findViewById(R.id.tv_bookingTableName);
        tvInfo = itemView.findViewById(R.id.tv_bookingTableInfo);
    }
}
