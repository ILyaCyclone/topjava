package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

/**
 * specifies concrete types of generic CrudRepository<T, ID>
 */
public interface MealRepository extends CrudRepository<Meal, Integer> {

}