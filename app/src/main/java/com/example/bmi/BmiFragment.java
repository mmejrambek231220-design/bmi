package com.example.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BmiFragment extends Fragment {

    private boolean isMale = true;
    private int weight = 74;
    private int height = 180;
    private int age = 24;

    private TextView tvWeightValue, tvHeightValue, tvAgeValue;
    private ImageView imgMale, imgFemale;
    private TextView tvMaleLabel, tvFemaleLabel;
    private View layoutMale, layoutFemale;
    private TextView tvBmiResult, tvBmiCategory;
    private View resultSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        tvWeightValue = view.findViewById(R.id.tv_weight_value);
        tvHeightValue = view.findViewById(R.id.tv_height_value);
        tvAgeValue    = view.findViewById(R.id.tv_age_value);
        imgMale       = view.findViewById(R.id.img_male);
        imgFemale     = view.findViewById(R.id.img_female);
        tvMaleLabel   = view.findViewById(R.id.tv_male_label);
        tvFemaleLabel = view.findViewById(R.id.tv_female_label);
        layoutMale    = view.findViewById(R.id.layout_male);
        layoutFemale  = view.findViewById(R.id.layout_female);
        tvBmiResult   = view.findViewById(R.id.tv_bmi_result);
        tvBmiCategory = view.findViewById(R.id.tv_bmi_category);
        resultSection = view.findViewById(R.id.result_section);

        layoutMale.setOnClickListener(v -> {
            isMale = true;
            updateGenderUI();
        });
        layoutFemale.setOnClickListener(v -> {
            isMale = false;
            updateGenderUI();
        });

        view.findViewById(R.id.btn_weight_minus).setOnClickListener(v -> {
            if (weight > 1) { weight--; tvWeightValue.setText(String.valueOf(weight)); }
        });
        view.findViewById(R.id.btn_weight_plus).setOnClickListener(v -> {
            if (weight < 300) { weight++; tvWeightValue.setText(String.valueOf(weight)); }
        });

        view.findViewById(R.id.btn_height_minus).setOnClickListener(v -> {
            if (height > 50) { height--; tvHeightValue.setText(String.valueOf(height)); }
        });
        view.findViewById(R.id.btn_height_plus).setOnClickListener(v -> {
            if (height < 250) { height++; tvHeightValue.setText(String.valueOf(height)); }
        });

        view.findViewById(R.id.btn_age_minus).setOnClickListener(v -> {
            if (age > 1) { age--; tvAgeValue.setText(String.valueOf(age)); }
        });
        view.findViewById(R.id.btn_age_plus).setOnClickListener(v -> {
            if (age < 120) { age++; tvAgeValue.setText(String.valueOf(age)); }
        });

        view.findViewById(R.id.btn_calculate).setOnClickListener(v -> calculateBmi());

        TextView tvViewWorkout = view.findViewById(R.id.tv_view_workout);
        tvViewWorkout.setOnClickListener(v -> {
            String category = tvBmiCategory.getText().toString();
            Intent intent = new Intent(getActivity(), WorkoutActivity.class);
            intent.putExtra("bmi_category", category);
            startActivity(intent);
        });

        updateGenderUI();
        return view;
    }

    private void calculateBmi() {
        float heightM = height / 100f;
        float bmi = weight / (heightM * heightM);
        String bmiStr = String.format(Locale.US, "%.1f", bmi);
        String category = getCategory(bmi);
        int color = getCategoryColor(bmi);
        tvBmiResult.setText(bmiStr);
        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(color);
        tvBmiCategory.setBackgroundResource(R.drawable.bg_category_default);
        resultSection.setVisibility(View.VISIBLE);

        saveBmiToFirebase(bmi, category);
        showWorkoutDialog(category);
    }

    private String getCategory(float bmi) {
        if (bmi < 16.0f)      return "Айқын арықтық";
        else if (bmi < 17.0f) return "Орташа арықтық";
        else if (bmi < 18.5f) return "Жеңіл арықтық";
        else if (bmi < 25.0f) return "Қалыпты";
        else if (bmi < 30.0f) return "Артық салмақ";
        else if (bmi < 35.0f) return "Семіздік I дәреже";
        else if (bmi < 40.0f) return "Семіздік II дәреже";
        else                   return "Семіздік III дәреже";
    }

    private int getCategoryColor(float bmi) {
        if (bmi < 16.0f)      return Color.parseColor("#E53935");
        else if (bmi < 17.0f) return Color.parseColor("#EF5350");
        else if (bmi < 18.5f) return Color.parseColor("#FF7043");
        else if (bmi < 25.0f) return Color.parseColor("#43A047");
        else if (bmi < 30.0f) return Color.parseColor("#FB8C00");
        else if (bmi < 35.0f) return Color.parseColor("#E53935");
        else if (bmi < 40.0f) return Color.parseColor("#B71C1C");
        else                   return Color.parseColor("#880E4F");
    }

    private void saveBmiToFirebase(float bmi, String category) {
        if (getContext() == null) return;
        SharedPreferences prefs = requireContext().getSharedPreferences("BMI_PREFS", android.content.Context.MODE_PRIVATE);
        String uid = prefs.getString("user_uid", null);
        if (uid == null) return;

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        Map<String, Object> record = new HashMap<>();
        record.put("bmi", bmi);
        record.put("category", category);
        record.put("timestamp", System.currentTimeMillis());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(uid).child("bmi_history").child(date).setValue(record);
    }

    private void showWorkoutDialog(String category) {
        if (getContext() == null) return;
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.workout_dialog_title))
                .setMessage(getString(R.string.workout_dialog_message))
                .setPositiveButton(getString(R.string.dialog_yes), (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                    intent.putExtra("bmi_category", category);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.dialog_no), null)
                .show();
    }

    private void updateGenderUI() {
        if (getContext() == null) return;
        int primaryColor = ContextCompat.getColor(requireContext(), R.color.vitality_primary);
        int mutedColor = ContextCompat.getColor(requireContext(), R.color.vitality_on_surface_variant);

        if (isMale) {
            layoutMale.setBackgroundResource(R.drawable.bg_gender_selected);
            ImageViewCompat.setImageTintList(imgMale, ColorStateList.valueOf(primaryColor));
            tvMaleLabel.setTextColor(primaryColor);

            layoutFemale.setBackgroundResource(R.drawable.bg_gender_default);
            ImageViewCompat.setImageTintList(imgFemale, ColorStateList.valueOf(mutedColor));
            tvFemaleLabel.setTextColor(mutedColor);
        } else {
            layoutFemale.setBackgroundResource(R.drawable.bg_gender_selected);
            ImageViewCompat.setImageTintList(imgFemale, ColorStateList.valueOf(primaryColor));
            tvFemaleLabel.setTextColor(primaryColor);

            layoutMale.setBackgroundResource(R.drawable.bg_gender_default);
            ImageViewCompat.setImageTintList(imgMale, ColorStateList.valueOf(mutedColor));
            tvMaleLabel.setTextColor(mutedColor);
        }
    }
}
