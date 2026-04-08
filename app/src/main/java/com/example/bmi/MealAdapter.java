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
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private final List<MealItem> items;

    public MealAdapter(List<MealItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealItem item = items.get(position);
        holder.tvMealName.setText(item.getName());
        holder.tvMealType.setText(item.getMealType());
        holder.tvCalories.setText(item.getCalories());
        holder.tvRecipe.setText(item.getShortRecipe());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivMeal);

        holder.tvToggleRecipe.setOnClickListener(v -> openDetail(holder, item));
        holder.itemView.setOnClickListener(v -> openDetail(holder, item));
    }

    private void openDetail(MealViewHolder holder, MealItem item) {
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvMealName, tvMealType, tvCalories, tvRecipe, tvToggleRecipe;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.iv_meal);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            tvMealType = itemView.findViewById(R.id.tv_meal_type);
            tvCalories = itemView.findViewById(R.id.tv_meal_calories);
            tvRecipe = itemView.findViewById(R.id.tv_meal_recipe);
            tvToggleRecipe = itemView.findViewById(R.id.tv_toggle_recipe);
        }
    }
}
