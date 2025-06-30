package com.example.homehelperfinder.ui.base;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homehelperfinder.utils.Logger;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {
    
    protected List<T> items;
    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;
    
    public BaseAdapter() {
        this.items = new ArrayList<>();
    }
    
    public BaseAdapter(List<T> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }
    
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = getItem(position);
        if (item != null) {
            holder.bind(item, position);
            setupClickListeners(holder, item, position);
        }
    }
    
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    // Get item at position
    public T getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return items.get(position);
        }
        return null;
    }
    
    // Update all items
    public void updateItems(List<T> newItems) {
        if (newItems != null) {
            this.items.clear();
            this.items.addAll(newItems);
            notifyDataSetChanged();
            Logger.d(getClass().getSimpleName(), "Updated " + newItems.size() + " items");
        }
    }
    
    // Add single item
    public void addItem(T item) {
        if (item != null) {
            items.add(item);
            notifyItemInserted(items.size() - 1);
            Logger.d(getClass().getSimpleName(), "Added item at position " + (items.size() - 1));
        }
    }
    
    // Add item at specific position
    public void addItem(int position, T item) {
        if (item != null && position >= 0 && position <= items.size()) {
            items.add(position, item);
            notifyItemInserted(position);
            Logger.d(getClass().getSimpleName(), "Added item at position " + position);
        }
    }
    
    // Remove item
    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            Logger.d(getClass().getSimpleName(), "Removed item at position " + position);
        }
    }
    
    // Remove specific item
    public void removeItem(T item) {
        int position = items.indexOf(item);
        if (position != -1) {
            removeItem(position);
        }
    }
    
    // Clear all items
    public void clearItems() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
        Logger.d(getClass().getSimpleName(), "Cleared " + size + " items");
    }
    
    // Check if empty
    public boolean isEmpty() {
        return getItemCount() == 0;
    }
    
    // Get all items
    public List<T> getItems() {
        return new ArrayList<>(items);
    }
    
    // Setup click listeners
    private void setupClickListeners(VH holder, T item, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item, position));
        }
        
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                onItemLongClickListener.onItemLongClick(item, position);
                return true;
            });
        }
    }
    
    // Set click listeners
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }
    
    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
    }
    
    // Base ViewHolder class
    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        
        public abstract void bind(Object item, int position);
    }
    
    // Click listener interfaces
    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }
    
    public interface OnItemLongClickListener<T> {
        void onItemLongClick(T item, int position);
    }
}
