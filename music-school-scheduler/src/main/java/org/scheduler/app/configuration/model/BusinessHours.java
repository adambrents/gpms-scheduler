package org.scheduler.app.configuration.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessHours {
    public String open;
    public String close;
    public List<String> days;

    public LocalTime getOpen() {
        return LocalTime.parse(open);
    }

    public LocalTime getClose() {
        return LocalTime.parse(close);
    }

    public List<DayOfWeek> getDays() {
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        for (String day : days) {
            daysOfWeek.add(DayOfWeek.valueOf(day.toUpperCase()));
        }
        return daysOfWeek;
    }
}
