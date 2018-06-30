package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealWithExceed> getFilteredWithExceeded(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOnDate = mealList.stream()
                .collect(groupingBy(m -> m.getDateTime().toLocalDate(),
                        summingInt(Meal::getCalories)));

        List<MealWithExceed> result = mealList.stream()
                .filter(meal -> isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new MealWithExceed(meal, caloriesOnDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        return result;
    }

    public static List<MealWithExceed> getFilteredWithExceededByCycles(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOnDate = new HashMap<>();
        for (Meal meal: mealList) {
            caloriesOnDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }

        List<MealWithExceed> resultList = new ArrayList<>();
        for (Meal meal: mealList) {
            if (isBetween(meal.getTime(), startTime, endTime)) {
                resultList.add(new MealWithExceed(meal, caloriesOnDate.get(meal.getDate()) > caloriesPerDay));
            }
        }

        return resultList;
    }
}
