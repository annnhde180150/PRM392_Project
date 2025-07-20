package com.example.homehelperfinder.ui.registerHelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperSkillResponse;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {
    private final List<HelperSkillResponse> skillList;
    private SkillActionListener actionListener;
    private boolean showEditDeleteButtons = true;

    public SkillAdapter(List<HelperSkillResponse> skillList) {
        this.skillList = skillList;
    }

    public void setSkillActionListener(SkillActionListener listener) {
        this.actionListener = listener;
    }

    public void setShowEditDeleteButtons(boolean show) {
        this.showEditDeleteButtons = show;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        HelperSkillResponse skill = skillList.get(position);
        holder.tvServiceName.setText(skill.getServiceName());
        holder.tvYears.setText(skill.getYearsOfExperience() == null ? "" : skill.getYearsOfExperience() + " years");
        holder.tvPrimary.setVisibility(skill.isPrimarySkill() ? View.VISIBLE : View.GONE);
        
        // Show/hide edit and delete buttons based on edit mode
        holder.btnEdit.setVisibility(showEditDeleteButtons ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(showEditDeleteButtons ? View.VISIBLE : View.GONE);
        
        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(position);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    public void addSkill(HelperSkillResponse skill) {
        skillList.add(skill);
        notifyItemInserted(skillList.size() - 1);
    }

    public void updateSkill(int position, HelperSkillResponse skill) {
        skillList.set(position, skill);
        notifyItemChanged(position);
    }

    public void removeSkill(int position) {
        skillList.remove(position);
        notifyItemRemoved(position);
    }

    public HelperSkillResponse getSkill(int position) {
        return skillList.get(position);
    }

    public interface SkillActionListener {
        void onEdit(int position);

        void onDelete(int position);
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvYears, tvPrimary;
        ImageButton btnEdit, btnDelete;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvYears = itemView.findViewById(R.id.tvYears);
            tvPrimary = itemView.findViewById(R.id.tvPrimary);
            btnEdit = itemView.findViewById(R.id.btnEditSkill);
            btnDelete = itemView.findViewById(R.id.btnDeleteSkill);
        }
    }
} 