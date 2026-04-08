package com.example.bmi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class MealDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "meal_name";
    public static final String EXTRA_CALORIES = "meal_calories";
    public static final String EXTRA_IMAGE_URL = "meal_image";
    public static final String EXTRA_MEAL_TYPE = "meal_type";
    public static final String EXTRA_DESCRIPTION = "meal_description";
    public static final String EXTRA_INGREDIENTS = "meal_ingredients";
    public static final String EXTRA_STEPS = "meal_steps";
    public static final String EXTRA_PROTEIN = "meal_protein";
    public static final String EXTRA_CARBS = "meal_carbs";
    public static final String EXTRA_FAT = "meal_fat";
    public static final String EXTRA_DURATION = "meal_duration";
    private int servingCount = 1;
    private TextView tvServingCount;
    private int carbsBase;
    private int proteinBase;
    private int fatBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        ImageView ivBack = findViewById(R.id.iv_back);
        ImageView ivMeal = findViewById(R.id.iv_detail_meal);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvType = findViewById(R.id.tv_detail_type);
        TextView tvCalories = findViewById(R.id.tv_detail_calories);
        TextView tvDuration = findViewById(R.id.tv_detail_duration);
        TextView tvDescription = findViewById(R.id.tv_detail_description);
        TextView tvProtein = findViewById(R.id.tv_detail_protein);
        TextView tvCarbs = findViewById(R.id.tv_detail_carbs);
        TextView tvFat = findViewById(R.id.tv_detail_fat);
        CircularProgressIndicator progressCarbs = findViewById(R.id.progress_carbs);
        CircularProgressIndicator progressProtein = findViewById(R.id.progress_protein);
        CircularProgressIndicator progressFat = findViewById(R.id.progress_fat);
        TextView tvIngredients = findViewById(R.id.tv_detail_ingredients);
        TextView tvSteps = findViewById(R.id.tv_detail_steps);
        tvServingCount = findViewById(R.id.tv_serving_count);
        MaterialButton btnMinus = findViewById(R.id.btn_serving_minus);
        MaterialButton btnPlus = findViewById(R.id.btn_serving_plus);

        tvTitle.setText(getIntent().getStringExtra(EXTRA_NAME));
        tvType.setText(getIntent().getStringExtra(EXTRA_MEAL_TYPE));
        tvCalories.setText(getIntent().getStringExtra(EXTRA_CALORIES));
        tvDuration.setText(getIntent().getStringExtra(EXTRA_DURATION));
        tvDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
        carbsBase = parseNumber(getIntent().getStringExtra(EXTRA_CARBS));
        proteinBase = parseNumber(getIntent().getStringExtra(EXTRA_PROTEIN));
        fatBase = parseNumber(getIntent().getStringExtra(EXTRA_FAT));
        tvCarbs.setText("Carbs " + carbsBase + "g");
        tvProtein.setText("Protein " + proteinBase + "g");
        tvFat.setText("Fat " + fatBase + "g");
        tvIngredients.setText(getIntent().getStringExtra(EXTRA_INGREDIENTS));
        tvSteps.setText(getIntent().getStringExtra(EXTRA_STEPS));
        setMacroProgress(progressCarbs, progressProtein, progressFat, carbsBase, proteinBase, fatBase);

        Glide.with(this)
                .load(getIntent().getStringExtra(EXTRA_IMAGE_URL))
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(ivMeal);

        ivBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        btnMinus.setOnClickListener(v -> updateServing(-1, tvCarbs, tvProtein, tvFat, progressCarbs, progressProtein, progressFat));
        btnPlus.setOnClickListener(v -> updateServing(1, tvCarbs, tvProtein, tvFat, progressCarbs, progressProtein, progressFat));
    }

    private void updateServing(
            int delta,
            TextView tvCarbs,
            TextView tvProtein,
            TextView tvFat,
            CircularProgressIndicator progressCarbs,
            CircularProgressIndicator progressProtein,
            CircularProgressIndicator progressFat
    ) {
        servingCount = Math.max(1, Math.min(10, servingCount + delta));
        tvServingCount.setText(String.valueOf(servingCount));
        int carbs = carbsBase * servingCount;
        int protein = proteinBase * servingCount;
        int fat = fatBase * servingCount;
        tvCarbs.setText("Carbs " + carbs + "g");
        tvProtein.setText("Protein " + protein + "g");
        tvFat.setText("Fat " + fat + "g");
        setMacroProgress(progressCarbs, progressProtein, progressFat, carbs, protein, fat);
    }

    private void setMacroProgress(
            CircularProgressIndicator progressCarbs,
            CircularProgressIndicator progressProtein,
            CircularProgressIndicator progressFat,
            int carbs,
            int protein,
            int fat
    ) {
        int total = Math.max(1, carbs + protein + fat);
        progressCarbs.setProgress((carbs * 100) / total);
        progressProtein.setProgress((protein * 100) / total);
        progressFat.setProgress((fat * 100) / total);
    }

    private int parseNumber(String value) {
        if (value == null) return 0;
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        return Integer.parseInt(digits);
    }
}
