package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.util.Throwables.getRootCause;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static Map<String, Long> testFinishedNanos = new HashMap<>();
    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            testFinishedNanos.put(description.getMethodName(), nanos);

            System.out.println(String.format("Test \"%s\" finished in %s seconds",
                    description.getMethodName(), formatNanosAsSeconds(nanos, 2)));
        }
    };

    @ClassRule
    public static Stopwatch classStopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String line = "\n--------------------------------";
            StringBuilder sb = new StringBuilder(line);
            sb.append("\nTest | Time, s");

            description.getChildren().forEach(d -> {
                sb.append(String.format("\n\"%s\" | %s", d.getMethodName(), formatNanosAsSeconds(testFinishedNanos.get(d.getMethodName()), 2)));
            });
            sb.append(line);
            System.out.println(sb.toString());
        }
    };

    private static String formatNanosAsSeconds(long nanos, int floatDigits) {
        // can't format TimeUnit.NANOSECONDS.toMillis(nanos) with floating point
        String format = "%." + floatDigits + "f";
        return String.format(format, (nanos / 1_000_000_000f));
    }




    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateIllegalCalories() {
        expectConstrainViolationException(getUpdatedIllegalCalories(), "calories");
    }

    @Test
    public void updateIllegalDescription() {
        expectConstrainViolationException(getUpdatedIllegalDescription(), "description");
    }

    private void expectConstrainViolationException(Meal updatedMeal, String exceptionMessageContains) {
        // see https://github.com/junit-team/junit4/pull/778
        try {
            service.update(updatedMeal, USER_ID);
            Assert.fail("Expected " + ConstraintViolationException.class.getName());
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            Assert.assertThat(rootCause, instanceOf(ConstraintViolationException.class));
            Assert.assertThat(rootCause.getMessage(), containsString(exceptionMessageContains));
        }
    }

    @Test
    public void updateNotFound() {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}