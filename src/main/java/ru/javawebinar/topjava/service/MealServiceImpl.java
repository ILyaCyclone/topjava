package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void update(Meal meal) throws NotFoundException {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    @Override
    public void delete(int userId, int mealId) throws NotFoundException {
        checkNotFound(repository.delete(userId, mealId), "Not found meal with id = "+mealId+" and userId = "+userId);
    }

    @Override
    public Meal get(int userId, int mealId) throws NotFoundException {
        return checkNotFoundWithId(repository.findOne(userId, mealId), mealId);
    }

    @Override
    public List<MealWithExceed> getWithExceed(int userId, int caloriesPerDay) {
        return repository.findWithExceed(userId, caloriesPerDay);
    }

    @Override
    public List<MealWithExceed> getFilteredWithExceed(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return repository.findFilteredWithExceed(userId, caloriesPerDay, startDate, endDate, startTime, endTime);
    }
}