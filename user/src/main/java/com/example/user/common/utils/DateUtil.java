package com.example.user.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM" +
            "-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    // Convert LocalDate to String
    public static String formatLocalDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    // Convert LocalDateTime to String
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    // Convert String to LocalDate
    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    // Convert String to LocalDateTime
    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    // Convert Date to LocalDateTime
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Converts LocalDate to Date.
     *
     * @param localDate the LocalDate to convert
     * @return the Date representation of the given LocalDate
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts LocalDateTime to Date.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return the Date representation of the given LocalDateTime
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date plusOrMinus(Date date, long amountToAdd, ChronoUnit unit) {
        Instant instant = date.toInstant();
        instant = instant.plus(amountToAdd, unit);
        return Date.from(instant);
    }
}
