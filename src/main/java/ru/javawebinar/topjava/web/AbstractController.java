package ru.javawebinar.topjava.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class AbstractController {
    public ResponseEntity<String> respondBindingError(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = null;
                    boolean bindingFailure = fe.isBindingFailure(); // either binding or validation failure
                    if (bindingFailure && Objects.equals(fe.getRejectedValue(), "")) {
                        msg = "must not by empty";
                    } else {
                        if (fe.getDefaultMessage() == null) {
                            msg = "was not bound correctly";
                        } else {
                            msg = fe.getDefaultMessage();
                        }
                    }

                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    joiner.add(msg);
                });
        return new ResponseEntity<>(joiner.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
