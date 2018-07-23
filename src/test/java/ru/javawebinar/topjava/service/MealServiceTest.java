package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEALS;
import static ru.javawebinar.topjava.MealTestData.USER_MEALS;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEALS.get(0).getId(), USER_ID);
        MealTestData.assertMatch(meal, USER_MEALS.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void getNotExistingMealNotFound() throws Exception {
        service.get(0, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getOthersMealNotFound() throws Exception {
        service.get(USER_MEALS.get(0).getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEALS.get(0).getId(), ADMIN_ID);
        MealTestData.assertMatch(service.getAll(ADMIN_ID), ADMIN_MEALS.get(1));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistingMealNotFound() {
        service.delete(0, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOthersMealNotFound() {
        service.delete(ADMIN_MEALS.get(0).getId(), USER_ID);
    }

    @Test
    public void getBetweenDateTimesAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 23, 59), USER_ID);
        MealTestData.assertMatch(meals,
                USER_MEALS.stream().sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()));
    }

    @Test
    public void getBetweenDateTimesMostRecentMeal() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 31, 20, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 20, 0), USER_ID);
        MealTestData.assertMatch(meals, USER_MEALS.get(USER_MEALS.size() - 1));
    }

    @Test
    public void getBetweenDateTimesEarlierThanAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 31, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 20, 0), ADMIN_ID);
        MealTestData.assertMatch(meals, Collections.emptyList());
    }

    @Test
    public void getBetweenDateTimesLaterThanAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.JUNE, 2, 21, 0),
                LocalDateTime.of(2015, Month.JUNE, 3, 21, 0), ADMIN_ID);
        MealTestData.assertMatch(meals, Collections.emptyList());
    }


    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        MealTestData.assertMatch(meals,
                USER_MEALS.stream().sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()));
    }

    @Test
    public void update() {
        service.update(ADMIN_MEALS.get(0), ADMIN_ID);
        MealTestData.assertMatch(service.get(ADMIN_MEALS.get(0).getId(), ADMIN_ID), ADMIN_MEALS.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void updateNotExistingMealNotFound() {
        service.update(new Meal(0, LocalDateTime.now(), "", 0), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateOthersMealNotFound() {
        service.update(USER_MEALS.get(0), ADMIN_ID);
    }

    @Test
    public void create() {
        Meal insertedMeal = new Meal(LocalDateTime.now(), "NEW", 1000);
        service.create(insertedMeal, ADMIN_ID);
        int id = AbstractBaseEntity.START_SEQ + 10;
        insertedMeal.setId(id);
        MealTestData.assertMatch(service.get(id, ADMIN_ID), insertedMeal);
    }
}