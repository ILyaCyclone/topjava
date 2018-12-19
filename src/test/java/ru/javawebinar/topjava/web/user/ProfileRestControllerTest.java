package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJsonResultActions;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(get(REST_URL)
                        .with(userHttpBasic(USER)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(getUserMatcher(USER))
        );
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void testRegister() throws Exception {
        UserTo createdTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        ResultActions action = mockMvc.perform(post(REST_URL + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo)))
                .andDo(print())
                .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);

        User created = UserUtil.createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(userService.getByEmail("newemail@ya.ru"), created);
    }

    @Test
    void testUpdate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(userService.getByEmail("newemail@ya.ru"), UserUtil.updateFromTo(new User(USER), updatedTo));
    }


    @Test
    void testRegisterCaloriesValidationError() throws Exception {
        UserTo createdTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 5);
        registerValidationError(createdTo, "caloriesPerDay");
    }

    @Test
    void testRegisterPasswordValidationError() throws Exception {
        UserTo createdTo = new UserTo(null, "newName", "newemail@ya.ru", "1234", 1500);
        updateValidationError(createdTo, "password");
    }

    @Test
    void testRegisterNameValidationError() throws Exception {
        UserTo createdTo = new UserTo(null, "o", "newemail@ya.ru", "newPassword", 1500);
        updateValidationError(createdTo, "name");
    }

    @Test
    void testUpdateCaloriesValidationError() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 5);
        updateValidationError(updatedTo, "caloriesPerDay");
    }

    @Test
    void testUpdatePasswordValidationError() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "1234", 1500);
        updateValidationError(updatedTo, "password");
    }

    @Test
    void testUpdateNameValidationError() throws Exception {
        UserTo updatedTo = new UserTo(null, "o", "newemail@ya.ru", "newPassword", 1500);
        updateValidationError(updatedTo, "name");
    }

    void updateValidationError(UserTo userTo, String errorField) throws Exception {
        mockMvc.perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(allOf(containsString(errorField), containsString("VALIDATION_ERROR"))));
    }

    void registerValidationError(UserTo userTo, String errorField) throws Exception {
        mockMvc.perform(post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(allOf(containsString(errorField), containsString("VALIDATION_ERROR"))));
    }
}