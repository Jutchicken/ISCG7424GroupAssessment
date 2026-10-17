package com.example.iscg7424groupassessment.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424groupassessment.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableHolder> {

    public interface OnTableActionListener {
        void onEdit(TableItem item);
        void onDelete(TableItem item);
    }

    private final List<TableItem> items;
    private final OnTableActionListener listener;

    public TableAdapter(List<TableItem> items, OnTableActionListener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableHolder holder, int position) {
        final TableItem item = items.get(position);

        holder.tvTableNumber.setText("Table No: " + item.getTableNumber());
        holder.tvTableSeats.setText("Seats: " + item.getSeats());

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
