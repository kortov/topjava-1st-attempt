package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.transaction.annotation.*;
import ru.javawebinar.topjava.model.*;

import java.time.*;
import java.util.*;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Override
    @Transactional
    Meal save(Meal item);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("userId") int userId);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m from Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id = ?1 and m.user.id = ?2")
    Meal getWithUser(int id, int userId);
}