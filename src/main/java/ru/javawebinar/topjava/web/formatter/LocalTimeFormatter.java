package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String source, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(source);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        if (localTime == null) {
            return "";
        }
        return localTime.format(DateTimeFormatter.ISO_TIME);
    }
}
