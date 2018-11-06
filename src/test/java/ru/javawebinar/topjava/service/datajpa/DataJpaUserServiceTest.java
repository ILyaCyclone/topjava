package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;



@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeal() {
        User user = service.getWithMeal(UserTestData.USER_ID);
        MealTestData.assertMatch(user.getMeals(), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

}