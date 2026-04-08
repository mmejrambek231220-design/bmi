package com.example.bmi;

public class MealItem {
    private final String name;
    private final String calories;
    private final String shortRecipe;
    private final String imageUrl;
    private final String mealType;
    private final String description;
    private final String ingredients;
    private final String steps;
    private final String protein;
    private final String carbs;
    private final String fat;
    private final String duration;

    public MealItem(
            String name,
            String calories,
            String shortRecipe,
            String imageUrl,
            String mealType,
            String description,
            String ingredients,
            String steps,
            String protein,
            String carbs,
            String fat,
            String duration
    ) {
        this.name = name;
        this.calories = calories;
        this.shortRecipe = shortRecipe;
        this.imageUrl = imageUrl;
        this.mealType = mealType;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.duration = duration;
    }

    public String getName() { return name; }
    public String getCalories() { return calories; }
    public String getShortRecipe() { return shortRecipe; }
    public String getImageUrl() { return imageUrl; }
    public String getMealType() { return mealType; }
    public String getDescription() { return description; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getProtein() { return protein; }
    public String getCarbs() { return carbs; }
    public String getFat() { return fat; }
    public String getDuration() { return duration; }
}
