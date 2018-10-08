package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.RepositoryFactory;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Optional;

/**
 * delegates standard CRUD methods to MealRepository
 */
class DefaultMealService implements MealService {

    private final MealRepository mealRepository;

    public DefaultMealService() {
        mealRepository = RepositoryFactory.createMealRepository();
    }

    @Override
    public List<MealWithExceed> getWithExceed(int caloriesPerDay) {
        return MealsUtil.getWithExceeded(findAll(), caloriesPerDay);
    }

    @Override
    public <S extends Meal> S save(S meal) {
        return mealRepository.save(meal);
    }

    @Override
    public <S extends Meal> Iterable<S> saveAll(Iterable<S> meals) {
        return mealRepository.saveAll(meals);
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return mealRepository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return mealRepository.existsById(id);
    }

    @Override
    public Iterable<Meal> findAll() {
        return mealRepository.findAll();
    }

    @Override
    public Iterable<Meal> findAllById(Iterable<Integer> ids) {
        return mealRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return mealRepository.count();
    }

    @Override
    public void deleteById(Integer id) {
        mealRepository.deleteById(id);
    }

    @Override
    public void delete(Meal meal) {
        mealRepository.delete(meal);
    }

    @Override
    public void deleteAll(Iterable<? extends Meal> meals) {
        mealRepository.deleteAll(meals);
    }

    @Override
    public void deleteAll() {
        mealRepository.deleteAll();
    }
}
