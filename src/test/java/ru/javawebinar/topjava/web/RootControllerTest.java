package ru.javawebinar.topjava.web;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
//                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals",
                        contains(mealMatcher(MEAL6)
                                , mealMatcher(MEAL5)
                                , mealMatcher(MEAL4)
                                , mealMatcher(MEAL3)
                                , mealMatcher(MEAL2)
                                , mealMatcher(MEAL1)
                        )
                        )
                );
    }

    private Matcher<Iterable<? super Object>> mealMatcher(Meal meal) {
        return
                allOf(
                        hasProperty("id", is(meal.getId())),
                        hasProperty("calories", is(meal.getCalories())),
                        hasProperty("description", is(meal.getDescription())),
                        hasProperty("dateTime", is(meal.getDateTime()))
                )
                ;
    }
}