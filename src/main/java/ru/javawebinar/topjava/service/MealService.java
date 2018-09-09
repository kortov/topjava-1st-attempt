package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.util.exception.*;

import java.time.*;
import java.util.*;

public interface MealService {
    Meal get(int id, int userId) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    default List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate, int userId) {
        return getBetweenDateTimes(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX), userId);
    }

    List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    List<Meal> getAll(int userId);

    void update(Meal meal, int userId) throws NotFoundException;

    Meal create(Meal meal, int userId);

    Meal getWithUser(int id, int userId);
}