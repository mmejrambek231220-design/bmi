package com.example.bmi;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.workout_title));
        }

        String category = getIntent().getStringExtra("bmi_category");
        if (category == null) category = "";

        RecyclerView recyclerView = findViewById(R.id.rv_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<WorkoutItem> items = buildWorkoutPlan(category);
        recyclerView.setAdapter(new WorkoutAdapter(items));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<WorkoutItem> buildWorkoutPlan(String category) {
        List<WorkoutItem> items = new ArrayList<>();
        boolean isUnderweight = category.contains("арықтық") || category.equals("Айқын арықтық")
                || category.equals("Орташа арықтық") || category.equals("Жеңіл арықтық");
        boolean isOverweight = category.contains("Артық") || category.contains("Семіздік");

        if (isUnderweight) {
            items.add(new WorkoutItem("Жоспар атауы", getString(R.string.workout_plan_underweight), "", false));
            items.add(new WorkoutItem("Дүйсенбі",
                    "Отырып-тұру, Отжимание, Гантелмен жаттығу",
                    "3x10, 3x12, 3x10", false));
            items.add(new WorkoutItem("Сейсенбі",
                    "Демалыс немесе жаяу жүру",
                    "30 минут", false));
            items.add(new WorkoutItem("Сәрсенбі",
                    "Отырып-тұру, Отжимание, Гантелмен жаттығу",
                    "3x10, 3x12, 3x10", false));
            items.add(new WorkoutItem("Бейсенбі",
                    "Демалыс немесе жаяу жүру",
                    "30 минут", false));
            items.add(new WorkoutItem("Жұма",
                    "Отырып-тұру, Отжимание, Гантелмен жаттығу",
                    "3x10, 3x12, 3x10", false));
            items.add(new WorkoutItem("Сенбі / Жексенбі",
                    "Демалыс",
                    "", false));
            items.add(new WorkoutItem("Кеңес",
                    "Жаттығудан кейін міндетті түрде тамақтаныңыз",
                    "", true));
        } else if (isOverweight) {
            items.add(new WorkoutItem("Жоспар атауы", getString(R.string.workout_plan_overweight), "", false));
            items.add(new WorkoutItem("Дүйсенбі",
                    "Кардио: жаяу жүру, велосипед",
                    "45 минут", false));
            items.add(new WorkoutItem("Сейсенбі",
                    "Жеңіл күш жаттығулары",
                    "30 минут", false));
            items.add(new WorkoutItem("Сәрсенбі",
                    "Кардио: жаяу жүру, велосипед",
                    "45 минут", false));
            items.add(new WorkoutItem("Бейсенбі",
                    "Жеңіл күш жаттығулары",
                    "30 минут", false));
            items.add(new WorkoutItem("Жұма",
                    "Кардио: жаяу жүру, велосипед",
                    "45 минут", false));
            items.add(new WorkoutItem("Сенбі / Жексенбі",
                    "Демалыс немесе серуен",
                    "", false));
            items.add(new WorkoutItem("Кеңес",
                    "Күніне 8000 қадам жүріңіз",
                    "", true));
        } else {
            items.add(new WorkoutItem("Жоспар атауы", getString(R.string.workout_plan_normal), "", false));
            items.add(new WorkoutItem("Дүйсенбі",
                    "Жаяу жүру + Отжимание",
                    "30 мин + 3x15", false));
            items.add(new WorkoutItem("Сейсенбі",
                    "Демалыс",
                    "", false));
            items.add(new WorkoutItem("Сәрсенбі",
                    "Кардио + Приседание",
                    "30 мин + 3x15", false));
            items.add(new WorkoutItem("Бейсенбі",
                    "Демалыс",
                    "", false));
            items.add(new WorkoutItem("Жұма",
                    "Күш жаттығулары",
                    "45 минут", false));
            items.add(new WorkoutItem("Жексенбі",
                    "Йога немесе созылу",
                    "30 минут", false));
            items.add(new WorkoutItem("Кеңес",
                    "Қазіргі салмақты сақтаңыз",
                    "", true));
        }
        return items;
    }
}
