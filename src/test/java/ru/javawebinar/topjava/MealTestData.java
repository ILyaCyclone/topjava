package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    // assume we know that sequence was incremented 2 times
    private static int SEQ_INC = 2;

    public static final Meal
            USER_MEAL1 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 10, 00), "Завтрак", 650),
            USER_MEAL2 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 13, 00), "Обед", 900),
            USER_MEAL3 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 22, 00), "Ужин", 500),
            USER_MEAL4 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 7, 00), "Завтрак", 256),
            USER_MEAL5 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 13, 00), "Обед", 1000),
            USER_MEAL6 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 22, 00), "Ужин", 500),

            ADMIN_MEAL1 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 9, 00), "Завтрак", 500),
            ADMIN_MEAL2 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 12, 00), "Обед", 950),
            ADMIN_MEAL3 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 18, 00), "Ужин", 650),
            ADMIN_MEAL4 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 8, 00), "Завтрак", 400),
            ADMIN_MEAL5 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 12, 00), "Обед", 800),
            ADMIN_MEAL6 = new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 18, 00), "Ужин", 300);

    public static final List<Meal> USER_MEALS = Arrays.asList(USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);
    public static final List<Meal> ADMIN_MEALS = Arrays.asList(ADMIN_MEAL6, ADMIN_MEAL5, ADMIN_MEAL4, ADMIN_MEAL3, ADMIN_MEAL2, ADMIN_MEAL1);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
