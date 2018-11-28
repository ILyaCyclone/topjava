package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String source, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalDate(source);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return "";
        }
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }
}
