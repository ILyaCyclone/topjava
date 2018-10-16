package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    // false if not found
    boolean delete(int userId, int mealId);

    // null if not found
    Meal findOne(int userId, int id);

    List<MealWithExceed> findWithExceed(int userId, int caloriesPerDay);

    List<MealWithExceed> findFilteredWithExceed(int userId, int caloriesPerDay
            , LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}