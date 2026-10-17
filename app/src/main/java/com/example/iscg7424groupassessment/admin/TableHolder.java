package com.example.iscg7424groupassessment.admin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class TableHolder extends RecyclerView.ViewHolder {

    TextView tvTableNumber, tvTableSeats;
    Button btnEdit, btnDelete;

    public TableHolder(@NonNull View itemView) {
        super(itemView);
        tvTableNumber = itemView.findViewById(R.id.tv_tableNumber);
        tvTableSeats = itemView.findViewById(R.id.tv_tableSeats);

        btnEdit = itemView.findViewById(R.id.btn_editTable);
        btnDelete = itemView.findViewById(R.id.btn_deleteTable);
    }
}
