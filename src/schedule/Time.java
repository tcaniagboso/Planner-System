package schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import validationutilities.ValidationUtilities;

/**
 * Represents the timing information for an event, including start and end times along with
 * their respective days. This class is responsible for managing time-related data for events,
 * ensuring time validity, and providing methods for time comparison.
 */
public class Time implements ITime {

  private DayOfWeek startDay;
  private LocalTime startTime;
  private DayOfWeek endDay;
  private LocalTime endTime;

  /**
   * Constructs a new Time object with specified start and end times for convenient testing.
   *
   * @param startDay  The start day as a string.
   * @param startTime The start time in HHmm format.
   * @param endDay    The end day as a string.
   * @param endTime   The end time in HHmm format.
   */
  public Time(String startDay, String startTime, String endDay, String endTime) {
    this.setStartDay(startDay);
    this.setStartTime(startTime);
    this.setEndDay(endDay);
    this.setEndTime(endTime);
  }

  /**
   * Default constructor for Time object. Initializes an instance without specific time details.
   */
  public Time() {
    // to represent an empty time constructor since there is a non-empty one, I had to declare this
  }

  @Override
  public DayOfWeek getStartDay() {
    ValidationUtilities.validateGetNull(this.startDay);
    return startDay;
  }

  @Override
  public void setStartDay(String startDay) {
    this.startDay = this.validateDay(startDay);
  }

  @Override
  public LocalTime getStartTime() {
    ValidationUtilities.validateGetNull(this.startTime);
    return startTime;
  }

  @Override
  public void setStartTime(String startTime) {
    if (startDay == null) {
      throw new IllegalStateException("Start day must be set before start time.");
    }
    this.startTime = this.validateTime(startTime);
  }

  @Override
  public DayOfWeek getEndDay() {
    ValidationUtilities.validateGetNull(this.endDay);
    return endDay;
  }

  @Override
  public void setEndDay(String endDay) {
    if (startDay == null || startTime == null) {
      throw new IllegalStateException("Start day and start time must be set before end day.");
    }
    this.endDay = this.validateDay(endDay);
  }

  @Override
  public LocalTime getEndTime() {
    ValidationUtilities.validateGetNull(this.endTime);
    return endTime;
  }

  @Override
  public void setEndTime(String endTime) {
    if (startDay == null || startTime == null || endDay == null) {
      throw new IllegalStateException("Start day, start time and end day must be "
              + "set before end time.");
    }
    LocalTime parsedEndTime = this.validateTime(endTime);
    if (startDay.equals(endDay) && startTime.equals(parsedEndTime)) {
      throw new IllegalArgumentException("An event cannot start and end at the same "
              + "time on the same day.");
    }
    this.endTime = parsedEndTime;
  }

  @Override
  public boolean overlap(ITime other, String firstDayOfWeek) {
    if (this.equals(other)) {
      return true;
    }
    int daysInWeek = 7;
    int minutesInDay = 1440;

    // Convert start and end times to minutes since the start of the week, considering Sunday as 0
    long thisStartMinutes = this.getMinutes(this.startDay, this.startTime, firstDayOfWeek);
    long thisEndMinutes = this.getMinutes(this.endDay, this.endTime, firstDayOfWeek);
    if (thisEndMinutes < thisStartMinutes) {
      thisEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    long otherStartMinutes = this.getMinutes(other.getStartDay(), other.getStartTime(),
            firstDayOfWeek);
    long otherEndMinutes = this.getMinutes(other.getEndDay(), other.getEndTime(), firstDayOfWeek);
    if (otherEndMinutes < otherStartMinutes) {
      otherEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    // Check for overlap considering wrap-around
    return !(otherEndMinutes <= thisStartMinutes || otherStartMinutes >= thisEndMinutes);
  }

  @Override
  public boolean occurs(String day, String time, String firstDayOfWeek) {
    DayOfWeek givenDay = DayOfWeek.valueOf(day.toUpperCase());
    LocalTime givenTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));

    int daysInWeek = 7;
    int minutesInDay = 1440; // 24 hours * 60 minutes

    // Convert the event's start and end times to minutes since the start of the week
    long eventStartMinutes = this.getMinutes(this.startDay, this.startTime, firstDayOfWeek);
    long eventEndMinutes = this.getMinutes(this.endDay, this.endTime, firstDayOfWeek);

    if (eventEndMinutes < eventStartMinutes) {
      eventEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    // Convert the given day and time to minutes since the start of the week
    long givenMinutes = ((givenDay.getValue() + this.difference(firstDayOfWeek)) % 7) * minutesInDay
            + givenTime.getHour() * 60 + givenTime.getMinute();

    // Check if the given day and time occur during the event
    // It occurs if it's after the start and strictly before the end
    return givenMinutes >= eventStartMinutes && givenMinutes < eventEndMinutes;
  }

  @Override
  public boolean wrapsAround(String firstDayOfWeek) {
    int difference = this.difference(firstDayOfWeek);
    int endDayValue = (this.endDay.getValue() + difference) % 7;
    int startDayValue = (this.startDay.getValue() + difference) % 7;
    return (endDayValue < startDayValue)
            || (this.startDay.equals(this.endDay) && this.endTime.isBefore(this.startTime));
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Time)) {
      return false;
    }

    Time other = (Time) object;
    return this.startTime.equals(other.getStartTime()) && this.endTime.equals(other.getEndTime())
            && this.startDay.equals(other.getStartDay()) && this.endDay.equals(other.getEndDay());
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDay, startTime, endDay, endTime);
  }

  /**
   * Validates the provided time string to ensure it represents a valid time in HHmm format.
   *
   * @param timeString The time string to validate.
   * @return A {@link LocalTime} object representing the validated time.
   * @throws IllegalArgumentException If the time string does not represent a valid time in HHmm
   *                                  format.
   */
  private LocalTime validateTime(String timeString) {
    if (timeString == null || timeString.trim().length() != 4) {
      throw new IllegalArgumentException("The chosen time is invalid");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    try {
      return LocalTime.parse(timeString.trim(), formatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Validates the provided day string to ensure it represents a valid {@link DayOfWeek}.
   *
   * @param dayString The day string to validate.
   * @return The validated {@link DayOfWeek}.
   * @throws IllegalArgumentException If the day string does not represent a valid
   *                                  {@link DayOfWeek}.
   */
  private DayOfWeek validateDay(String dayString) {
    if (dayString == null || dayString.isBlank()) {
      throw new IllegalArgumentException("The chosen day is invalid");
    }
    try {
      return DayOfWeek.valueOf(dayString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Calculates the total minutes from the start of the week for a given day and time.
   * This method is used for internal calculations related to event scheduling and overlap checks.
   *
   * @param day  The day of the week.
   * @param time The time of day.
   * @param firstDayOfWeek The first day of the week.
   * @return The total number of minutes from the start of the week to the specified day and time.
   */
  private long getMinutes(DayOfWeek day, LocalTime time, String firstDayOfWeek) {
    int difference = this.difference(firstDayOfWeek);
    return ((day.getValue() + difference) % 7) * 1440
            + time.getHour() * 60 + time.getMinute();
  }

  /**
   * Calculates the difference between the value of the current day and 7.
   *
   * @param day The day we are concerned with.
   * @return the difference from the original start day Sunday.
   */
  private int difference(String day) {
    return 7 - (DayOfWeek.valueOf(day.toUpperCase()).getValue());
  }
}
