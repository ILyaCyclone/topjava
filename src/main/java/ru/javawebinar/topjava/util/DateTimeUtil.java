package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenTime(LocalTime time, LocalTime startTime, LocalTime endTime) {
//        return time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0;
        return isBetween(time, startTime, endTime);
    }

    public static boolean isBetweenDate(LocalDate date, LocalDate startDate, LocalDate endDate) {
//        return !(date.isBefore(startDate) || date.isAfter(endDate));
        return isBetween(date, startDate, endDate);
    }

    // <T extends Comparable> might be too wide generic, allowing to compare heterogeneous Comparables
    public static <T extends Comparable> boolean isBetween(T object, T startRange, T endRange) {
        return object.compareTo(startRange) >= 0 && object.compareTo(endRange) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
