package com.example.bmi;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class MealHorizontalAdapter extends RecyclerView.Adapter<MealHorizontalAdapter.HorizontalViewHolder> {

    private final List<MealItem> items;
    private final boolean featured;

    public MealHorizontalAdapter(List<MealItem> items, boolean featured) {
        this.items = new ArrayList<>(items);
        this.featured = featured;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = featured ? R.layout.item_meal_featured : R.layout.item_meal_horizontal;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        MealItem item = items.get(position);
        holder.tvMealName.setText(item.getName());
        holder.tvMealCalories.setText(item.getCalories());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivMeal);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MealDetailActivity.class);
            intent.putExtra(MealDetailActivity.EXTRA_NAME, item.getName());
            intent.putExtra(MealDetailActivity.EXTRA_CALORIES, item.getCalories());
            intent.putExtra(MealDetailActivity.EXTRA_IMAGE_URL, item.getImageUrl());
            intent.putExtra(MealDetailActivity.EXTRA_MEAL_TYPE, item.getMealType());
            intent.putExtra(MealDetailActivity.EXTRA_DESCRIPTION, item.getDescription());
            intent.putExtra(MealDetailActivity.EXTRA_INGREDIENTS, item.getIngredients());
            intent.putExtra(MealDetailActivity.EXTRA_STEPS, item.getSteps());
            intent.putExtra(MealDetailActivity.EXTRA_PROTEIN, item.getProtein());
            intent.putExtra(MealDetailActivity.EXTRA_CARBS, item.getCarbs());
            intent.putExtra(MealDetailActivity.EXTRA_FAT, item.getFat());
            intent.putExtra(MealDetailActivity.EXTRA_DURATION, item.getDuration());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<MealItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvMealName;
        TextView tvMealCalories;

        HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.iv_meal);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            tvMealCalories = itemView.findViewById(R.id.tv_meal_calories);
        }
    }
}
