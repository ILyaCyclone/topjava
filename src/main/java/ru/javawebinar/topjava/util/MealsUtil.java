package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    private MealsUtil(){}
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<MealWithExceed> mealsWithExceeded = getFilteredWithExceeded(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsWithExceeded.forEach(System.out::println);
    }

    public static List<MealWithExceed> getWithExceeded(Iterable<Meal> meals, int caloriesPerDay) {
        // or use filter with LocalTime.MIN & LocalTime.MAX
        Predicate<Meal> mealPredicate = (m -> true);
        return getFilteredWithExceededInOnePass(meals, mealPredicate, caloriesPerDay);
    }
    public static List<MealWithExceed> getFilteredWithExceeded(Iterable<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Predicate<Meal> mealPredicate = (m -> TimeUtil.isBetween(m.getTime(), startTime, endTime));
        return getFilteredWithExceededInOnePass(meals, mealPredicate, caloriesPerDay);
    }




    private static List<MealWithExceed> getFilteredWithExceededInOnePass(Iterable<Meal> meals, Predicate<Meal> mealPredicate, int caloriesPerDay) {
        Collection<List<Meal>> list = StreamSupport.stream(meals.spliterator(), false)
                .collect(Collectors.groupingBy(Meal::getDate)).values();

        return list.stream().flatMap(dayMeals -> {
            boolean exceed = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
            return dayMeals.stream()
                    .filter(mealPredicate)
                    .map(meal -> createWithExceed(meal, exceed));
        }).collect(toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}