package com.example.iscg7424groupassessment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PCAdapter extends RecyclerView.Adapter<PCHolder>{
    private Property[] PArr;

    public PCAdapter(Property[] PArr) {
        this.PArr = PArr;
    }

    @NonNull
    @Override
    public PCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pc_item, parent, false);
        return new PCHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PCHolder holder, int position) {
        Property property = PArr[position];

        holder.tvPropertyName.setText(property.getName());
        holder.tvLocation.setText("Location: " + property.getLocation());
        holder.tvPrice.setText(property.getPrice());

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(
                    v.getContext(),
                    PropertyDetailPage.class
            );

            intent.putExtra("name", property.getName());
            intent.putExtra("location", property.getLocation());
            intent.putExtra("price", property.getPrice());
            intent.putExtra("category", property.getCategory());
            intent.putExtra("image", property.getImage());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return PArr.length;
    }

    // keep array updated
    public void setProperties(Property[] properties) {
        this.PArr = properties;
        notifyDataSetChanged();
    }

    // update array based on searched word
    public void searchProperties(Property[] properties, String keyword) {
        if (keyword.isEmpty()) {
            this.PArr = properties;
            notifyDataSetChanged();
            return;
        }

        int count = 0;

        for (Property property : properties) {
            if (property.getName().toLowerCase().contains(keyword.toLowerCase())
                    || property.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
                count++;
            }
        }

        Property[] result = new Property[count];
        int index = 0;

        for (Property property : properties) {
            if (property.getName().toLowerCase().contains(keyword.toLowerCase())
                    || property.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
                result[index] = property;
                index++;
            }
        }

        this.PArr = result;
        notifyDataSetChanged();
    }
}
