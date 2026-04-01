package com.example.bmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<WorkoutItem> items;

    public WorkoutAdapter(List<WorkoutItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem item = items.get(position);
        holder.tvDay.setText(item.getDay());
        holder.tvExercises.setText(item.getExercises());

        if (item.isTip()) {
            holder.tvReps.setVisibility(View.GONE);
            holder.tvTipLabel.setVisibility(View.VISIBLE);
        } else {
            holder.tvReps.setVisibility(View.VISIBLE);
            holder.tvReps.setText(item.getReps());
            holder.tvTipLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvExercises, tvReps, tvTipLabel;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_workout_day);
            tvExercises = itemView.findViewById(R.id.tv_workout_exercises);
            tvReps = itemView.findViewById(R.id.tv_workout_reps);
            tvTipLabel = itemView.findViewById(R.id.tv_workout_tip_label);
        }
    }
}
