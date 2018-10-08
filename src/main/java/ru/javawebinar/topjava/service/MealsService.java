package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;
import java.util.Optional;

/**
 * methods signature from Spring Data CrudRepository
 */
public interface MealsService {

    List<MealWithExceed> getWithExceed(int caloriesPerDay);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param meal must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    <S extends Meal> S save(S meal);

    /**
     * Saves all given entities.
     *
     * @param meals must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    <S extends Meal> Iterable<S> saveAll(Iterable<S> meals);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<Meal> findById(Integer id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    boolean existsById(Integer id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Iterable<Meal> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    Iterable<Meal> findAllById(Iterable<Integer> ids);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void deleteById(Integer id);

    /**
     * Deletes a given entity.
     *
     * @param meal
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(Meal meal);

    /**
     * Deletes the given entities.
     *
     * @param meals
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void deleteAll(Iterable<? extends Meal> meals);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();
}
