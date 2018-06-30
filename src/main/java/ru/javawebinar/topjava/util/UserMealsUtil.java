package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

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
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOnDate = mealList.stream()
                .collect(groupingBy(m -> m.getDateTime().toLocalDate(),
                        summingInt(UserMeal::getCalories)));

        List<UserMealWithExceed> result = mealList.stream()
                .filter(userMeal -> isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(userMeal, caloriesOnDate.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOnDate = new HashMap<>();
        for (UserMeal userMeal: mealList) {
            LocalDateTime localDateTime = userMeal.getDateTime();
            LocalDate dateForMapKey = localDateTime.toLocalDate();
            caloriesOnDate.merge(dateForMapKey, userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMeal userMeal: mealList) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            if (isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExceed(userMeal, caloriesOnDate.get(mealDate) > caloriesPerDay));
            }
        }

        return resultList;
    }
}
