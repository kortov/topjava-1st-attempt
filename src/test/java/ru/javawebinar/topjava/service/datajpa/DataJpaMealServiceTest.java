package ru.javawebinar.topjava.service.datajpa;

import org.junit.*;
import org.springframework.test.context.*;
import ru.javawebinar.topjava.*;
import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.service.*;
import ru.javawebinar.topjava.util.exception.*;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.Profiles.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void testGetWithUser() throws Exception {
        Meal adminMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(adminMeal, ADMIN_MEAL1);
        UserTestData.assertMatch(adminMeal.getUser(), UserTestData.ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithUserNotFound() throws Exception {
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }
}
