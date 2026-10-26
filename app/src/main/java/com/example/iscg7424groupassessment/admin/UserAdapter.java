package com.example.iscg7424groupassessment.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    public interface OnUserActionListener {
        void onEdit(UserItem user);
        void onDelete(UserItem user);
    }

    private final List<UserItem> items;
    private final OnUserActionListener listener;

    public UserAdapter(List<UserItem> items, OnUserActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        final UserItem user = items.get(position);

        holder.tvInitials.setText(initials(user.getFullName()));
        holder.tvName.setText(safe(user.getFullName()));
        holder.tvLoginId.setText("ID: " + safe(user.getUsername()));

        String email = safe(user.getEmail());
        holder.tvEmail.setText(email.isEmpty() ? "No Email" : email);

        String phone = safe(user.getPhone());
        holder.tvPhone.setText(phone.isEmpty() ? "No Phone" : phone);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(user);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String initials(String name) {
        String n = safe(name).trim();
        if(n.isEmpty()) {
            return "?";
        }
        String[] parts = n.split("\\s+");
        String result = parts[0].substring(0, 1);
        if(parts.length > 1) {
            result = result + parts[1].substring(0,1);
        }
        return  result.toUpperCase();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

}
