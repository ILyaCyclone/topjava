package ru.javawebinar.topjava.web.meal;

import org.junit.ClassRule;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class MealRestControllerGetTest {

    @ClassRule
    public static MealRestControllerInitializeRule resource = new MealRestControllerInitializeRule();

    @Test
    public void contextLoads() {
        assertNotNull(resource.controller());
    }

    @Test
    public void getAll() {
        List<MealWithExceed> mealsWithExceed = resource.controller().getWithExceed();

        assertFalse("meal list is empty", mealsWithExceed.isEmpty());

        assertTrue("acquired alien meals",
        mealsWithExceed.stream()
                .noneMatch(meal -> meal.getUserId() != resource.authUserId())
        );
    }

    @Test
    public void getOne() {
        Integer mealId = 7;
        Meal meal = resource.controller().get(mealId);

        assertEquals("ID does not match", meal.getId(), mealId);

        assertEquals("meal does not belong to user", meal.getUserId(), resource.authUserId());
    }

    @Test(expected = NotFoundException.class)
    public void getOneWrongId() {
        resource.controller().get(99);
    }

    @Test(expected = NotFoundException.class)
    public void getOneForeign() {
        resource.controller().get(1);
    }



    @Test
    public void getWithExceed() {
        // assume that we know initial values
        List<MealWithExceed> mealsWithExceed = resource.controller().getWithExceed();
        assertFalse("meal with exceeded is empty", mealsWithExceed.isEmpty());

        mealsWithExceed.forEach(mealWithExceed -> {
            // assume that we know initial values
            switch (mealWithExceed.getId()) {
                case 7: case 8: case 9:
                    assertFalse(mealWithExceed.isExceed());
                    break;
                case 10: case 11: case 12:
                    assertTrue(mealWithExceed.isExceed());
                    break;
                }
        });
    }

    @Test
    public void getFilteredByTimeWithExceed() {
        // assume that we know initial values
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(15, 0);

        List<MealWithExceed> mealsWithExceed = resource.controller().getWithExceed();
        assertFalse("meal with exceeded is empty", mealsWithExceed.isEmpty());

        // check that unfiltered list has values outside filter range
        assertTrue(mealsWithExceed.stream().anyMatch(m -> !DateTimeUtil.isBetween(m.getTime(), startTime, endTime)));

        List<MealWithExceed> filteredWithExceed = resource.controller().getFilteredWithExceed(startTime, endTime);
        assertFalse("meals outside filter range", filteredWithExceed.stream().anyMatch(m -> !DateTimeUtil.isBetween(m.getTime(), startTime, endTime)));
    }

    @Test
    public void getFilteredByDateWithExceed() {
        // assume that we know initial values
        LocalDate startDate = LocalDate.of(2011, 5, 29);
        LocalDate endDate = LocalDate.of(2011, 5, 30);

        List<MealWithExceed> mealsWithExceed = resource.controller().getWithExceed();
        assertFalse("meal with exceeded is empty", mealsWithExceed.isEmpty());

        // check that unfiltered list has values outside filter range
        assertTrue(mealsWithExceed.stream().anyMatch(m -> !DateTimeUtil.isBetween(m.getDate(), startDate, endDate)));

        List<MealWithExceed> filteredWithExceed = resource.controller().getFilteredWithExceed(startDate, endDate);
        assertFalse("meals outside filter range", filteredWithExceed.stream().anyMatch(m -> !DateTimeUtil.isBetween(m.getDate(), startDate, endDate)));
    }

    @Test
    public void getFilteredByDateAndTimeWithExceed() {
        // assume that we know initial values
        LocalDate startDate = LocalDate.of(2011, 5, 31);
        LocalDate endDate = LocalDate.of(2011, 6, 1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(15, 0);

        List<MealWithExceed> mealsWithExceed = resource.controller().getWithExceed();
        assertFalse("meal with exceeded is empty", mealsWithExceed.isEmpty());
        mealsWithExceed.forEach(System.out::println);

        // check that unfiltered list has values outside filter range
        assertTrue(mealsWithExceed.stream().anyMatch(
                m -> (!DateTimeUtil.isBetween(m.getDate(), startDate, endDate) && (!DateTimeUtil.isBetween(m.getTime(), startTime, endTime)))));


        List<MealWithExceed> filteredWithExceed = resource.controller().getFilteredWithExceed(startDate, endDate, startTime, endTime);

        assertFalse("meals outside filter range", filteredWithExceed.stream().anyMatch(
                m -> (!DateTimeUtil.isBetween(m.getDate(), startDate, endDate) && (!DateTimeUtil.isBetween(m.getTime(), startTime, endTime)))));
        filteredWithExceed.forEach(System.out::println);
    }
}