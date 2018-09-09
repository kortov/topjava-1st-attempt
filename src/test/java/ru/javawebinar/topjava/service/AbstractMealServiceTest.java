package ru.javawebinar.topjava.service;

import org.junit.*;
import org.springframework.beans.factory.annotation.*;
import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.util.exception.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;


public abstract class AbstractMealServiceTest extends AbstractServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteFromNonExistingUserNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, 1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistingMealNotFound() {
        service.delete(0, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOthersMealNotFound() {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getOthersMealNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotExistingMealNotFound() throws Exception {
        service.get(0, USER_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateOthersMealNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotExistingMealNotFound() {
        service.update(new Meal(0, LocalDateTime.now(), "", 0), ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenDateTimesAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 23, 59), USER_ID);
        assertMatch(meals, MEALS.stream().sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()));
    }

    @Test
    public void getBetweenDateTimesMostRecentMeal() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 31, 20, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 20, 0), USER_ID);
        assertMatch(meals, MEAL6);
    }

    @Test
    public void getBetweenDateTimesEarlierThanAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 31, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 20, 0), ADMIN_ID);
        assertMatch(meals, Collections.emptyList());
    }

    @Test
    public void getBetweenDateTimesLaterThanAllMeals() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.JUNE, 2, 21, 0),
                LocalDateTime.of(2015, Month.JUNE, 3, 21, 0), ADMIN_ID);
        assertMatch(meals, Collections.emptyList());
    }
}