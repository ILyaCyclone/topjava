package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkExistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }


    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(SecurityUtil.authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(meal);
    }

    public void update(Meal meal) {
        log.info("update {} ", meal);
        checkExistent(meal);
        meal.setUserId(SecurityUtil.authUserId());
        service.update(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(SecurityUtil.authUserId(), id);
    }

    public List<MealWithExceed> getWithExceed() {
        log.info("getWithExceed");
        return service.getWithExceed(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getFilteredWithExceed(LocalDate startDate, LocalDate endDate) {
        return getFilteredWithExceed(startDate, endDate, null, null);
    }
    public List<MealWithExceed> getFilteredWithExceed(LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExceed(null, null, startTime, endTime);
    }
    public List<MealWithExceed> getFilteredWithExceed(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getFilteredWithExceed ");
        return service.getFilteredWithExceed(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()
                , startDate != null ? startDate : LocalDate.MIN
                , endDate != null ? endDate : LocalDate.MAX
                , startTime != null ? startTime : LocalTime.MIN
                , endTime != null ? endTime : LocalTime.MAX);
    }

}