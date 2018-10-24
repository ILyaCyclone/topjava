package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NON_EXISTENT_USER_ID;
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
        Meal actual = service.get(USER_MEAL3.getId(), USER_ID);
        assertMatch(actual, USER_MEAL3);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundUser() {
        service.get(USER_MEAL3.getId(), NON_EXISTENT_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundMeal() {
        service.get(NON_EXISTENT_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL3.getId(), USER_ID);
        List<Meal> actual = service.getAll(USER_ID);

        assertMatch(actual, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL2, USER_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundUser() throws Exception {
        service.delete(USER_MEAL3.getId(), NON_EXISTENT_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundMeal() throws Exception {
        service.delete(NON_EXISTENT_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlien() throws Exception {
        service.delete(ADMIN_MEAL1.getId(), USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2018, 10, 19);
        LocalDate endDate = LocalDate.MAX;

        List<Meal> actual = service.getBetweenDates(startDate, endDate, USER_ID);

        assertMatch(actual, USER_MEAL6, USER_MEAL5, USER_MEAL4);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime startDateTime = LocalDateTime.of(2018, 10, 19, 13, 0);
        LocalDateTime endDateTime = LocalDateTime.MAX;

        List<Meal> actual = service.getBetweenDateTimes(startDateTime, endDateTime, USER_ID);

        assertMatch(actual, USER_MEAL6, USER_MEAL5);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);

        assertMatch(actual, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);
    }

    @Test
    public void update() {
        Meal expectedMeal = new Meal(USER_MEAL3.getId(), LocalDateTime.of(2018, 10, 18, 22, 00), "Ужин update", 500);
        service.update(expectedMeal, USER_ID);

        Meal actualMeal = service.get(USER_MEAL3.getId(), USER_ID);

        assertMatch(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlien() {
        Meal meal = new Meal(ADMIN_MEAL1.getId(), LocalDateTime.of(2018, 10, 19, 13, 01), "Обед update", 1001);
        service.update(meal, USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2018, 10, 20, 13, 00), "Обед new", 1000);
        assertThat(service.create(newMeal, USER_ID)).isNotNull();

        List<Meal> actual = service.getAll(USER_ID);

        assertMatch(actual, newMeal, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);

    }
}