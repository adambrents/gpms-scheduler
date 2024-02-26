package org.scheduler.controller.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.utilities.TimeHelper;
import org.scheduler.viewmodels.Lesson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class LessonControllerBase extends ControllerBase {

    private ObservableList<LocalDateTime> _validStartTimes = FXCollections.observableArrayList();
    private ObservableList<LocalDateTime> _validEndTimes = FXCollections.observableArrayList();
    private final LessonsDTO lessonsDTO = new LessonsDTO();

    /**
     * accepts dates and returns a list of valid start times
     * @param userPickedDate
     * @return
     */
    public ObservableList<LocalDateTime> getAllowedLessonStartTimes(LocalDate userPickedDate) {
        _validStartTimes.clear();
        ObservableList<LocalDateTime> possibleApptTimes = TimeHelper.getAvailableStartTimes(userPickedDate);
        _validStartTimes = possibleApptTimes;
        ObservableList<Lesson> allTakenStartTimes = lessonsDTO.getAllTakenLessonTimesByDate(userPickedDate);
        ObservableList<Lesson> allTakenEndTimes = lessonsDTO.getAllTakenLessonTimesByDate(userPickedDate);

        int allTakenStartTimesCount = allTakenStartTimes.size() - 1;
        int x = 0;
        while (x <= allTakenStartTimesCount){
            int y = _validStartTimes.size() - 1;
            LocalTime takenStartTime = allTakenStartTimes.get(x).getStartTime();
            LocalTime takenEndTime = allTakenEndTimes.get(x).getEndTime();
            while (y  >= 0){
                LocalTime possibleStartTime = possibleApptTimes.get(y).toLocalTime();
                LocalTime possibleEndTime = possibleStartTime.plusMinutes(30);
                if (possibleStartTime.isBefore(takenEndTime) || possibleStartTime.equals(takenEndTime)){
                    if ((possibleStartTime.isBefore(takenStartTime) || possibleStartTime.equals(takenStartTime)) && possibleEndTime.isAfter(takenStartTime)) {
                        _validStartTimes.remove(y);
                    }
                    else if ((possibleStartTime.isAfter(takenStartTime) || possibleStartTime.equals(takenStartTime)) && possibleStartTime.isBefore(takenEndTime)) {
                        _validStartTimes.remove(y);
                    }
                }
                --y;
            }
            ++x;
        }
        return _validStartTimes;
    }

    /**
     * accepts dates and times and returns a list of valid end times
     * @param userPickedDate
     * @param userPickedStartTime
     * @return
     */
    public ObservableList<LocalDateTime> getAllowedLessonEndTimes(LocalDate userPickedDate, LocalTime userPickedStartTime) {
        _validEndTimes.clear();
        LocalDateTime userPickedDateTime = LocalDateTime.of(userPickedDate, userPickedStartTime);
        LocalDateTime possibleEndDateTime = userPickedDateTime.plusMinutes(30);

        _validEndTimes = TimeHelper.getAvailableEndTimes(possibleEndDateTime, userPickedStartTime);
        ObservableList<Lesson> allTakenStartTimes = lessonsDTO.getAllTakenLessonTimesByDate(userPickedDateTime.toLocalDate());
        ObservableList<Lesson> allTakenEndTimes = lessonsDTO.getAllTakenLessonTimesByDate(userPickedDateTime.toLocalDate());
        int allTakenEndTimesCount = allTakenEndTimes.size() - 1;
        int x = 0;
        while (x <= allTakenEndTimesCount){
            int y = 0;
            int z = _validEndTimes.size() - 1;
            while (z  >= y){
                if (userPickedStartTime.isBefore(allTakenEndTimes.get(x).getEndTime()) || userPickedStartTime.equals(allTakenEndTimes.get(x).getEndTime())){
                    if (_validEndTimes.get(z).equals(allTakenEndTimes.get(x)) || _validEndTimes.get(z).toLocalTime().isAfter(allTakenEndTimes.get(x).getEndTime())
                            || _validEndTimes.get(z).toLocalTime().isAfter(allTakenStartTimes.get(x).getStartTime())){
                        _validEndTimes.remove(z);
                    }
                    --z;
                    possibleEndDateTime = possibleEndDateTime.plusMinutes(30);
                }
                else {
                    --z;
                    possibleEndDateTime = possibleEndDateTime.plusMinutes(30);
                }
            }
            ++x;
        }
        return _validEndTimes;
    }

}
