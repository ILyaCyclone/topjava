package ru.javawebinar.topjava.web.meal;

import org.junit.Rule;
import org.junit.Test;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.*;

public class MealRestControllerDeleteTest {

    @Rule
    public MealRestControllerInitializeRule resource = new MealRestControllerInitializeRule();


    @Test
    public void contextLoads() {
        assertNotNull(resource.controller());
    }

    @Test
    public void deleteOwn() {
        int id = 10;
        List<MealWithExceed> initialList = resource.controller().getWithExceed();

        System.out.println("~~~ initialList ~~~");
        initialList.forEach(System.out::println);
        System.out.println("~~~ end of initialList ~~~");

        assertTrue(initialList.stream()
                .anyMatch(meal -> meal.getId() == id));

        resource.controller().delete(10);

        List<MealWithExceed> afterList = resource.controller().getWithExceed();

        assertFalse(afterList.stream()
                .anyMatch(meal -> meal.getId() == id));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistent() {
        resource.controller().delete(99);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeign() {
        resource.controller().delete(1);
    }
}