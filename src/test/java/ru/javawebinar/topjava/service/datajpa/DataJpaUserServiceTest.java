package ru.javawebinar.topjava.service.datajpa;

import org.junit.*;
import org.springframework.test.context.*;
import ru.javawebinar.topjava.*;
import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.service.*;
import ru.javawebinar.topjava.util.exception.*;

import static ru.javawebinar.topjava.Profiles.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), MealTestData.MEALS);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithMealsNotFound() throws Exception {
        service.getWithMeals(1);
    }
}