package com.example.bmi;

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
        holder.tvCalories.setText(item.getCalories());
        holder.tvRecipe.setText(item.getRecipe());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivMeal);

        holder.tvToggleRecipe.setOnClickListener(v -> {
            if (holder.tvRecipe.getVisibility() == View.VISIBLE) {
                holder.tvRecipe.setVisibility(View.GONE);
                holder.tvToggleRecipe.setText(R.string.recipe_show);
            } else {
                holder.tvRecipe.setVisibility(View.VISIBLE);
                holder.tvToggleRecipe.setText(R.string.recipe_hide);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvMealName, tvCalories, tvRecipe, tvToggleRecipe;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.iv_meal);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            tvCalories = itemView.findViewById(R.id.tv_meal_calories);
            tvRecipe = itemView.findViewById(R.id.tv_meal_recipe);
            tvToggleRecipe = itemView.findViewById(R.id.tv_toggle_recipe);
        }
    }
}
