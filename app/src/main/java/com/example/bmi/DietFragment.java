package com.example.bmi;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DietFragment extends Fragment {

    private final MealHorizontalAdapter[] sectionAdapters = new MealHorizontalAdapter[6];
    private final List<MealItem>[] allSectionItems = new List[6];
    private final View[] sectionViews = new View[6];

    private LinearLayout llCategories;
    private String searchQuery = "";
    private int selectedCategory = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        // Prepare data
        for (int i = 0; i < 6; i++) {
            allSectionItems[i] = buildSectionMeals(i);
        }

        // Hot RecyclerView
        RecyclerView rvHot = view.findViewById(R.id.rv_hot);
        rvHot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvHot.setAdapter(new MealHorizontalAdapter(buildHotItems(), true));

        // Section RecyclerViews
        int[] rvIds = {
            R.id.rv_breakfast, R.id.rv_lunch, R.id.rv_dinner,
            R.id.rv_weight_gain, R.id.rv_lose_weight, R.id.rv_salads
        };
        int[] sectionIds = {
            R.id.section_breakfast, R.id.section_lunch, R.id.section_dinner,
            R.id.section_weight_gain, R.id.section_lose_weight, R.id.section_salads
        };

        for (int i = 0; i < 6; i++) {
            sectionViews[i] = view.findViewById(sectionIds[i]);
            RecyclerView rv = view.findViewById(rvIds[i]);
            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            sectionAdapters[i] = new MealHorizontalAdapter(new ArrayList<>(allSectionItems[i]), false);
            rv.setAdapter(sectionAdapters[i]);
        }

        // Search
        EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s == null ? "" : s.toString().toLowerCase().trim();
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Filter toggle button
        ImageButton btnFilter = view.findViewById(R.id.btn_filter);
        HorizontalScrollView hsvCategories = view.findViewById(R.id.hsv_categories);
        llCategories = view.findViewById(R.id.ll_categories);

        btnFilter.setOnClickListener(v -> {
            boolean visible = hsvCategories.getVisibility() == View.VISIBLE;
            hsvCategories.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        setupCategoryChips();
        return view;
    }

    private void setupCategoryChips() {
        Context ctx = requireContext();
        String[] labels = {
            "Барлығы", "Таңғы ас", "Түскі ас", "Кешкі ас",
            "Салмақ жинау", "Арықтау", "Салаттар"
        };
        float density = ctx.getResources().getDisplayMetrics().density;
        int hPad = (int)(16 * density);
        int vPad = (int)(9 * density);
        int marginEnd = (int)(10 * density);

        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            TextView chip = new TextView(ctx);
            chip.setText(labels[i]);
            chip.setTextSize(13.5f);
            chip.setPadding(hPad, vPad, hPad, vPad);
            chip.setTag(index);
            applyChipStyle(chip, index == 0);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(marginEnd);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                selectedCategory = index;
                refreshChips();
                applyFilters();
            });
            llCategories.addView(chip);
        }
    }

    private void applyChipStyle(TextView chip, boolean active) {
        Context ctx = requireContext();
        if (active) {
            chip.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_category_active));
            chip.setTextColor(0xFFFFFFFF);
        } else {
            chip.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_category_default));
            chip.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
        }
    }

    private void refreshChips() {
        for (int i = 0; i < llCategories.getChildCount(); i++) {
            View child = llCategories.getChildAt(i);
            if (child instanceof TextView) {
                applyChipStyle((TextView) child, (int) child.getTag() == selectedCategory);
            }
        }
    }

    private void applyFilters() {
        for (int i = 0; i < 6; i++) {
            boolean show = selectedCategory == 0 || selectedCategory == (i + 1);
            sectionViews[i].setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                sectionAdapters[i].updateItems(filterByQuery(allSectionItems[i]));
            }
        }
    }

    private List<MealItem> filterByQuery(List<MealItem> source) {
        if (searchQuery.isEmpty()) return new ArrayList<>(source);
        List<MealItem> result = new ArrayList<>();
        for (MealItem item : source) {
            if (item.getName().toLowerCase().contains(searchQuery) ||
                    item.getDescription().toLowerCase().contains(searchQuery)) {
                result.add(item);
            }
        }
        return result;
    }

    // ──────────────────────────────────────────────────────────
    //  Data builders
    // ──────────────────────────────────────────────────────────

    private List<MealItem> buildHotItems() {
        List<MealItem> hot = new ArrayList<>();
        hot.add(new MealItem(
            "Brown Rice with Salmon", "475 kcal", "Premium healthy bowl",
            "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
            "Түскі ас",
            "Ақуыз бен омегаға бай ыстық ұсыныс.",
            "• Лосось\n• Қоңыр күріш\n• Көкөніс",
            "1) Пісіріңіз\n2) Араластырыңыз\n3) Ұсыныңыз",
            "36 g", "63 g", "25 g", "25 min"));
        hot.add(new MealItem(
            "Greek Yogurt Parfait", "305 kcal", "Fruity breakfast cup",
            "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900",
            "Таңғы ас",
            "Жеңіл әрі тойымды таңғы ас.",
            "• Йогурт\n• Жидек\n• Гранола",
            "1) Қабаттаңыз\n2) Салқын беріңіз",
            "17 g", "32 g", "11 g", "8 min"));
        hot.add(new MealItem(
            "Avocado Toast Egg", "330 kcal", "Clean energy breakfast",
            "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=900",
            "Таңғы ас",
            "Жақсы майлар мен ақуыз көзі.",
            "• Авокадо\n• Жұмыртқа\n• Тост",
            "1) Қуырыңыз\n2) Жинаңыз",
            "16 g", "28 g", "19 g", "10 min"));
        hot.add(new MealItem(
            "Grilled Chicken Salad", "390 kcal", "Fresh & filling",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
            "Түскі ас",
            "Жоғары ақуызды гриль тауық салаты.",
            "• Тауық\n• Салат жапырағы\n• Помидор",
            "1) Гриль жасаңыз\n2) Кесіңіз\n3) Қосыңыз",
            "42 g", "18 g", "14 g", "20 min"));
        return hot;
    }

    private List<MealItem> buildSectionMeals(int sectionIndex) {
        String[][][] names = {
            { // 0 – Таңғы ас
                {"Лосось боулы","Көкөніс микс-боул","Тауық филе гриль","Қоңыр күріш табағы",
                 "Балық пен лимон","Киноа көк боул","Асқабақ сорпасы","Буға піскен көкөніс",
                 "Түрік ноқат боулы","Жеңіл тунец табағы"}
            },
            { // 1 – Түскі ас
                {"Цезарь fit","Грек салаты","Киноа салаты","Тунец салаты",
                 "Авокадо салаты","Қызылша фета","Ноқат салаты","Жасыл смузи боул",
                 "Көкөніс салаты","Йогурт парфе"}
            },
            { // 2 – Кешкі ас
                {"Бифштекс табағы","Макарон ірімшік","Күріш тауық XL","Лосось картоп",
                 "Буррито боул","Сиыр еті боул","Жаңғақ смузи","Жержаңғақ тост",
                 "Бұршақ ет табағы","Құнарлы омлет"}
            },
            { // 3 – Салмақ жинау
                {"Омлет + сүзбе","Тунец боулы","Тауық стир-фрай","Күркетауық ролл",
                 "Протеин панкейк","Сүзбе жидек","Йогурт протеин","Жұмыртқа авокадо",
                 "Тауық сэндвич fit","Булгур тунец"}
            },
            { // 4 – Арықтау
                {"Гречка боул","Көк сорпа","Буға піскен тунец","Қияр салат табақ",
                 "Томат сорпасы","Көкөніс ролл","Асқабақ смузи","Қырыққабат боул",
                 "Лимон тауық","Киноа жеңіл табақ"}
            },
            { // 5 – Салаттар
                {"Грек салаты","Авокадо салаты","Тунец салаты","Ноқат салаты",
                 "Киноа салаты","Түсті салат","Сәбіз алма салат","Қызылша фета",
                 "Көк салат mix","Шпинат салаты"}
            }
        };

        String[][] sectionImages = {
            { // 0 – Таңғы ас: salmon bowl, veggie bowl, grilled chicken, brown rice, fish lemon,
              //               quinoa, pumpkin soup, steamed veg, chickpea, tuna
                "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
                "https://images.unsplash.com/photo-1540189549336-e6e99eb4b272?w=900",
                "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=900",
                "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=900",
                "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=900",
                "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=900",
                "https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?w=900",
                "https://images.unsplash.com/photo-1512058454905-6b841e7ad132?w=900",
                "https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?w=900",
                "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=900"
            },
            { // 1 – Түскі ас: caesar, greek salad, quinoa salad, tuna salad, avocado,
              //               beet feta, chickpea, green smoothie bowl, veg salad, yogurt parfait
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
                "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=900",
                "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=900",
                "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
                "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=900",
                "https://images.unsplash.com/photo-1540914124281-342587941389?w=900",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=900",
                "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=900",
                "https://images.unsplash.com/photo-1573821663912-6cf460f06de6?w=900",
                "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900"
            },
            { // 2 – Кешкі ас: steak, mac cheese, chicken rice, salmon potato, burrito bowl,
              //               beef bowl, nut smoothie, peanut toast, bean meat, omelette
                "https://images.unsplash.com/photo-1558030006-450675393462?w=900",
                "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=900",
                "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=900",
                "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=900",
                "https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=900",
                "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=900",
                "https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=900",
                "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=900",
                "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?w=900",
                "https://images.unsplash.com/photo-1547592180-85f173990554?w=900"
            },
            { // 3 – Салмақ жинау: omelette cottage, tuna bowl, chicken stir fry, turkey roll,
              //               protein pancake, cottage berry, yogurt protein, egg avocado, chicken sandwich, bulgur tuna
                "https://images.unsplash.com/photo-1547592180-85f173990554?w=900",
                "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
                "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=900",
                "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=900",
                "https://images.unsplash.com/photo-1536304929831-ee1ca9d44906?w=900",
                "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900",
                "https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?w=900",
                "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=900",
                "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=900",
                "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=900"
            },
            { // 4 – Арықтау: buckwheat bowl, green soup, steamed tuna, cucumber salad,
              //              tomato soup, veggie roll, pumpkin smoothie, cabbage bowl, lemon chicken, quinoa light
                "https://images.unsplash.com/photo-1512058454905-6b841e7ad132?w=900",
                "https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?w=900",
                "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=900",
                "https://images.unsplash.com/photo-1540189549336-e6e99eb4b272?w=900",
                "https://images.unsplash.com/photo-1585032226651-759b792d1739?w=900",
                "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=900",
                "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=900",
                "https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=900",
                "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=900",
                "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=900"
            },
            { // 5 – Салаттар: greek, avocado, tuna, chickpea, quinoa, colorful, carrot apple, beet feta, green mix, spinach
                "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=900",
                "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=900",
                "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=900",
                "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=900",
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
                "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?w=900",
                "https://images.unsplash.com/photo-1540914124281-342587941389?w=900",
                "https://images.unsplash.com/photo-1573821663912-6cf460f06de6?w=900",
                "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=900"
            }
        };

        String[] sectionMealTypes = {
            "Таңғы ас", "Түскі ас", "Кешкі ас",
            "Салмақ жинау", "Арықтау", "Салаттар"
        };
        String[] categoryNames = {
            "таңғы ас", "түскі ас", "кешкі ас",
            "салмақ жинау", "арықтау", "салаттар"
        };
        int[] calorieBases = {380, 310, 430, 450, 280, 240};

        String[] rowNames = names[sectionIndex][0];
        List<MealItem> list = new ArrayList<>();
        for (int i = 0; i < rowNames.length; i++) {
            int cal = calorieBases[sectionIndex] + i * 15;
            list.add(new MealItem(
                rowNames[i],
                cal + " ккал",
                "Тез дайындалатын, денсаулыққа пайдалы мәзір.",
                sectionImages[sectionIndex][i],
                sectionMealTypes[sectionIndex],
                "Бұл тағам " + categoryNames[sectionIndex] + " санатына кіреді және күнделікті рационға ыңғайлы.",
                "• Негізгі ингредиенттер\n• Дәмдеуіштер\n• Көкөніс қоспасы",
                "1) Ингредиенттерді дайындаңыз.\n2) 10-15 минут пісіріңіз.\n3) Жылы күйінде ұсыныңыз.",
                (20 + i) + " г",
                (28 + i * 2) + " г",
                (10 + i) + " г",
                (15 + i) + " мин"
            ));
        }
        return list;
    }
}
