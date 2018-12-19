package ru.javawebinar.topjava.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

public class MealValidator implements Validator {
    private final MealService mealService;

    public MealValidator(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Meal.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Meal meal = (Meal)o;

        boolean dateIsFree = mealService.getBetweenDateTimes(meal.getDateTime(), meal.getDateTime(), SecurityUtil.authUserId()).isEmpty();

        if(!dateIsFree) {
            errors.rejectValue("dateTime", "error.dateAlreadyExists");
        }
    }
}
