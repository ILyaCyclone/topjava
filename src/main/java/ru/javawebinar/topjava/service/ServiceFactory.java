package ru.javawebinar.topjava.service;

public class ServiceFactory {
    private ServiceFactory() {}

    public static MealsService createMealsService() {
        return new InMemoryMealsService();
    }
}
