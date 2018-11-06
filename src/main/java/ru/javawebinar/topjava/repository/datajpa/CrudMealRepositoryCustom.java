package ru.javawebinar.topjava.repository.datajpa;

import ru.javawebinar.topjava.model.Meal;

public interface CrudMealRepositoryCustom {
    Meal save(Meal meal, int userId);
}
