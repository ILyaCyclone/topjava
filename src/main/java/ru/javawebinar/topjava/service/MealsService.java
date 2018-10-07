package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface MealsService {
    List<MealWithExceed> getWithExceed(int caloriesPerDay);
}
