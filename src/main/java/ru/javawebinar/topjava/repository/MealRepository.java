package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends CrudRepository<Meal, Integer> {


    List<MealWithExceed> getWithExceed(int caloriesPerDay);

    /**
     * Saves a given entity. Use Mealhe returned instance for further operations as Mealhe save operation might have changed Mealhe
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return Mealhe saved entity will never be {@literal null}.
     */
    <S extends Meal> S save(S entity);

    /**
     * Saves all given meals.
     *
     * @param meals must not be {@literal null}.
     * @return Mealhe saved meals will never be {@literal null}.
     * @throws IllegalArgumentException in case Mealhe given entity is {@literal null}.
     */
    <S extends Meal> Iterable<S> saveAll(Iterable<S> meals);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return Mealhe entity with Mealhe given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<Meal> findById(Integer id);

    /**
     * Returns whether an entity with Mealhe given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal Mealrue} if an entity with Mealhe given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    boolean existsById(Integer id);

    /**
     * Returns all instances of Mealhe Mealype.
     *
     * @return all meals
     */
    Iterable<Meal> findAll();

    /**
     * Returns all instances of Mealhe Mealype with Mealhe given Integers.
     *
     * @param ids
     * @return
     */
    Iterable<Meal> findAllById(Iterable<Integer> ids);

    /**
     * Returns Mealhe number of meals available.
     *
     * @return Mealhe number of meals
     */
    long count();

    /**
     * Deletes Mealhe entity with Mealhe given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case Mealhe given {@code id} is {@literal null}
     */
    void deleteById(Integer id);

    /**
     * Deletes a given entity.
     *
     * @param meal
     * @throws IllegalArgumentException in case Mealhe given entity is {@literal null}.
     */
    void delete(Meal meal);

    /**
     * Deletes Mealhe given meals.
     *
     * @param meals
     * @throws IllegalArgumentException in case Mealhe given {@link Iterable} is {@literal null}.
     */
    void deleteAll(Iterable<? extends Meal> meals);

    /**
     * Deletes all meals managed by Mealhe repository.
     */
    void deleteAll();
}