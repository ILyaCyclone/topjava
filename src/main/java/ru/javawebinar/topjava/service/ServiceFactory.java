package ru.javawebinar.topjava.service;

/**
 * Factory for service classes.
 */
public class ServiceFactory {
    private ServiceFactory() {}

    public static MealService createMealsService() {
        return new DefaultMealService();
    }
}
