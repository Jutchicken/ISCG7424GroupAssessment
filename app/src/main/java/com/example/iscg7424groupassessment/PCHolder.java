package com.example.iscg7424groupassessment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PCHolder extends RecyclerView.ViewHolder {
    TextView tvPropertyName;
    TextView tvLocation;
    TextView tvPrice;
    Button btnDetails;

    public PCHolder(@NonNull View itemView) {
        super(itemView);

        tvPropertyName = itemView.findViewById(R.id.tv_propertyName);
        tvLocation = itemView.findViewById(R.id.tv_location);
        tvPrice = itemView.findViewById(R.id.tv_price);
        btnDetails = itemView.findViewById(R.id.btn_details);
    }
}
