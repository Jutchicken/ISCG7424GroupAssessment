package com.example.iscg7424groupassessment.customer;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;
import com.example.iscg7424groupassessment.admin.FoodAdapter;
import com.example.iscg7424groupassessment.admin.FoodHolder;
import com.example.iscg7424groupassessment.admin.FoodImage;
import com.example.iscg7424groupassessment.admin.FoodItem;

import java.util.List;
import java.util.Locale;

public class C_FoodAdapter extends RecyclerView.Adapter<C_FoodHolder> {


    public interface OnFoodActionListener {
        void onEdit(FoodItem item);
        void onDelete(FoodItem item);
    }

    private final List<FoodItem> items;
    private final C_FoodAdapter.OnFoodActionListener listener;

    public C_FoodAdapter(List<FoodItem> items, C_FoodAdapter.OnFoodActionListener listener) {
        this.items = items;
        this.listener = listener;
    }
    @NonNull
    @Override
    public C_FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_food_item, parent, false);
        return new C_FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull C_FoodHolder holder, int position) {
        FoodItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$ %.2f", item.getPrice()));
        holder.tvDescription.setText(item.getDescription());

        Bitmap bitmap = null;

        String image = item.getImage();

        if(image != null && !image.isEmpty()) {
            bitmap = FoodImage.base64ToBitmap(image);
        }

        if(image != null) {
            holder.ivImage.setImageBitmap(bitmap);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
