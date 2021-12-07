package com.convenientservices.web.utilities;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class Utils {

    public static Long changeMinuteToSeconds(Long min) {
        if (min < 0L || min > 1440L) {
            return 0L;
        }
        return min * 60;
    }

    public static String getStringTime(Long seconds) {
        if (seconds < 0L || seconds > 86400L) {
            return "Указано некорректное время";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        return LocalTime.ofSecondOfDay(seconds).format(formatter);
    }

    public static LocalDate getLocalDateFromString(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(time, formatter);
        return date;
    }

    public static boolean passwordMatching(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    public static String getRandomActivationCode() {
        return UUID.randomUUID().toString();
    }
}
