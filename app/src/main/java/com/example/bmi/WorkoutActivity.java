package com.example.bmi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ImageView ivBack = findViewById(R.id.iv_back_workout);
        ivBack.setOnClickListener(v -> finish());

        String category = getIntent().getStringExtra("bmi_category");
        if (category == null) category = "";

        RecyclerView recyclerView = findViewById(R.id.rv_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<WorkoutItem> items = buildWorkoutPlan(category);
        recyclerView.setAdapter(new WorkoutAdapter(items));
    }

    private List<WorkoutItem> buildWorkoutPlan(String category) {
        List<WorkoutItem> items = new ArrayList<>();
        boolean isUnderweight = category.contains("арықтық");
        boolean isOverweight  = category.contains("Артық") || category.contains("Семіздік");

        if (isUnderweight) {
            items.add(new WorkoutItem("Жоспар", getString(R.string.workout_plan_underweight), "", false));
            items.add(new WorkoutItem("Дүйсенбі", "Отырып-тұру, Отжимание, Гантелмен жаттығу", "3×10, 3×12, 3×10", false));
            items.add(new WorkoutItem("Сейсенбі", "Демалыс немесе жаяу жүру", "30 минут", false));
            items.add(new WorkoutItem("Сәрсенбі", "Отырып-тұру, Отжимание, Гантелмен жаттығу", "3×10, 3×12, 3×10", false));
            items.add(new WorkoutItem("Бейсенбі", "Демалыс немесе жаяу жүру", "30 минут", false));
            items.add(new WorkoutItem("Жұма", "Отырып-тұру, Отжимание, Гантелмен жаттығу", "3×10, 3×12, 3×10", false));
            items.add(new WorkoutItem("Сенбі / Жексенбі", "Демалыс", "", false));
            items.add(new WorkoutItem("Кеңес", "Жаттығудан кейін міндетті түрде тамақтаныңыз", "", true));
        } else if (isOverweight) {
            items.add(new WorkoutItem("Жоспар", getString(R.string.workout_plan_overweight), "", false));
            items.add(new WorkoutItem("Дүйсенбі", "Кардио: жаяу жүру, велосипед", "45 минут", false));
            items.add(new WorkoutItem("Сейсенбі", "Жеңіл күш жаттығулары", "30 минут", false));
            items.add(new WorkoutItem("Сәрсенбі", "Кардио: жаяу жүру, велосипед", "45 минут", false));
            items.add(new WorkoutItem("Бейсенбі", "Жеңіл күш жаттығулары", "30 минут", false));
            items.add(new WorkoutItem("Жұма", "Кардио: жаяу жүру, велосипед", "45 минут", false));
            items.add(new WorkoutItem("Сенбі / Жексенбі", "Демалыс немесе серуен", "", false));
            items.add(new WorkoutItem("Кеңес", "Күніне 8000 қадам жүріңіз", "", true));
        } else {
            items.add(new WorkoutItem("Жоспар", getString(R.string.workout_plan_normal), "", false));
            items.add(new WorkoutItem("Дүйсенбі", "Жаяу жүру + Отжимание", "30 мин + 3×15", false));
            items.add(new WorkoutItem("Сейсенбі", "Демалыс", "", false));
            items.add(new WorkoutItem("Сәрсенбі", "Кардио + Приседание", "30 мин + 3×15", false));
            items.add(new WorkoutItem("Бейсенбі", "Демалыс", "", false));
            items.add(new WorkoutItem("Жұма", "Күш жаттығулары", "45 минут", false));
            items.add(new WorkoutItem("Жексенбі", "Йога немесе созылу", "30 минут", false));
            items.add(new WorkoutItem("Кеңес", "Қазіргі салмақты сақтаңыз", "", true));
        }
        return items;
    }
}
