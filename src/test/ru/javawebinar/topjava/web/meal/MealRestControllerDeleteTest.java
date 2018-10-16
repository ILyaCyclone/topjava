package ru.javawebinar.topjava.web.meal;

import org.junit.Rule;
import org.junit.Test;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.*;

public class MealRestControllerDeleteTest {

    @Rule
    public MealRestControllerInitializeRule testRule = new MealRestControllerInitializeRule();


    @Test
    public void contextLoads() {
        assertNotNull(testRule.controller());
    }

    @Test
    public void deleteOwn() {
        int id = 10;
        List<MealWithExceed> initialList = testRule.controller().getWithExceed();

        System.out.println("~~~ initialList ~~~");
        initialList.forEach(System.out::println);
        System.out.println("~~~ end of initialList ~~~");

        assertTrue(initialList.stream()
                .anyMatch(meal -> meal.getId() == id));

        testRule.controller().delete(10);

        List<MealWithExceed> afterList = testRule.controller().getWithExceed();

        assertFalse(afterList.stream()
                .anyMatch(meal -> meal.getId() == id));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistent() {
        testRule.controller().delete(99);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeign() {
        testRule.controller().delete(1);
    }
}