package ru.javawebinar.topjava.service.datajpa;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;



@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeal() {
        User user = service.getWithMeal(UserTestData.USER_ID);
        MealTestData.assertMatch(user.getMeals(), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealNotFound() throws Exception {
        service.getWithMeal(1);
    }

    @Test
    public void getWithMealEmpty() throws Exception {
        User hungryUser = new User(null, "Hungry", "hunger@gmail.com", "password", Role.ROLE_USER);
        User storedUser = service.create(hungryUser);
        User storedUserWithMeal = service.getWithMeal(storedUser.getId());
        Assertions.assertThat(storedUserWithMeal.getMeals().isEmpty()).withFailMessage("Hungry user meal should be empty");
    }

}