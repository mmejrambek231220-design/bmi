package com.example.bmi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BmiHistoryAdapter extends RecyclerView.Adapter<BmiHistoryAdapter.HistoryViewHolder> {

    private final List<BmiHistoryItem> items;

    public BmiHistoryAdapter(List<BmiHistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bmi_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BmiHistoryItem item = items.get(position);
        holder.tvDate.setText(item.getDate());
        holder.tvBmiValue.setText(String.format("%.1f", item.getBmi()));
        holder.tvCategory.setText(item.getCategory());
        holder.tvBmiValue.setTextColor(getCategoryColor(item.getBmi()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int getCategoryColor(float bmi) {
        if (bmi < 16.0f)       return Color.parseColor("#E53935");
        else if (bmi < 17.0f)  return Color.parseColor("#EF5350");
        else if (bmi < 18.5f)  return Color.parseColor("#FF7043");
        else if (bmi < 25.0f)  return Color.parseColor("#43A047");
        else if (bmi < 30.0f)  return Color.parseColor("#FB8C00");
        else if (bmi < 35.0f)  return Color.parseColor("#E53935");
        else if (bmi < 40.0f)  return Color.parseColor("#B71C1C");
        else                   return Color.parseColor("#880E4F");
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvBmiValue, tvCategory;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvBmiValue = itemView.findViewById(R.id.tv_history_bmi);
            tvCategory = itemView.findViewById(R.id.tv_history_category);
        }
    }
}
