package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ResultActivity extends AppCompatActivity {

    private GaugeView gaugeView;
    private TextView tvBmiValue;
    private TextView tvGender, tvWeight, tvHeight, tvAge;
    private TextView tvCat1, tvCat2, tvCat3, tvCat4, tvCat5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> finish());

        tvGender = findViewById(R.id.tv_gender);
        tvWeight = findViewById(R.id.tv_weight);
        tvHeight = findViewById(R.id.tv_height);
        tvAge    = findViewById(R.id.tv_age);

        tvBmiValue = findViewById(R.id.tv_bmi_value);
        gaugeView  = findViewById(R.id.gauge_view);

        tvCat1 = findViewById(R.id.tv_cat1);
        tvCat2 = findViewById(R.id.tv_cat2);
        tvCat3 = findViewById(R.id.tv_cat3);
        tvCat4 = findViewById(R.id.tv_cat4);
        tvCat5 = findViewById(R.id.tv_cat5);

        Intent intent = getIntent();
        String gender = intent.getStringExtra("gender");
        float weight  = intent.getFloatExtra("weight", 70f);
        float height  = intent.getFloatExtra("height", 170f);
        int age       = intent.getIntExtra("age", 25);

        float heightM = height / 100f;
        float bmi = weight / (heightM * heightM);

        String genderLabel = "Ер".equals(gender) ? "Ер" : "Әйел";
        tvGender.setText(genderLabel);
        tvWeight.setText((int) weight + " кг");
        tvHeight.setText((int) height + " см");
        tvAge.setText(String.valueOf(age));

        String bmiStr = String.format("%.1f", bmi);
        tvBmiValue.setText(bmiStr);
        gaugeView.setBmi(bmi);

        highlightCategory(bmi);
    }

    private void highlightCategory(float bmi) {
        int defaultBg   = R.drawable.bg_category_default;
        int activeBg    = R.drawable.bg_category_active;
        int defaultText = ContextCompat.getColor(this, R.color.text_secondary);
        int activeText  = ContextCompat.getColor(this, R.color.white);

        tvCat1.setBackgroundResource(defaultBg); tvCat1.setTextColor(defaultText);
        tvCat2.setBackgroundResource(defaultBg); tvCat2.setTextColor(defaultText);
        tvCat3.setBackgroundResource(defaultBg); tvCat3.setTextColor(defaultText);
        tvCat4.setBackgroundResource(defaultBg); tvCat4.setTextColor(defaultText);
        tvCat5.setBackgroundResource(defaultBg); tvCat5.setTextColor(defaultText);

        TextView active;
        if (bmi < 16)         active = tvCat1;
        else if (bmi < 18.5f) active = tvCat2;
        else if (bmi < 25)    active = tvCat3;
        else if (bmi < 40)    active = tvCat4;
        else                  active = tvCat5;

        active.setBackgroundResource(activeBg);
        active.setTextColor(activeText);
    }
}
