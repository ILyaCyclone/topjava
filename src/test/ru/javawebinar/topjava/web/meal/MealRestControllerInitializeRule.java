package ru.javawebinar.topjava.web.meal;

import org.junit.rules.ExternalResource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.SecurityUtil;

public class MealRestControllerInitializeRule extends ExternalResource {

    private ConfigurableApplicationContext appCtx;
    private MealRestController controller;
    private Integer authUserId;

    ConfigurableApplicationContext appCtx() {
        return appCtx;
    }

    MealRestController controller() {
        return controller;
    }

    Integer authUserId() {
        return authUserId;
    }

    @Override
    protected void before() throws Throwable {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
        authUserId = SecurityUtil.authUserId();
    }

    @Override
    protected void after() {
        appCtx.close();
    }
}
