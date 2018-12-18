package ru.javawebinar.topjava.util.exception;

import java.util.ArrayList;
import java.util.List;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final List<String> details = new ArrayList<>();

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.details.add(detail);
    }

    public ErrorInfo(CharSequence url, ErrorType type, List<String> details) {
        this.url = url.toString();
        this.type = type;
        this.details.addAll(details);
    }
}