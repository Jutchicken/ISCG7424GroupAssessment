package com.example.iscg7424groupassessment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BRAdapter extends RecyclerView.Adapter<BRHolder>{

    private BookingRequest[] requests;

    public BRAdapter(BookingRequest[] requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public BRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.pbr_item, parent, false);

        return new BRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BRHolder holder, int position) {
        BookingRequest request = requests[position];

        holder.tvRequestProperty.setText(request.getPropertyName());
        holder.tvCustomer.setText("Customer: " + request.getCustomerName());
        holder.tvBookingDate.setText("Booking Date: " + request.getBookingDate());

        holder.btnAccept.setOnClickListener(v -> {
            removeRequest(v.getContext(), position);
        });

        holder.btnReject.setOnClickListener(v -> {
            removeRequest(v.getContext(), position);
        });
    }

    @Override
    public int getItemCount() {
        return requests.length;
    }

    private void removeRequest(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(
                "SupplierData", Context.MODE_PRIVATE
        );

        BookingRequest request = requests[position];

        String customerName = request.getCustomerName();

        // Delete Request per Customer
        if (customerName.equals("John")) {
            preferences.edit().putBoolean("johnRequest", false).apply();
        }
        else if (customerName.equals("Sarah")) {
            preferences.edit().putBoolean("sarahRequest", false).apply();
        }


        // Reduce Request Count
        int requestCount = preferences.getInt("requestCount", requests.length);

        if (requestCount > 0) {
            requestCount--;
        }

        preferences.edit().putInt("requestCount", requestCount).apply();


        // Delete from RecyclerView
        BookingRequest[] newRequests = new BookingRequest[requests.length - 1];

        int index = 0;

        for (int i = 0; i < requests.length; i++) {
            if (i != position) {
                newRequests[index] = requests[i];
                index++;
            }
        }

        requests = newRequests;
        notifyItemRemoved(position);

        Toast.makeText(context,
                "Booking request processed",
                Toast.LENGTH_SHORT).show();
    }
}
