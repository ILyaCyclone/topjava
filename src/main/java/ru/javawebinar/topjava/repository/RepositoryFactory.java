package ru.javawebinar.topjava.repository;

/**
 * Factory for repository classes.
 */
public class RepositoryFactory {
    private RepositoryFactory(){}

    public static MealRepository createMealRepository() {
        return new InMemoryMealsRepository();
    }
}
