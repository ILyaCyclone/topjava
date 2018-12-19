package ru.javawebinar.topjava.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class RegistrationUserToValidator implements Validator {
    private final UserService userService;

    public RegistrationUserToValidator(UserService userService) {

        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UserTo.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserTo userTo = (UserTo)o;

        try {
            userService.getByEmail(userTo.getEmail());
            // email already exists
            errors.rejectValue("email", "error.emailAlreadyExists");
        } catch (NotFoundException e) {
            // email is free
        }
    }
}
