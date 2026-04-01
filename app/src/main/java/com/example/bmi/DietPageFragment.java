package com.example.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DietPageFragment extends Fragment {

    private static final String ARG_TAB_INDEX = "tab_index";

    public static DietPageFragment newInstance(int tabIndex) {
        DietPageFragment fragment = new DietPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_page, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_meals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int tabIndex = getArguments() != null ? getArguments().getInt(ARG_TAB_INDEX, 0) : 0;
        List<MealItem> meals = buildMeals(tabIndex);
        recyclerView.setAdapter(new MealAdapter(meals));
        return view;
    }

    private List<MealItem> buildMeals(int tabIndex) {
        List<MealItem> list = new ArrayList<>();
        switch (tabIndex) {
            case 0: // Арық адамдарға
                list.add(new MealItem(
                        "Таңғы ас: Сұлы ботқасы + 2 жұмыртқа + бал",
                        "550 ккал",
                        "Сұлыны 200мл сүтпен пісіріңіз, үстіне бал қосыңыз",
                        "https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=400",
                        "Таңғы ас"));
                list.add(new MealItem(
                        "Түскі ас: Тауық еті + күріш + көкөніс салаты",
                        "700 ккал",
                        "Тауықты пісіріп, күрішпен бірге беріңіз",
                        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400",
                        "Түскі ас"));
                list.add(new MealItem(
                        "Кешкі ас: Балық + картоп пюресі + нан",
                        "600 ккал",
                        "Балықты буда пісіріңіз",
                        "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=400",
                        "Кешкі ас"));
                list.add(new MealItem(
                        "Тәттілік: Жаңғақ, банан, сүт",
                        "300 ккал",
                        "Жаңғақ пен бананды бірге жеңіз",
                        "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?w=400",
                        "Тәттілік"));
                list.add(new MealItem(
                        "Күніне барлығы: ~2800-3200 ккал",
                        "",
                        "",
                        "",
                        "Жалпы"));
                break;

            case 1: // Қалыпты адамдарға
                list.add(new MealItem(
                        "Таңғы ас: Жұмыртқа омлеті + тост + жеміс",
                        "400 ккал",
                        "Жұмыртқаны омлет жасап, тостпен беріңіз",
                        "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=400",
                        "Таңғы ас"));
                list.add(new MealItem(
                        "Түскі ас: Сорпа + ет + салат",
                        "550 ккал",
                        "Сорпаны пісіріп, ет пен салатпен беріңіз",
                        "https://images.unsplash.com/photo-1547592180-85f173990554?w=400",
                        "Түскі ас"));
                list.add(new MealItem(
                        "Кешкі ас: Балық немесе тауық + көкөніс",
                        "450 ккал",
                        "Балықты немесе тауықты пісіріңіз",
                        "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=400",
                        "Кешкі ас"));
                list.add(new MealItem(
                        "Күніне барлығы: ~1800-2200 ккал",
                        "",
                        "",
                        "",
                        "Жалпы"));
                break;

            case 2: // Арықтау үшін
                list.add(new MealItem(
                        "Таңғы ас: Гречка ботқасы + кефир",
                        "300 ккал",
                        "Гречканы суда пісіріп, кефирмен беріңіз",
                        "https://images.unsplash.com/photo-1495214783159-3503fd1b572d?w=400",
                        "Таңғы ас"));
                list.add(new MealItem(
                        "Түскі ас: Көкөніс сорпасы + тауық кеудесі",
                        "400 ккал",
                        "Сорпаны пісіріп, тауық кеудесімен беріңіз",
                        "https://images.unsplash.com/photo-1547592180-85f173990554?w=400",
                        "Түскі ас"));
                list.add(new MealItem(
                        "Кешкі ас: Салат + пісірілген балық",
                        "300 ккал",
                        "Салат жасап, балықты буда пісіріңіз",
                        "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400",
                        "Кешкі ас"));
                list.add(new MealItem(
                        "Тәттілік: Алма, кефир",
                        "150 ккал",
                        "Алманы жеп, кефир ішіңіз",
                        "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=400",
                        "Тәттілік"));
                list.add(new MealItem(
                        "Күніне барлығы: ~1200-1500 ккал",
                        "",
                        "",
                        "",
                        "Жалпы"));
                break;
        }
        return list;
    }
}
