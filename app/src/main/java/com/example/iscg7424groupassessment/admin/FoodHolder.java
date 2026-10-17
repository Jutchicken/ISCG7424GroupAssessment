package com.example.iscg7424groupassessment.admin;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class FoodHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvPrice, tvDescription;
    ImageView ivImage;
    Button btnEdit, btnDelete;

    public FoodHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_foodName);
        tvPrice = itemView.findViewById(R.id.tv_foodPrice);
        tvDescription = itemView.findViewById(R.id.tv_foodDescription);

        ivImage = itemView.findViewById(R.id.iv_foodImage);

        btnEdit = itemView.findViewById(R.id.btn_editFood);
        btnDelete = itemView.findViewById(R.id.btn_deleteFood);
    }
}
