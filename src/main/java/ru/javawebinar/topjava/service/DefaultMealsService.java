package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.RepositoryFactory;

import java.util.List;
import java.util.Optional;

/**
 * delegates all standard CRUD methods to MealRepository
 */
class DefaultMealsService implements MealsService {

    private final MealRepository mealsRepository;

    public DefaultMealsService() {
        mealsRepository = RepositoryFactory.createMealRepository();
    }

    @Override
    public List<MealWithExceed> getWithExceed(int caloriesPerDay) {
        return mealsRepository.getWithExceed(caloriesPerDay);
    }

    @Override
    public <S extends Meal> S save(S meal) {
        return mealsRepository.save(meal);
    }

    @Override
    public <S extends Meal> Iterable<S> saveAll(Iterable<S> meals) {
        return mealsRepository.saveAll(meals);
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return mealsRepository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return mealsRepository.existsById(id);
    }

    @Override
    public Iterable<Meal> findAll() {
        return mealsRepository.findAll();
    }

    @Override
    public Iterable<Meal> findAllById(Iterable<Integer> ids) {
        return mealsRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return mealsRepository.count();
    }

    @Override
    public void deleteById(Integer id) {
        mealsRepository.deleteById(id);
    }

    @Override
    public void delete(Meal meal) {
        mealsRepository.delete(meal);
    }

    @Override
    public void deleteAll(Iterable<? extends Meal> meals) {
        mealsRepository.deleteAll(meals);
    }

    @Override
    public void deleteAll() {
        mealsRepository.deleteAll();
    }
}
