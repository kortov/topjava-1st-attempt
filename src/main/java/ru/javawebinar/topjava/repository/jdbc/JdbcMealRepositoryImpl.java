package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.dao.support.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.stereotype.*;
import ru.javawebinar.topjava.*;
import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.repository.*;

import java.sql.*;
import java.time.*;
import java.util.*;

@Repository
public abstract class JdbcMealRepositoryImpl<T> implements MealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    protected abstract T toDbDateTime(LocalDateTime ldt);

    @Repository
    @Profile(Profiles.POSTGRES_DB)
    public static class Java8JdbcMealRepositoryImpl extends JdbcMealRepositoryImpl<LocalDateTime> {
        public Java8JdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            super(jdbcTemplate, namedParameterJdbcTemplate);
        }

        @Override
        protected LocalDateTime toDbDateTime(LocalDateTime ldt) {
            return ldt;
        }
    }

    @Repository
    @Profile(Profiles.HSQL_DB)
    public static class TimestampJdbcMealRepositoryImpl extends JdbcMealRepositoryImpl<Timestamp> {
        public TimestampJdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            super(jdbcTemplate, namedParameterJdbcTemplate);
        }

        @Override
        protected Timestamp toDbDateTime(LocalDateTime ldt) {
            return Timestamp.valueOf(ldt);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", toDbDateTime(meal.getDateTime()))
                .addValue("user_id", userId);

        if (meal.isNew()) {
            Number newId = insertMeal.executeAndReturnKey(map);
            meal.setId(newId.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("" +
                            "UPDATE meals " +
                            "   SET description=:description, calories=:calories, date_time=:date_time " +
                            " WHERE id=:id AND user_id=:user_id"
                    , map) == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(
                "SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, toDbDateTime(startDate), toDbDateTime(endDate));
    }
}
