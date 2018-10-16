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
    public MealRestControllerInitializeRule resource = new MealRestControllerInitializeRule();


    @Test
    public void contextLoads() {
        assertNotNull(resource.controller());
    }

    @Test
    public void updateOwn() {
        // arrange
        Integer id = 10;
        String updatedDescription = "just edited";
        Integer updatedCalories = 350;
        LocalDateTime updatedDateTime = LocalDateTime.of(2018, 10, 15, 10, 30);

        assertNotNull("can't get meal ID " + id, resource.controller().get(id));

        Meal newMeal = new Meal(id, resource.authUserId(), updatedDateTime, updatedDescription, updatedCalories);

        // act
        resource.controller().update(newMeal);

        // assert
        Meal updatedMeal = resource.controller().get(id);

        assertEquals(id, updatedMeal.getId());
        assertEquals(resource.authUserId(), updatedMeal.getUserId());
        assertEquals((Object) updatedCalories, updatedMeal.getCalories());
        assertEquals(updatedDescription, updatedMeal.getDescription());
        assertEquals(updatedDateTime, updatedMeal.getDateTime());
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistent() {
        resource.controller().update(
                new Meal(999, resource.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        resource.controller().update(
                new Meal(1, resource.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateNew() {
        resource.controller().update(
                new Meal(resource.authUserId(), LocalDateTime.now(), "desc", 100)
        );
    }
}