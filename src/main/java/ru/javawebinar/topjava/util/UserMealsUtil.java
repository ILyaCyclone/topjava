package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class UserMealsUtil {
    public static void main(String[] args) {

        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    /**
     * должны возвращаться только записи между startTime и endTime
     * поле UserMealWithExceed.exceed должно показывать, превышает ли сумма калорий за весь день параметра метода caloriesPerDay
     */
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // we use either of two methods, so suppress "unused" warnings
        return getFilteredWithExceededUsingStreams(mealList, startTime, endTime, caloriesPerDay);
//        return getFilteredWithExceededUsingCycles(mealList, startTime, endTime, caloriesPerDay);
    }


    @SuppressWarnings({"unused"})
    private static List<UserMealWithExceed> getFilteredWithExceededUsingStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> datesWithCalories = mealList.stream()
                .collect(groupingBy(meal -> meal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories()
                        , datesWithCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }


    @SuppressWarnings({"unused"})
    private static List<UserMealWithExceed> getFilteredWithExceededUsingCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> datesWithCalories = new HashMap<>();
        for (UserMeal meal : mealList) {
            datesWithCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
        }

        List<UserMealWithExceed> filteredWithExceeded = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredWithExceeded.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories()
                        , datesWithCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return filteredWithExceeded;
    }
}
