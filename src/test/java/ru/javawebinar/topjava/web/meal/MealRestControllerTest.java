package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';


    @Autowired
    protected MealService mealService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExcess(MEALS, UserTestData.USER.getCaloriesPerDay())));
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(UserTestData.USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(UserTestData.USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, UserTestData.USER_ID), updated);
    }

    @Test
    void testGetBetween() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2015, 5, 31, 0, 0);
        LocalDateTime startTime = LocalDateTime.of(2015, 5, 31, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 5, 31, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2015, 5, 31, 13, 0);

        //not sure if we should rely on mealService here
//        List<Meal> dateFilteredMeals = MEALS.stream().filter(m -> Util.isBetween(m.getDate(), startDate.toLocalDate(), endDate.toLocalDate())).collect(Collectors.toList());
        List<Meal> dateFilteredMeals = mealService.getBetweenDates(startDate.toLocalDate(), endDate.toLocalDate(), UserTestData.USER_ID);
        List<MealTo> mealToExpected = MealsUtil.getFilteredWithExcess(dateFilteredMeals, UserTestData.USER.getCaloriesPerDay(), startTime.toLocalTime(), endTime.toLocalTime());

        mockMvc.perform(get(REST_URL + "filter"
//                // use this block if using @DateTimeFormat in MealRestController.getBetween
//                + "?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE_TIME)
//                + "&startTime=" + startTime.format(DateTimeFormatter.ISO_DATE_TIME)
//                + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE_TIME)
//                + "&endTime=" + endTime.format(DateTimeFormatter.ISO_DATE_TIME)
                        // use this block if using custom formatters
                        + "?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE)
                        + "&startTime=" + startTime.format(DateTimeFormatter.ISO_TIME)
                        + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE)
                        + "&endTime=" + endTime.format(DateTimeFormatter.ISO_TIME)

        )).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(mealToExpected));
    }
}