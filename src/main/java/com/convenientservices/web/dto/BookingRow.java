package com.convenientservices.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Data
public class BookingRow {
    private Long id;
    private String color;
    private Integer startTime;
    private Integer endTime;
    private Integer length;
    private String text;
    private String span;
    private String resultClassText;
    private LocalDate date;
    private Boolean isFree;

    public BookingRow(Long id, Integer startTime, Integer endTime, Integer length, String span, LocalDate date, Boolean isFree) {
        this.id = id;
        this.isFree = isFree;
        this.color = getColor(Date.from(date.atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant()));
        this.startTime = startTime;
        this.endTime = endTime;
        this.length = length;
        this.span = span;
        this.date = date;
        String startTimeTmp = String.valueOf(startTime);
        this.text = isFree ? "Свободно:" + (startTimeTmp.length() == 3 ? startTimeTmp.substring(0, 1).concat(":").concat(startTimeTmp.substring(1))
                : startTimeTmp.substring(0, 2).concat(":").concat(startTimeTmp.substring(2))) : "Забронировано";
        this.resultClassText = "event " + color + " " + "start-" + startTime + " end-" + endTime + " length-" + length;
    }

    private String getColor(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (isFree && (dayOfWeek == 7 || dayOfWeek == 1)) {
            return "stage-jupiter";
        }

        if (isFree) {
            return "stage-earth";
        }
        return "stage-venus";
    }
}
