package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            log.debug("saved {}", meal);
            return meal;
        }
        meal.setUserId(userId);
        // treat case: update, but absent in storage
        Meal updatingMeal = repository.computeIfPresent(meal.getId(),
                (id, oldMeal) -> oldMeal.getUserId().equals(userId) ? meal : oldMeal);
        log.debug("saved {}", meal);
        return updatingMeal;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete {}", id);
        return repository.remove(id, get(id, userId));
    }

    @Override
    public Meal get(int id, Integer userId) {
        log.info("get {}", id);
        Meal result = repository.getOrDefault(id, new Meal(null, null, 0));
        return userId.equals(result.getUserId()) ? result : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.info("getAll");
        return repository.values().parallelStream()
                .filter(meal -> userId.equals(meal.getUserId()))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

