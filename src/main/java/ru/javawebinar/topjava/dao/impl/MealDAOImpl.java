package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="https://stackoverflow.com/a/31854892/9466638"></a>
 */
public class MealDAOImpl implements MealDAO {
    private static Map<Long, Meal> meals = new ConcurrentHashMap<>();

    static {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        for (Meal meal : mealList) {
            meals.put(meal.getId(), meal);
        }
    }

    @Override
    public void addItem(Meal item) {
        meals.put(item.getId(), item);
    }

    @Override
    public void updateItem(Meal item) {
        meals.put(item.getId(), item);
    }

    @Override
    public void removeItem(Meal item) {
        meals.remove(item.getId());
    }

    @Override
    public void removeItem(long id) {
        meals.remove(id);
    }

    @Override
    public Meal getItem(long id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

}
