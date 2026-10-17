package com.example.iscg7424groupassessment.admin;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {



    public interface OnFoodActionListener {
        void onEdit(FoodItem item);
        void onDelete(FoodItem item);
    }

    private final List<FoodItem> items;
    private final OnFoodActionListener listener;

    public FoodAdapter(List<FoodItem> items, OnFoodActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        FoodItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$ %.2f", item.getPrice()));
        holder.tvDescription.setText(item.getDescription());

        String image = item.getImage();
        if(image == null || image.isEmpty()) {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            try{
                holder.ivImage.setImageURI(Uri.parse(image));
            } catch (SecurityException e){
                holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(item);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
