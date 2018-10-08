package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InMemoryMealRepository implements MealRepository {

    // map key and Meal::getId must be equals
    private static final Map<Integer, Meal> map;
    private static final AtomicInteger idCounter;

    static {
        map = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger(0);

        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        meals.forEach(meal -> {
            meal.setId(idCounter.getAndIncrement());
            map.put(meal.getId(), meal);
        });
    }

    @Override
    public <S extends Meal> S save(S meal) {
        if(Objects.isNull(meal.getId())) {
            meal.setId(idCounter.getAndIncrement());
        }

        map.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public <S extends Meal> Iterable<S> saveAll(Iterable<S> meals) {
        return StreamSupport.stream(meals.spliterator(), false)
                .map(meal -> save(meal))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return Optional.of(map.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return map.containsKey(id);
    }

    @Override
    public Iterable<Meal> findAll() {
        return map.values();
    }

    @Override
    public Iterable<Meal> findAllById(Iterable<Integer> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(id -> findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public void deleteById(Integer id) {
        map.remove(id);
    }

    @Override
    public void delete(Meal meal) {
        if(map.containsKey(meal.getId())) {
            map.remove(meal.getId());
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Meal> meals) {
        meals.forEach(meal -> delete(meal));
    }

    @Override
    public void deleteAll() {
        map.clear();
    }
}
