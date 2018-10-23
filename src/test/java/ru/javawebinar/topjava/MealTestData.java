package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    // assume we know that sequence was incremented 2 times
    private static int SEQ_INC = 2;

    public static final List<Meal> USER_MEALS;
    public static final List<Meal> ADMIN_MEALS;

    static {
        USER_MEALS = Arrays.asList(
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 10, 00), "Завтрак",650),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 13, 00), "Обед",   900),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 22, 00), "Ужин",   500),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 7,  00), "Завтрак",256),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 13, 00), "Обед",   1000),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 22, 00), "Ужин",   500)
        );
        Collections.reverse(USER_MEALS);

        ADMIN_MEALS = Arrays.asList(
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 9,  00), "Завтрак",500),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 12, 00), "Обед",   950),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 18, 18, 00), "Ужин",   650),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 8,  00), "Завтрак",400),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 12, 00), "Обед",   800),
                new Meal(START_SEQ + SEQ_INC++, LocalDateTime.of(2018, 10, 19, 18, 00), "Ужин",   300)
        );
        Collections.reverse(ADMIN_MEALS);
    }


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertContainsInAnyOrder(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expected);
    }

}
