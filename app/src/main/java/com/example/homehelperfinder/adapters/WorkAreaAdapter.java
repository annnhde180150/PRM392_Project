package com.example.homehelperfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.models.HelperWorkArea;
import java.util.List;

public class WorkAreaAdapter extends RecyclerView.Adapter<WorkAreaAdapter.WorkAreaViewHolder> {
    private List<HelperWorkArea> workAreaList;
    private WorkAreaActionListener actionListener;

    public interface WorkAreaActionListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public WorkAreaAdapter(List<HelperWorkArea> workAreaList) {
        this.workAreaList = workAreaList;
    }

    public void setWorkAreaActionListener(WorkAreaActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public WorkAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_area, parent, false);
        return new WorkAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkAreaViewHolder holder, int position) {
        HelperWorkArea area = workAreaList.get(position);
        String info = area.getCity() + ", " + area.getDistrict();
        if (area.getWard() != null && !area.getWard().isEmpty()) {
            info += ", " + area.getWard();
        }
        holder.tvWorkAreaInfo.setText(info);
        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(position);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return workAreaList.size();
    }

    public void addWorkArea(HelperWorkArea area) {
        workAreaList.add(area);
        notifyItemInserted(workAreaList.size() - 1);
    }

    public void updateWorkArea(int position, HelperWorkArea area) {
        workAreaList.set(position, area);
        notifyItemChanged(position);
    }

    public void removeWorkArea(int position) {
        workAreaList.remove(position);
        notifyItemRemoved(position);
    }

    public HelperWorkArea getWorkArea(int position) {
        return workAreaList.get(position);
    }

    public static class WorkAreaViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkAreaInfo;
        ImageButton btnEdit, btnDelete;
        public WorkAreaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkAreaInfo = itemView.findViewById(R.id.tvWorkAreaInfo);
            btnEdit = itemView.findViewById(R.id.btnEditWorkArea);
            btnDelete = itemView.findViewById(R.id.btnDeleteWorkArea);
        }
    }
} 