package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // State
    private boolean isMale = true;
    private int weight = 70;
    private int height = 170;
    private int age = 25;

    // Views
    private TextView tvWeightValue, tvHeightValue, tvAgeValue;
    private ImageView imgMale, imgFemale;
    private TextView tvMaleLabel, tvFemaleLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        tvWeightValue = findViewById(R.id.tv_weight_value);
        tvHeightValue = findViewById(R.id.tv_height_value);
        tvAgeValue    = findViewById(R.id.tv_age_value);
        imgMale       = findViewById(R.id.img_male);
        imgFemale     = findViewById(R.id.img_female);
        tvMaleLabel   = findViewById(R.id.tv_male_label);
        tvFemaleLabel = findViewById(R.id.tv_female_label);

        // Gender selection
        findViewById(R.id.layout_male).setOnClickListener(v -> {
            isMale = true;
            updateGenderUI();
        });
        findViewById(R.id.layout_female).setOnClickListener(v -> {
            isMale = false;
            updateGenderUI();
        });

        // Weight buttons
        findViewById(R.id.btn_weight_minus).setOnClickListener(v -> {
            if (weight > 1) { weight--; tvWeightValue.setText(String.valueOf(weight)); }
        });
        findViewById(R.id.btn_weight_plus).setOnClickListener(v -> {
            if (weight < 300) { weight++; tvWeightValue.setText(String.valueOf(weight)); }
        });

        // Height buttons
        findViewById(R.id.btn_height_minus).setOnClickListener(v -> {
            if (height > 50) { height--; tvHeightValue.setText(String.valueOf(height)); }
        });
        findViewById(R.id.btn_height_plus).setOnClickListener(v -> {
            if (height < 250) { height++; tvHeightValue.setText(String.valueOf(height)); }
        });

        // Age buttons
        findViewById(R.id.btn_age_minus).setOnClickListener(v -> {
            if (age > 1) { age--; tvAgeValue.setText(String.valueOf(age)); }
        });
        findViewById(R.id.btn_age_plus).setOnClickListener(v -> {
            if (age < 120) { age++; tvAgeValue.setText(String.valueOf(age)); }
        });

        // Calculate button
        Button btnCalculate = findViewById(R.id.btn_calculate);
        btnCalculate.setOnClickListener(v -> {
            String gender = isMale ? "Ер" : "Әйел";
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("gender", gender);
            intent.putExtra("weight", (float) weight);
            intent.putExtra("height", (float) height);
            intent.putExtra("age", age);
            startActivity(intent);
        });

        // Init UI
        updateGenderUI();
    }

    private void updateGenderUI() {
        if (isMale) {
            imgMale.setBackgroundResource(R.drawable.bg_gender_selected);
            imgMale.setColorFilter(ContextCompat.getColor(this, R.color.blue_primary));
            tvMaleLabel.setTextColor(ContextCompat.getColor(this, R.color.blue_primary));

            imgFemale.setBackgroundResource(R.drawable.bg_gender_default);
            imgFemale.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
            tvFemaleLabel.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else {
            imgFemale.setBackgroundResource(R.drawable.bg_gender_selected);
            imgFemale.setColorFilter(ContextCompat.getColor(this, R.color.blue_primary));
            tvFemaleLabel.setTextColor(ContextCompat.getColor(this, R.color.blue_primary));

            imgMale.setBackgroundResource(R.drawable.bg_gender_default);
            imgMale.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
            tvMaleLabel.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }
}