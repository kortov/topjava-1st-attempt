package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealsUtilTest {

    @Test
    void getFilteredWithExceeded() {
        List<Meal> tested = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<MealWithExceed> expected = Arrays.asList(
                MealsUtil.createWithExceed(tested.get(0), false),
                MealsUtil.createWithExceed(tested.get(3), true)
        );


        assertEquals(expected, MealsUtil.getFilteredWithExceeded(tested, 2000, LocalTime.of(7, 0), LocalTime.of(12, 0)));
    }

    @Test
    void getFilteredWithExceededByCycles() {
        List<Meal> tested = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<MealWithExceed> expected = Arrays.asList(
                MealsUtil.createWithExceed(tested.get(0), false),
                MealsUtil.createWithExceed(tested.get(3), true)
        );


        assertEquals(expected, MealsUtil.getFilteredWithExceededByCycles(tested, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

}