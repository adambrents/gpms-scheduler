package org.scheduler.utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;

public class TimeHelper {
    private static final ObservableList<LocalDateTime> availableTimes = FXCollections.observableArrayList();

    /**
     * accepts a localdate and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param localDate
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableStartTimes(LocalDate localDate){
        availableTimes.clear();

        LocalDateTime localDateTimeStart = LocalDateTime.of(localDate,LocalTime.of(0, 0));
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(localDateTimeStart, ZoneId.systemDefault());

        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateTimeStart.toLocalDate(),LocalTime.of(23,59));
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(localDateTimeEnd, ZoneId.systemDefault());

        while (startZonedDateTime.isBefore(endZonedDateTime)){
            if(validStartBusinessHours(startZonedDateTime.toLocalDateTime())){
                availableTimes.add(startZonedDateTime.toLocalDateTime());
            }
            startZonedDateTime = startZonedDateTime.plusMinutes(30);
        }
        return availableTimes;
    }

    /**
     * accepts a localDateTime and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param endDateTime
     * @param startTime
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableEndTimes(LocalDateTime endDateTime, LocalTime startTime){
        availableTimes.clear();


        LocalDateTime localDateTimeStart = LocalDateTime.of(endDateTime.toLocalDate(),startTime.plusMinutes(30));
        ZonedDateTime startingIncrementZonedDateTime = ZonedDateTime.of(localDateTimeStart, ZoneId.systemDefault());

        LocalDateTime localDateTimeEnd = LocalDateTime.of(endDateTime.toLocalDate(),LocalTime.of(22,1));
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(localDateTimeEnd, ZoneId.of("America/New_York"));

        while (startingIncrementZonedDateTime.isBefore(endZonedDateTime)){
            if(validEndBusinessHours(startingIncrementZonedDateTime, endZonedDateTime)){
                availableTimes.add(startingIncrementZonedDateTime.toLocalDateTime());
            }
            startingIncrementZonedDateTime = startingIncrementZonedDateTime.plusMinutes(30);
        }
        return availableTimes;
    }

    /**
     * accepts a start and end time and returns a bool on whether or not the end time is within business hours
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static boolean validEndBusinessHours(ZonedDateTime startDateTime, ZonedDateTime endDateTime){

        boolean isApptValid = true;

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        if(endDateTime.toLocalTime().isBefore(LocalTime.of(8,0))){
            isApptValid = false;
        }
        if(endDateTime.toLocalTime().isAfter(LocalTime.of(22, 1))){
            isApptValid = false;
        }
        if(endDateTime.isBefore(ZonedESTNOW)){
            isApptValid = false;
        }
        if(endDateTime.toLocalTime().equals(startDateTime.toLocalTime())){
            isApptValid = false;
        }

        return isApptValid;
    }

    /**
     * accepts a start and end time and returns a bool on whether or not the start time is within business hours
     *
     * @param startDateTime
     * @return
     */
    public static boolean validStartBusinessHours(LocalDateTime startDateTime){

        boolean isApptValid = true;

        ZonedDateTime zoneStartDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedStartDateTimeEST = zoneStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        if(zonedStartDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,0))){
            isApptValid = false;
        }
        if(zonedStartDateTimeEST.toLocalTime().isAfter(LocalTime.of(21, 30))){
            isApptValid = false;
        }
        if(zonedStartDateTimeEST.isBefore(ZonedESTNOW)){
            isApptValid = false;
        }

        return isApptValid;
    }
}
