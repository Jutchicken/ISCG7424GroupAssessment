package com.example.iscg7424groupassessment;

import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class PSAdapter extends RecyclerView.Adapter<PCHolder> {

    private Property[] PArr;

    public PSAdapter(Property[] PArr) {
        this.PArr = PArr;
    }

    @NonNull
    @Override
    public PCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ps_item, parent, false);
        return new PCHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PCHolder holder, int position) {
        Property property = PArr[position];

        holder.tvPropertyName.setText(property.getName());
        holder.tvLocation.setText("Location: " + property.getLocation());
        holder.tvPrice.setText(property.getPrice());
        holder.btnDetails.setText("Delete");

        holder.btnDetails.setOnClickListener(v -> {
            deleteProperty(v, position);
        });
    }

    @Override
    public int getItemCount() {
        return PArr.length;
    }

    private void deleteProperty(View view, int position) {
        try {
            SharedPreferences preferences = view.getContext().getSharedPreferences(
                    "SupplierData", android.content.Context.MODE_PRIVATE
            );

            String propertyData = preferences.getString(
                    "properties", "[]"
            );

            JSONArray properties = new JSONArray(propertyData);


            // Delete selected Property
            properties.remove(position);

            int propertyCount = preferences.getInt("propertyCount", 0);

            if (propertyCount > 0) {
                propertyCount--;
            }


            // Save edited Data
            preferences.edit().putString("properties", properties.toString())
                    .putInt("propertyCount", propertyCount).apply();

            // Delete from RecyclerView
            Property[] newArray = new Property[PArr.length - 1];

            int index = 0;

            for (int i = 0; i < PArr.length; i++) {
                if (i != position) {
                    newArray[index] = PArr[i];
                    index++;
                }
            }

            PArr = newArray;
            notifyItemRemoved(position);

            Toast.makeText(view.getContext(),
                    "Property deleted",
                    Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(view.getContext(),
                    "Failed to delete property",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
