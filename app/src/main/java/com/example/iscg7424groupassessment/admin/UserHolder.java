package com.example.iscg7424groupassessment.admin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

public class UserHolder extends RecyclerView.ViewHolder {

    TextView tvRole, tvBadge, tvInitials, tvName, tvLoginId, tvEmail, tvPhone;
    Button btnEdit, btnDelete;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        tvRole = itemView.findViewById(R.id.tv_userRole);
        tvBadge = itemView.findViewById(R.id.tv_userBadge);
        tvInitials = itemView.findViewById(R.id.tv_userInitials);
        tvName = itemView.findViewById(R.id.tv_userName);
        tvLoginId = itemView.findViewById(R.id.tv_userLoginId);
        tvEmail = itemView.findViewById(R.id.tv_userEmail);
        tvPhone = itemView.findViewById(R.id.tv_userPhone);

        btnEdit = itemView.findViewById(R.id.btn_editUser);
        btnDelete = itemView.findViewById(R.id.btn_deleteUser);
    }
}
