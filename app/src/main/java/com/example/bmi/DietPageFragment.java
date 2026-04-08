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
    private String searchQuery = "";
    private List<MealItem> allNewFoods = new ArrayList<>();
    private List<MealItem> allBreakfast = new ArrayList<>();
    private List<MealItem> allDinner = new ArrayList<>();
    private MealHorizontalAdapter newAdapter;
    private MealHorizontalAdapter breakfastAdapter;
    private MealHorizontalAdapter dinnerAdapter;

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
        RecyclerView rvNew = view.findViewById(R.id.rv_new_foods);
        RecyclerView rvBreakfast = view.findViewById(R.id.rv_breakfast);
        RecyclerView rvDinner = view.findViewById(R.id.rv_dinner);

        int tabIndex = getArguments() != null ? getArguments().getInt(ARG_TAB_INDEX, 0) : 0;
        allNewFoods = buildMeals(tabIndex, 0);
        allBreakfast = buildMeals(tabIndex, 1);
        allDinner = buildMeals(tabIndex, 2);

        rvNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvBreakfast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDinner.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        newAdapter = new MealHorizontalAdapter(new ArrayList<>(allNewFoods), true);
        breakfastAdapter = new MealHorizontalAdapter(new ArrayList<>(allBreakfast), false);
        dinnerAdapter = new MealHorizontalAdapter(new ArrayList<>(allDinner), false);

        rvNew.setAdapter(newAdapter);
        rvBreakfast.setAdapter(breakfastAdapter);
        rvDinner.setAdapter(dinnerAdapter);
        applyFilter();

        return view;
    }

    public void setSearchQuery(String query) {
        searchQuery = query == null ? "" : query.trim().toLowerCase();
        applyFilter();
    }

    private void applyFilter() {
        if (newAdapter == null || breakfastAdapter == null || dinnerAdapter == null) {
            return;
        }
        newAdapter.updateItems(filterByQuery(allNewFoods));
        breakfastAdapter.updateItems(filterByQuery(allBreakfast));
        dinnerAdapter.updateItems(filterByQuery(allDinner));
    }

    private List<MealItem> filterByQuery(List<MealItem> source) {
        if (searchQuery.isEmpty()) return new ArrayList<>(source);
        List<MealItem> filtered = new ArrayList<>();
        for (MealItem item : source) {
            String name = item.getName().toLowerCase();
            String desc = item.getDescription().toLowerCase();
            if (name.contains(searchQuery) || desc.contains(searchQuery)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private List<MealItem> buildMeals(int tabIndex, int sectionIndex) {
        List<MealItem> list = new ArrayList<>();
        String[] names = getNames(tabIndex, sectionIndex);
        String mealType = sectionIndex == 1 ? "Таңғы ас" : sectionIndex == 2 ? "Кешкі ас" : "Жаңа тағам";
        int caloriesBase = sectionIndex == 1 ? 320 : sectionIndex == 2 ? 430 : 380;
        String[] images = {
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=900",
                "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=900",
                "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=900",
                "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=900",
                "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=900",
                "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900",
                "https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=900",
                "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=900",
                "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=900"
        };

        for (int i = 0; i < names.length; i++) {
            int calories = caloriesBase + (i * 18);
            list.add(meal(
                    names[i],
                    calories + " ккал",
                    "Тез дайындалатын, денсаулыққа пайдалы мәзір.",
                    images[i % images.length],
                    mealType,
                    "Бұл тағам " + getCategoryName(tabIndex) + " санатына кіреді және күнделікті рационға ыңғайлы.",
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

    private String getCategoryName(int tabIndex) {
        switch (tabIndex) {
            case 0: return "таңғы ас";
            case 1: return "түскі ас";
            case 2: return "кешкі ас";
            case 3: return "салмақ жинау";
            case 4: return "арықтау";
            case 5: return "салаттар";
            default: return "диета";
        }
    }

    private String[] getNames(int tabIndex, int sectionIndex) {
        String[][][] data = new String[][][] {
                { // Таңғы ас
                        {"Лосось боулы", "Көкөніс микс-боул", "Тауық филе гриль", "Қоңыр күріш табағы", "Балық пен лимон", "Киноа көк боул", "Асқабақ сорпасы", "Буға піскен көкөніс", "Түрік ноқат боулы", "Жеңіл тунец табағы"},
                        {"Сұлы мен жидек", "Чиа пуддинг", "Йогурт гранола", "Авокадо тост", "Омлет шпинат", "Қарақұмық ботқа", "Банан сұлы смузи", "Ірімшік пен жеміс", "Жұмыртқа мафин", "Тары ботқасы"},
                        {"Көкөніс бұқтырмасы", "Буға піскен балық", "Тауық пен брокколи", "Асқабақ пюресі", "Күркетауық стейкі", "Тұтас дәнді паста", "Көк салат боул", "Жасымық пен көкөніс", "Тунец салаты", "Саңырауқұлақ омлет"}
                },
                { // Түскі ас
                        {"Цезарь fit", "Грек салаты", "Киноа салаты", "Тунец салаты", "Авокадо салаты", "Қызылша фета", "Ноқат салаты", "Жасыл смузи боул", "Көкөніс салаты", "Йогурт парфе"},
                        {"Жеміс салаты", "Сүзбе салаты", "Йогурт банан", "Гранола боул", "Тост салаты", "Жидек чиа", "Көк алма салаты", "Ірімшік салаты", "Сұлы стакан", "Құрғақ жеміс боул"},
                        {"Тауық салаты", "Күркетауық салаты", "Теңіз өнім салаты", "Авокадо тунец", "Жұмыртқа салаты", "Ноқат көк салат", "Көкөніс гриль салат", "Булгур салаты", "Киноа тунец", "Қызыл қырыққабат салат"}
                },
                { // Кешкі ас
                        {"Бифштекс табағы", "Макарон ірімшік", "Күріш тауық XL", "Лосось картоп", "Буррито боул", "Сиыр еті боул", "Жаңғақ смузи", "Жержаңғақ тост", "Бұршақ ет табағы", "Құнарлы омлет"},
                        {"Банан паста тост", "Сұлы + жаңғақ", "Омлет ірімшік", "Сүзбе бал", "Йогурт жаңғақ", "Авокадо жұмыртқа", "Сүтті ботқа", "Жеміс гранола", "Протеин смузи", "Круассан жұмыртқа"},
                        {"Ет пен күріш", "Балық пен пюре", "Тауық паста", "Күркетауық картоп", "Лазанья fit", "Бұршақ ет бұқтыру", "Тунец күріш", "Котлет боул", "Палау fit", "Сиыр еті бургер fit"}
                },
                { // Салмақ жинау
                        {"Омлет + сүзбе", "Тунец боулы", "Тауық стир-фрай", "Күркетауық ролл", "Протеин панкейк", "Сүзбе жидек", "Йогурт протеин", "Жұмыртқа авокадо", "Тауық сэндвич fit", "Булгур тунец"},
                        {"Протеин сұлы", "Жұмыртқа тост", "Сүзбе банан", "Йогурт чиа", "Омлет брокколи", "Тары + ірімшік", "Протеин вафли", "Көкөніс омлет", "Жаңғақ сүзбе", "Тауық рулет"},
                        {"Тауық филе", "Тунец стейк", "Лосось гриль", "Сиыр еті fit", "Күркетауық гриль", "Жасымық котлет", "Балық + киноа", "Бұршақ тауық", "Омлет кешкі", "Сүзбе салаты"}
                },
                { // Арықтау
                        {"Гречка боул", "Көк сорпа", "Буға піскен тунец", "Қияр салат табақ", "Томат сорпасы", "Көкөніс ролл", "Асқабақ смузи", "Қырыққабат боул", "Лимон тауық", "Киноа жеңіл табақ"},
                        {"Чиа йогурт", "Қара кофе + тост", "Жидек ботқа", "Көк алма йогурт", "Омлет ақуызы", "Сүзбе нөл%", "Жеміс смузи", "Жұмсақ гранола", "Салат тост", "Күріш ботқасы"},
                        {"Брокколи балық", "Тауық буға", "Көк салат", "Жасымық сорпа", "Киноа көкөніс", "Тунец қияр", "Асқабақ пюре", "Салат жұмыртқа", "Көк бұқтырма", "Ноқат жеңіл"}
                },
                { // Салаттар
                        {"Грек салаты", "Авокадо салаты", "Тунец салаты", "Ноқат салаты", "Киноа салаты", "Түсті салат", "Сәбіз алма салат", "Қызылша фета", "Көк салат mix", "Шпинат салаты"},
                        {"Жеміс салаты", "Йогурт салат", "Сүзбе салат", "Банан жидек", "Чиа фрут", "Алма даршын", "Манго йогурт", "Гранола фрут", "Ірімшік салат", "Киви боул"},
                        {"Тауық цезарь", "Күркетауық салаты", "Теңіз өнім салат", "Авокадо тунец", "Булгур салаты", "Көкөніс гриль", "Жұмыртқа салаты", "Киноа тунец", "Ноқат көк салат", "Қырыққабат фреш"}
                }
        };
        return data[tabIndex][sectionIndex];
    }

    private MealItem meal(
            String name,
            String calories,
            String shortRecipe,
            String imageUrl,
            String mealType,
            String description,
            String ingredients,
            String steps,
            String protein,
            String carbs,
            String fat,
            String duration
    ) {
        return new MealItem(
                name, calories, shortRecipe, imageUrl, mealType, description,
                ingredients, steps, protein, carbs, fat, duration
        );
    }
}
