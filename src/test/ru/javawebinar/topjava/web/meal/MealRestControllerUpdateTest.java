package ru.javawebinar.topjava.web.meal;

import org.junit.Rule;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MealRestControllerUpdateTest {

    @Rule
    public MealRestControllerInitializeRule testRule = new MealRestControllerInitializeRule();


    @Test
    public void contextLoads() {
        assertNotNull(testRule.controller());
    }

    @Test
    public void updateOwn() {
        // arrange
        Integer id = 10;
        String updatedDescription = "just edited";
        Integer updatedCalories = 350;
        LocalDateTime updatedDateTime = LocalDateTime.of(2018, 10, 15, 10, 30);

        assertNotNull("can't get meal ID " + id, testRule.controller().get(id));

        Meal newMeal = new Meal(id, testRule.authUserId(), updatedDateTime, updatedDescription, updatedCalories);

        // act
        testRule.controller().update(newMeal);

        // assert
        Meal updatedMeal = testRule.controller().get(id);

        assertEquals(id, updatedMeal.getId());
        assertEquals(testRule.authUserId(), updatedMeal.getUserId());
        assertEquals((Object) updatedCalories, updatedMeal.getCalories());
        assertEquals(updatedDescription, updatedMeal.getDescription());
        assertEquals(updatedDateTime, updatedMeal.getDateTime());
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistent() {
        testRule.controller().update(
                new Meal(999, testRule.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        testRule.controller().update(
                new Meal(1, testRule.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateNew() {
        testRule.controller().update(
                new Meal(testRule.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }
}