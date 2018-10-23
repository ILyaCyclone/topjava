package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(100003, USER_ID);
        Meal expected = USER_MEALS.get(4);
        assertMatch(actual, expected);
    }

    @Test
    public void delete() {
        final int userId = USER_ID;
        service.delete(100003, userId);

        List<Meal> actual = service.getAll(userId);
        ArrayList<Meal> expected = new ArrayList<>(MealTestData.USER_MEALS);
        expected.remove(4);
        assertMatch(actual, expected);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundUser() throws Exception {
        service.delete(100003, 999);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundMeal() throws Exception {
        service.delete(999, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlien() throws Exception {
        service.delete(100010, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        int userId = USER_ID;
        LocalDate startDate = LocalDate.of(2018, 10, 19);
        LocalDate endDate = LocalDate.MAX;

        List<Meal> actual = service.getBetweenDates(startDate, endDate, userId);
        List<Meal> expected = USER_MEALS.stream()
                .filter(meal -> Util.isBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());

        assertMatch(actual, expected);
    }

    @Test
    public void getBetweenDateTimes() {
        int userId = USER_ID;
        LocalDateTime startDateTime = LocalDateTime.of(2018, 10, 19, 15, 0);
        LocalDateTime endDateTime = LocalDateTime.MAX;

        List<Meal> actual = service.getBetweenDateTimes(startDateTime, endDateTime, userId);
        List<Meal> expected = USER_MEALS.stream()
                .filter(meal -> Util.isBetween(meal.getDateTime(), startDateTime, endDateTime))
                .collect(Collectors.toList());

        assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        int userId = USER_ID;
        List<Meal> actual = service.getAll(userId);
        List<Meal> expected = new ArrayList<>(USER_MEALS);

        assertMatch(actual, expected);
    }

    @Test
    public void update() {
        int userId = USER_ID;
        int mealId = 100006;
        Meal expectedMeal = new Meal(mealId, LocalDateTime.of(2018, 10, 19, 13, 01), "Обед update", 1001);
        service.update(expectedMeal, userId);

        Meal actualMeal = service.get(mealId, userId);

        assertMatch(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlien() {
        int userId = USER_ID;
        Meal meal = new Meal(100010, LocalDateTime.of(2018, 10, 19, 13, 01), "Обед update", 1001);
        service.update(meal, userId);
    }

    @Test
    public void create() {
        int userId = USER_ID;
        Meal meal = new Meal(LocalDateTime.of(2018, 10, 19, 13, 01), "Обед update", 1001);
        service.create(meal, userId);

        List<Meal> actual = service.getAll(userId);

        List<Meal> expected = new ArrayList<>(USER_MEALS);
        expected.add(meal);

        assertContainsInAnyOrder(actual, expected);
    }
}