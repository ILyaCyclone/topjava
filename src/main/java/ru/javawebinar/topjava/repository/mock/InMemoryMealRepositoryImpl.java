package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        logger.info("initializing meals");
        //        MealsUtil.MEALS.forEach(this::save);
        List<Meal> meals = Arrays.asList(
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),

                new Meal(1, LocalDateTime.of(2011, Month.MAY, 30, 10, 0), "Завтрак 1", 500),
                new Meal(1, LocalDateTime.of(2011, Month.MAY, 30, 13, 0), "Обед 1", 1000),
                new Meal(1, LocalDateTime.of(2011, Month.MAY, 30, 20, 0), "Ужин 1", 500),
                new Meal(1, LocalDateTime.of(2011, Month.MAY, 31, 10, 0), "Завтрак 1", 1000),
                new Meal(1, LocalDateTime.of(2011, Month.MAY, 31, 13, 0), "Обед 1", 500),
                new Meal(1, LocalDateTime.of(2011, Month.MAY, 31, 20, 0), "Ужин 1", 510)
        );
        meals.forEach(this::save);
        logger.info("repository.size: {}", repository.size());
    }

    /**
     * if meal is new, save it
     * if meal is not new (has ID), fetch stored meal by id and check userId equality
     * @param meal
     * @return
     */
    @Override
    public Meal save(Meal meal) {
        Objects.requireNonNull(meal.getUserId(), "meal userId is mandatory");
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            // this would work, but wouldn't return null if meal does not belong to user
//            return repository.computeIfPresent(meal.getId()
//                    , (id, storedMeal) -> MealsUtil.mealBelongsToUser(storedMeal, meal.getUserId()) ? meal : oldMeal);

            Meal storedMeal = repository.get(meal.getId());
            if(storedMeal != null && MealsUtil.mealBelongsToUser(storedMeal, meal.getUserId())) {
                repository.put(meal.getId(), meal);
                return meal;
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean delete(int userId, int mealId) {
        Meal storedMeal = repository.get(mealId);
        if(storedMeal != null && MealsUtil.mealBelongsToUser(storedMeal, userId)) {
            repository.remove(mealId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Meal findOne(int userId, int mealId) {
        Meal storedMeal = repository.get(mealId);
        if(storedMeal != null && storedMeal.getUserId() == userId) {
            return storedMeal;
        } else {
            return null;
        }
    }

//    @Override
//    public Collection<Meal> findAll() {
//        return repository.values();
//    }

//    public List<Meal> findByUserId(int userId) {
//        return findAll().stream()
//                .filter(meal -> meal.getUserId() == userId)
//                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(Meal::getTime))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<MealWithExceed> findWithExceed(int userId, int caloriesPerDay) {
        return findFilteredWithExceed(userId, caloriesPerDay, LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
    }
        @Override
    public List<MealWithExceed> findFilteredWithExceed(int userId, int caloriesPerDay
                , LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(repository.values().stream()
                .filter(meal -> MealsUtil.mealBelongsToUser(meal, userId))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(Meal::getTime))
                .collect(Collectors.toList())
                , caloriesPerDay
                , startTime
                , endTime);
    }
}

