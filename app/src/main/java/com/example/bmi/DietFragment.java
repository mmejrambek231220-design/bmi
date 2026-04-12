package com.example.bmi;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    // 0=hot/featured, 1=Арықтау, 2=Таңғы ас, 3=Түскі ас, 4=Кешкі ас, 5=Салмақ жинау, 6=Салаттар
    private static final int SECTION_COUNT = 6;
    private final MealHorizontalAdapter[] sectionAdapters = new MealHorizontalAdapter[SECTION_COUNT];
    private final List<MealItem>[] allSectionItems = new List[SECTION_COUNT];
    private final View[] sectionViews = new View[SECTION_COUNT];

    private LinearLayout llCategories;
    private String searchQuery = "";
    private int selectedCategory = 0; // 0=all

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        for (int i = 0; i < SECTION_COUNT; i++) {
            allSectionItems[i] = buildSectionMeals(i);
        }

        // Hot (featured) RecyclerView
        RecyclerView rvHot = view.findViewById(R.id.rv_hot);
        rvHot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        MealHorizontalAdapter hotAdapter = new MealHorizontalAdapter(new ArrayList<>(allSectionItems[0]), true);
        rvHot.setAdapter(hotAdapter);
        sectionAdapters[0] = hotAdapter;
        sectionViews[0] = view.findViewById(R.id.section_hot);

        // Section RecyclerViews
        int[] rvIds = {
            R.id.rv_lose_weight, R.id.rv_breakfast, R.id.rv_lunch,
            R.id.rv_dinner, R.id.rv_weight_gain, R.id.rv_salads
        };
        int[] sectionIds = {
            R.id.section_lose_weight, R.id.section_breakfast, R.id.section_lunch,
            R.id.section_dinner, R.id.section_weight_gain, R.id.section_salads
        };

        for (int i = 1; i < SECTION_COUNT; i++) {
            sectionViews[i] = view.findViewById(sectionIds[i - 1]);
            RecyclerView rv = view.findViewById(rvIds[i - 1]);
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

        // Filter button toggles category visibility
        ImageButton btnFilter = view.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(v -> applyFilters());

        llCategories = view.findViewById(R.id.ll_categories);
        setupCategoryChips();
        return view;
    }

    private void setupCategoryChips() {
        Context ctx = requireContext();
        String[] labels = {
            "Барлығы", "Арықтау", "Таңғы ас", "Түскі ас",
            "Кешкі ас", "Салмақ жинау", "Салаттар"
        };
        float density = ctx.getResources().getDisplayMetrics().density;
        int hPad = (int)(20 * density);
        int vPad = (int)(10 * density);
        int marginEnd = (int)(8 * density);

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
            chip.setTextColor(ContextCompat.getColor(ctx, R.color.vitality_on_surface_variant));
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
        // sectionViews[0] = hot (always show)
        // sectionViews[1..6] correspond to categories 1..6
        for (int i = 0; i < SECTION_COUNT; i++) {
            boolean show;
            if (i == 0) {
                show = selectedCategory == 0; // hot only in "all" view
            } else {
                show = selectedCategory == 0 || selectedCategory == i;
            }
            if (sectionViews[i] != null) {
                sectionViews[i].setVisibility(show ? View.VISIBLE : View.GONE);
            }
            if (show && sectionAdapters[i] != null) {
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

    private List<MealItem> buildSectionMeals(int sectionIndex) {
        switch (sectionIndex) {
            case 0: return buildHotItems();
            case 1: return buildLoseWeightItems();
            case 2: return buildBreakfastItems();
            case 3: return buildLunchItems();
            case 4: return buildDinnerItems();
            case 5: return buildWeightGainItems();
            case 6: return buildSaladItems();
            default: return new ArrayList<>();
        }
    }

    // ── 0. Featured / Hot ──────────────────────────────────────
    private List<MealItem> buildHotItems() {
        List<MealItem> hot = new ArrayList<>();
        // From diet.txt category - Арықтауға арналған
        hot.add(new MealItem(
            "Гриль тауық пен көкөніс", "250 ккал • 15 мин",
            "Төмен калориялы, ақуызға бай тағам.",
            "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=900",
            "Арықтау",
            "Төмен калориялы, ақуызға бай тағам.",
            "тауық еті, брокколи, сәбіз, зәйтүн майы",
            "1) Тауықты дәмдеуішпен маринадтау\n2) Көкөністерді турау\n3) Грильге қою\n4) 15 минут пісіру\n5) Дайын күйінде ұсыну",
            "35 г", "10 г", "5 г", "15 мин"));
        hot.add(new MealItem(
            "Салат тунецпен", "180 ккал • 10 мин",
            "Диеталық салат.",
            "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
            "Арықтау",
            "Диеталық салат.",
            "тунец, жапырақ салат, қызанақ",
            "1) Ингредиенттерді турау\n2) Тунец қосу\n3) Май қосу\n4) Араластыру\n5) Ұсыну",
            "25 г", "5 г", "8 г", "10 мин"));
        hot.add(new MealItem(
            "Лосось боулы", "380 ккал • 20 мин",
            "Ақуыз бен омегаға бай ыстық ұсыныс.",
            "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
            "Түскі ас",
            "Ақуыз бен омегаға бай тағам.",
            "лосось, күріш, авокадо, көкөніс",
            "1) Күрішті пісіру\n2) Лосось грильге\n3) Авокадо кесу\n4) Жинау\n5) Ұсыну",
            "36 г", "40 г", "18 г", "20 мин"));
        hot.add(new MealItem(
            "Жасыл смузи боул", "220 ккал • 5 мин",
            "Витаминге бай таңғы смузи.",
            "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=900",
            "Таңғы ас",
            "Витаминге бай жеңіл таңғы ас.",
            "банан, шпинат, сүт, жидек",
            "1) Барлығын блендерге\n2) Ұру\n3) Кесеге құю\n4) Тәттілікпен безендіру",
            "8 г", "35 г", "3 г", "5 мин"));
        return hot;
    }

    // ── 1. Арықтау (diet.txt барлық тағамдары) ─────────────────
    private List<MealItem> buildLoseWeightItems() {
        List<MealItem> list = new ArrayList<>();
        // 1) Гриль тауық пен көкөніс
        list.add(new MealItem(
            "Гриль тауық пен көкөніс", "250 ккал",
            "Төмен калориялы, ақуызға бай тағам.",
            "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=900",
            "Арықтау",
            "Төмен калориялы, ақуызға бай тағам.",
            "тауық еті, брокколи, сәбіз, зәйтүн майы",
            "1) Тауықты дәмдеуішпен маринадтау\n2) Көкөністерді турау\n3) Грильге қою\n4) 15 минут пісіру\n5) Дайын күйінде ұсыну",
            "35 г", "10 г", "5 г", "15 мин"));
        // 2) Овсянка жидекпен
        list.add(new MealItem(
            "Овсянка жидекпен", "220 ккал",
            "Таңғы асқа жеңіл әрі пайдалы.",
            "https://images.unsplash.com/photo-1495214783159-3503fd1b572d?w=900",
            "Арықтау",
            "Таңғы асқа жеңіл әрі пайдалы.",
            "сұлы, сүт, жидек",
            "1) Сүтті қайнату\n2) Сұлы қосу\n3) 5 минут пісіру\n4) Жидек қосу\n5) Ұсыну",
            "10 г", "40 г", "5 г", "10 мин"));
        // 3) Жұмыртқа омлеті
        list.add(new MealItem(
            "Жұмыртқа омлеті", "180 ккал",
            "Ақуызға бай жеңіл тағам.",
            "https://images.unsplash.com/photo-1547592180-85f173990554?w=900",
            "Арықтау",
            "Ақуызға бай жеңіл тағам.",
            "жұмыртқа, сүт, көк шөп",
            "1) Жұмыртқаны шайқау\n2) Сүт қосу\n3) Табада қуыру\n4) Көк шөп қосу\n5) Ұсыну",
            "20 г", "2 г", "10 г", "10 мин"));
        // 4) Салат тунецпен
        list.add(new MealItem(
            "Салат тунецпен", "180 ккал",
            "Диеталық салат.",
            "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
            "Арықтау",
            "Диеталық салат.",
            "тунец, жапырақ салат, қызанақ",
            "1) Ингредиенттерді турау\n2) Тунец қосу\n3) Май қосу\n4) Араластыру\n5) Ұсыну",
            "25 г", "5 г", "8 г", "10 мин"));
        // 5) Айран мен алма
        list.add(new MealItem(
            "Айран мен алма", "120 ккал",
            "Жеңіл snack.",
            "https://images.unsplash.com/photo-1567306301408-9b74779a11af?w=900",
            "Арықтау",
            "Жеңіл және пайдалы тәттілік.",
            "айран, алма",
            "1) Алманы турау\n2) Айран құю\n3) Араластыру\n4) Ұсыну",
            "5 г", "20 г", "2 г", "5 мин"));
        return list;
    }

    // ── 2. Таңғы ас ────────────────────────────────────────────
    private List<MealItem> buildBreakfastItems() {
        List<MealItem> list = new ArrayList<>();
        list.add(new MealItem(
            "Овсянка жидекпен", "220 ккал",
            "Таңғы асқа жеңіл әрі пайдалы.",
            "https://images.unsplash.com/photo-1495214783159-3503fd1b572d?w=900",
            "Таңғы ас",
            "Таңғы асқа жеңіл әрі пайдалы.",
            "сұлы, сүт, жидек",
            "1) Сүтті қайнату\n2) Сұлы қосу\n3) 5 минут пісіру\n4) Жидек қосу\n5) Ұсыну",
            "10 г", "40 г", "5 г", "10 мин"));
        list.add(new MealItem(
            "Жұмыртқа омлеті", "180 ккал",
            "Ақуызға бай жеңіл тағам.",
            "https://images.unsplash.com/photo-1547592180-85f173990554?w=900",
            "Таңғы ас",
            "Ақуызға бай жеңіл таңғы тағам.",
            "жұмыртқа, сүт, көк шөп",
            "1) Жұмыртқаны шайқау\n2) Сүт қосу\n3) Табада қуыру\n4) Көк шөп қосу\n5) Ұсыну",
            "20 г", "2 г", "10 г", "10 мин"));
        list.add(new MealItem(
            "Авокадо тост", "310 ккал",
            "Жақсы майлар мен ақуыз көзі.",
            "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=900",
            "Таңғы ас",
            "Жақсы майлар мен ақуызға бай таңғы ас.",
            "авокадо, жұмыртқа, нан",
            "1) Нанды қуыру\n2) Авокадо езу\n3) Нанға жағу\n4) Жұмыртқа қою\n5) Ұсыну",
            "12 г", "28 г", "18 г", "10 мин"));
        list.add(new MealItem(
            "Жасыл смузи", "180 ккал",
            "Витаминге бай таңғы сусын.",
            "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=900",
            "Таңғы ас",
            "Витаминге бай жасыл смузи.",
            "банан, шпинат, алма, сүт",
            "1) Барлығын блендерге салу\n2) Ұру\n3) Стаканға құю\n4) Ұсыну",
            "5 г", "30 г", "2 г", "5 мин"));
        list.add(new MealItem(
            "Йогурт парфе", "280 ккал",
            "Жеңіл әрі тойымды таңғы ас.",
            "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900",
            "Таңғы ас",
            "Жеңіл әрі тойымды таңғы ас.",
            "йогурт, жидек, гранола",
            "1) Йогуртты стаканға\n2) Жидек қосу\n3) Гранола сеу\n4) Ұсыну",
            "12 г", "35 г", "8 г", "5 мин"));
        list.add(new MealItem(
            "Айран мен алма", "120 ккал",
            "Жеңіл таңғы тәттілік.",
            "https://images.unsplash.com/photo-1567306301408-9b74779a11af?w=900",
            "Таңғы ас",
            "Жеңіл таңғы тәттілік.",
            "айран, алма",
            "1) Алманы турау\n2) Айран құю\n3) Ұсыну",
            "5 г", "20 г", "2 г", "5 мин"));
        return list;
    }

    // ── 3. Түскі ас ────────────────────────────────────────────
    private List<MealItem> buildLunchItems() {
        List<MealItem> list = new ArrayList<>();
        list.add(new MealItem(
            "Лосось боулы", "380 ккал",
            "Ақуыз бен омегаға бай тағам.",
            "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
            "Түскі ас",
            "Ақуыз бен омегаға бай тағам.",
            "лосось, күріш, авокадо, көкөніс",
            "1) Күрішті пісіру\n2) Лосось грильге\n3) Авокадо кесу\n4) Жинау\n5) Ұсыну",
            "36 г", "40 г", "18 г", "20 мин"));
        list.add(new MealItem(
            "Гриль тауық салаты", "310 ккал",
            "Жоғары ақуызды гриль тауық салаты.",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
            "Түскі ас",
            "Жоғары ақуызды гриль тауық салаты.",
            "тауық, салат жапырағы, помидор, май",
            "1) Тауықты гриль жасау\n2) Кесу\n3) Көкөніс қосу\n4) Майлау\n5) Ұсыну",
            "35 г", "15 г", "12 г", "20 мин"));
        list.add(new MealItem(
            "Цезарь салаты", "390 ккал",
            "Классикалық диеталық салат.",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
            "Түскі ас",
            "Классикалық диеталық салат.",
            "тауық, ромэн салат, пармезан, сухарики",
            "1) Тауықты пісіру\n2) Салатты кесу\n3) Цезарь соусы қосу\n4) Пармезан сеу\n5) Ұсыну",
            "30 г", "20 г", "15 г", "15 мин"));
        list.add(new MealItem(
            "Гречка боул", "290 ккал",
            "Пайдалы және тойымды түскі ас.",
            "https://images.unsplash.com/photo-1512058454905-6b841e7ad132?w=900",
            "Түскі ас",
            "Пайдалы және тойымды түскі ас.",
            "гречка, көкөніс, зәйтүн майы",
            "1) Гречканы пісіру\n2) Көкөніс турау\n3) Майлау\n4) Ұсыну",
            "12 г", "45 г", "8 г", "20 мин"));
        list.add(new MealItem(
            "Томат сорпасы", "180 ккал",
            "Жеңіл және пайдалы сорпа.",
            "https://images.unsplash.com/photo-1585032226651-759b792d1739?w=900",
            "Түскі ас",
            "Жеңіл және пайдалы сорпа.",
            "томат, пияз, сарымсақ, дәмдеуіш",
            "1) Пияз қуыру\n2) Томат қосу\n3) Пісіру\n4) Блендерлеу\n5) Ұсыну",
            "5 г", "20 г", "4 г", "25 мин"));
        list.add(new MealItem(
            "Тунец салаты", "200 ккал",
            "Жеңіл диеталық салат.",
            "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
            "Түскі ас",
            "Жеңіл диеталық салат.",
            "тунец, жапырақ салат, қызанақ, лимон",
            "1) Ингредиенттерді турау\n2) Тунец қосу\n3) Лимон шырынын қосу\n4) Ұсыну",
            "22 г", "5 г", "6 г", "10 мин"));
        return list;
    }

    // ── 4. Кешкі ас ────────────────────────────────────────────
    private List<MealItem> buildDinnerItems() {
        List<MealItem> list = new ArrayList<>();
        list.add(new MealItem(
            "Бифштекс табағы", "480 ккал",
            "Тойымды кешкі тағам.",
            "https://images.unsplash.com/photo-1558030006-450675393462?w=900",
            "Кешкі ас",
            "Тойымды кешкі тағам.",
            "сиыр еті, картоп, көкөніс",
            "1) Ет маринадтау\n2) Гриль жасау\n3) Гарнир дайындау\n4) Ұсыну",
            "40 г", "35 г", "20 г", "30 мин"));
        list.add(new MealItem(
            "Тауық күріш", "420 ккал",
            "Классикалық кешкі тағам.",
            "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=900",
            "Кешкі ас",
            "Классикалық кешкі тағам.",
            "тауық, ақ күріш, пияз, сарымсақ",
            "1) Тауықты пісіру\n2) Күрішті пісіру\n3) Бірге пісіру\n4) Ұсыну",
            "32 г", "50 г", "12 г", "35 мин"));
        list.add(new MealItem(
            "Лосось картоппен", "450 ккал",
            "Омегаға бай кешкі тағам.",
            "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=900",
            "Кешкі ас",
            "Омегаға бай кешкі тағам.",
            "лосось, картоп, лимон, дәмдеуіш",
            "1) Картопты пісіру\n2) Лосось грильге\n3) Бірге ұсыну",
            "35 г", "40 г", "18 г", "25 мин"));
        list.add(new MealItem(
            "Буррито боул", "520 ккал",
            "Тойымды мексикандық тағам.",
            "https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=900",
            "Кешкі ас",
            "Тойымды мексикандық тағам.",
            "тауық, күріш, бұршақ, авокадо",
            "1) Тауықты пісіру\n2) Күрішті пісіру\n3) Барлығын жинау\n4) Ұсыну",
            "30 г", "55 г", "15 г", "30 мин"));
        list.add(new MealItem(
            "Бұршақ ет табағы", "400 ккал",
            "Ақуызға бай кешкі тағам.",
            "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?w=900",
            "Кешкі ас",
            "Ақуызға бай кешкі тағам.",
            "ет, бұршақ, пияз, дәмдеуіш",
            "1) Еті пісіру\n2) Бұршақ қосу\n3) Пісіру\n4) Ұсыну",
            "35 г", "30 г", "12 г", "40 мин"));
        return list;
    }

    // ── 5. Салмақ жинау ────────────────────────────────────────
    private List<MealItem> buildWeightGainItems() {
        List<MealItem> list = new ArrayList<>();
        list.add(new MealItem(
            "Протеин панкейк", "450 ккал",
            "Ақуызға бай таңғы тағам.",
            "https://images.unsplash.com/photo-1536304929831-ee1ca9d44906?w=900",
            "Салмақ жинау",
            "Ақуызға бай таңғы тағам.",
            "жұмыртқа, сүт, ұн, протеин порошок",
            "1) Қамыр дайындау\n2) Панкейк пісіру\n3) Балмен ұсыну",
            "30 г", "40 г", "15 г", "15 мин"));
        list.add(new MealItem(
            "Омлет сүзбемен", "380 ккал",
            "Жоғары ақуызды таңғы тағам.",
            "https://images.unsplash.com/photo-1547592180-85f173990554?w=900",
            "Салмақ жинау",
            "Жоғары ақуызды таңғы тағам.",
            "жұмыртқа, сүзбе, шөп",
            "1) Жұмыртқаны шайқау\n2) Сүзбе қосу\n3) Пісіру\n4) Ұсыну",
            "35 г", "8 г", "20 г", "10 мин"));
        list.add(new MealItem(
            "Тауық сэндвич", "520 ккал",
            "Тойымды спортшы тағамы.",
            "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=900",
            "Салмақ жинау",
            "Тойымды спортшы тағамы.",
            "тауық, нан, авокадо, салат",
            "1) Тауықты пісіру\n2) Нанды дайындау\n3) Жинау\n4) Ұсыну",
            "40 г", "45 г", "20 г", "15 мин"));
        list.add(new MealItem(
            "Жаңғақ смузи", "400 ккал",
            "Калорияға бай смузи.",
            "https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=900",
            "Салмақ жинау",
            "Калорияға бай смузи.",
            "банан, жержаңғақ майы, сүт, протеин",
            "1) Барлығын блендерге\n2) Ұру\n3) Ұсыну",
            "25 г", "50 г", "15 г", "5 мин"));
        list.add(new MealItem(
            "Сиыр еті боул", "550 ккал",
            "Күш жаттығуына арналған тағам.",
            "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=900",
            "Салмақ жинау",
            "Күш жаттығуына арналған тағам.",
            "сиыр еті, күріш, броколли, соус",
            "1) Ет пісіру\n2) Күріш пісіру\n3) Жинау\n4) Ұсыну",
            "45 г", "55 г", "18 г", "30 мин"));
        return list;
    }

    // ── 6. Салаттар ────────────────────────────────────────────
    private List<MealItem> buildSaladItems() {
        List<MealItem> list = new ArrayList<>();
        list.add(new MealItem(
            "Грек салаты", "150 ккал",
            "Классикалық жеңіл грек салаты.",
            "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=900",
            "Салаттар",
            "Классикалық жеңіл грек салаты.",
            "қияр, помидор, зәйтүн, фета, пияз",
            "1) Көкөністерді кесу\n2) Фета қосу\n3) Зәйтүн майымен майлау\n4) Ұсыну",
            "6 г", "10 г", "10 г", "10 мин"));
        list.add(new MealItem(
            "Цезарь тауықпен", "380 ккал",
            "Тойымды цезарь салаты.",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
            "Салаттар",
            "Тойымды цезарь салаты.",
            "тауық, ромэн, пармезан, сухарики",
            "1) Тауықты пісіру\n2) Салатты кесу\n3) Соус қосу\n4) Ұсыну",
            "28 г", "22 г", "15 г", "15 мин"));
        list.add(new MealItem(
            "Авокадо салаты", "280 ккал",
            "Пайдалы майларға бай салат.",
            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=900",
            "Салаттар",
            "Пайдалы майларға бай салат.",
            "авокадо, помидор, лимон, шөп",
            "1) Авокадо кесу\n2) Помидор кесу\n3) Лимон шырыны қосу\n4) Ұсыну",
            "4 г", "12 г", "22 г", "10 мин"));
        list.add(new MealItem(
            "Тунец салаты", "200 ккал",
            "Диеталық тунец салаты.",
            "https://images.unsplash.com/photo-1622621614098-8ebf5dbdff86?w=900",
            "Салаттар",
            "Диеталық тунец салаты.",
            "тунец, жапырақ салат, қызанақ",
            "1) Ингредиенттерді турау\n2) Тунец қосу\n3) Майлау\n4) Ұсыну",
            "22 г", "5 г", "8 г", "10 мин"));
        list.add(new MealItem(
            "Киноа салаты", "260 ккал",
            "Суперфудтан жасалған пайдалы салат.",
            "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=900",
            "Салаттар",
            "Суперфудтан жасалған пайдалы салат.",
            "киноа, қияр, помидор, петрушка",
            "1) Киноаны пісіру\n2) Көкөніс кесу\n3) Байланыстыру\n4) Ұсыну",
            "8 г", "35 г", "8 г", "20 мин"));
        list.add(new MealItem(
            "Шпинат салаты", "180 ккал",
            "Темірге бай жасыл салат.",
            "https://images.unsplash.com/photo-1573821663912-6cf460f06de6?w=900",
            "Салаттар",
            "Темірге бай жасыл салат.",
            "шпинат, жержаңғақ, лимон, зәйтүн майы",
            "1) Шпинатты жуу\n2) Жержаңғақ қосу\n3) Майлау\n4) Ұсыну",
            "5 г", "8 г", "12 г", "5 мин"));
        return list;
    }
}
