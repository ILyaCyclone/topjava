package ru.javawebinar.topjava.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@RunWith(BlockJUnit4ClassRunner.class)
public class MealRepositoryTest {

    private MealRepository mealRepository;

    @Before
    public void setUp() {
        mealRepository = RepositoryFactory.createMealRepository();
    }

    @Test
    public void mealCrud() {
        Assert.assertNotNull(mealRepository);
        mealRepository.findAll().forEach(System.out::println);

        Meal meal4 = mealRepository.findById(4).get();
        Assert.assertNotNull(meal4);
        System.out.println("meal4 = " + meal4);

        mealRepository.save(new Meal(4, LocalDateTime.of(2018, Month.MAY, 30, 10, 0), "Завтрак", 1500));

        Meal meal4updated = mealRepository.findById(4).get();
        Assert.assertEquals(LocalDate.of(2018, Month.MAY, 30), meal4updated.getDate());
        System.out.println("meal4updated = " + meal4updated);

        Iterable<Meal> meals345 = mealRepository.findAllById(Arrays.asList(3, 4, 5));
        Assert.assertEquals(3, StreamSupport.stream(meals345.spliterator(), false).count());
        System.out.println("meals345 = " + meals345);



    }

}
