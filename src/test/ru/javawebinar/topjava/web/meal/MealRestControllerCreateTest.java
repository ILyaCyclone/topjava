package ru.javawebinar.topjava.web.meal;

import org.junit.ClassRule;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class MealRestControllerCreateTest {

    @ClassRule
    public static MealRestControllerInitializeRule testRule = new MealRestControllerInitializeRule();

    @Test
    public void contextLoads() {
        assertNotNull(testRule.controller());
    }

    @Test
    public void createOwn() {
        String updatedDescription = "just created";
        Integer updatedCalories = 350;
        LocalDateTime updatedDateTime = LocalDateTime.of(2018, 10, 15, 10, 30);

        Meal newMeal = new Meal(testRule.authUserId(), updatedDateTime, updatedDescription, updatedCalories);

        Meal createdMeal = testRule.controller().create(newMeal);
        Integer createdId = createdMeal.getId();

        assertEquals(createdId, createdMeal.getId());
        assertEquals(testRule.authUserId(), createdMeal.getUserId());
        assertEquals((Object) updatedCalories, createdMeal.getCalories());
        assertEquals(updatedDescription, createdMeal.getDescription());
        assertEquals(updatedDateTime, createdMeal.getDateTime());


        Meal storedMeal = testRule.controller().get(createdId);

        assertEquals(storedMeal.getId(), createdMeal.getId());
        assertEquals(storedMeal.getUserId(), createdMeal.getUserId());
        assertEquals((Object) storedMeal.getCalories(), createdMeal.getCalories());
        assertEquals(storedMeal.getDescription(), createdMeal.getDescription());
        assertEquals(storedMeal.getDateTime(), createdMeal.getDateTime());
    }

    @Test
    public void createForeign() {
        Integer foreignUserId = 0;
        Meal createdMeal = testRule.controller().create(new Meal(foreignUserId, LocalDateTime.now(), "desc", 100));
        assertNotEquals(foreignUserId, createdMeal.getUserId());
        assertEquals(testRule.authUserId(), createdMeal.getUserId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNotNew() {
        testRule.controller().create(new Meal(10, testRule.authUserId(), LocalDateTime.now(), "desc", 100));
    }

}