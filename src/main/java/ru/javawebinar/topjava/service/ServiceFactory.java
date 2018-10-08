package ru.javawebinar.topjava.service;

/**
 * Factory for service classes.
 */
public class ServiceFactory {
    private ServiceFactory() {}

    public static MealsService createMealsService() {
        return new DefaultMealsService();
    }
}
