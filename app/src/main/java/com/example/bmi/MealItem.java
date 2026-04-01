package com.example.bmi;

public class MealItem {
    private String name;
    private String calories;
    private String recipe;
    private String imageUrl;
    private String mealType;

    public MealItem(String name, String calories, String recipe, String imageUrl, String mealType) {
        this.name = name;
        this.calories = calories;
        this.recipe = recipe;
        this.imageUrl = imageUrl;
        this.mealType = mealType;
    }

    public String getName() { return name; }
    public String getCalories() { return calories; }
    public String getRecipe() { return recipe; }
    public String getImageUrl() { return imageUrl; }
    public String getMealType() { return mealType; }
}
