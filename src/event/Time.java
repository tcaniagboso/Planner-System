package event;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Time {

  private DayOfWeek startDay;
  private String startTime;
  private DayOfWeek endDay;
  private String endTime;


  public DayOfWeek getStartDay() {
    return startDay;
  }

  public void setStartDay(String startDay) {
    this.startDay = this.validateDay(startDay);
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.validateTime(startTime);
    this.startTime = startTime;
  }

  public DayOfWeek getEndDay() {
    return endDay;
  }

  public void setEndDay(String endDay) {
    this.endDay = this.validateDay(endDay);
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.validateTime(endTime);
    if (this.startDay == this.endDay && this.startDay != null && this.startTime.equals(endTime)) {
      throw new IllegalArgumentException("An event cannot last for more than 6 days 23 hours and "
              + "59 minutes.");
    }
    this.endTime = endTime;
  }

  // to check if the given time is valid
  private void validateTime(String timeString) throws IllegalArgumentException{
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    try {
      LocalTime.parse(timeString, formatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  // to check if the given day is valid
  private DayOfWeek validateDay(String dayString) {
    try {
      return DayOfWeek.valueOf(dayString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
