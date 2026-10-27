package com.example.iscg7424groupassessment.customer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class C_FoodHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvPrice, tvDescription;
    ImageView ivImage;

    public C_FoodHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_customerFoodName);
        tvPrice = itemView.findViewById(R.id.tv_customerFoodPrice);
        tvDescription = itemView.findViewById(R.id.tv_customerFoodDescription);

        ivImage = itemView.findViewById(R.id.iv_customerFoodImage);

    }
}
