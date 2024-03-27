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
public class Time {

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

  /**
   * Gets the start day of the event.
   *
   * @return The {@link DayOfWeek} indicating the start day of the event.
   */
  public DayOfWeek getStartDay() {
    ValidationUtilities.validateGetNull(this.startDay);
    return startDay;
  }

  /**
   * Sets the start day of the event. Validates the input to ensure it is a valid day of the week.
   *
   * @param startDay The day of the week as a String that the event starts on.
   * @throws IllegalArgumentException If the input does not correspond to a valid {@link DayOfWeek}.
   */
  public void setStartDay(String startDay) {
    this.startDay = this.validateDay(startDay);
  }

  /**
   * Gets the start time of the event.
   *
   * @return The start time of the event as {@link LocalTime}.
   */
  public LocalTime getStartTime() {
    ValidationUtilities.validateGetNull(this.startTime);
    return startTime;
  }

  /**
   * Sets the start time of the event. Validates the input to ensure it follows the HHmm format.
   *
   * @param startTime The start time of the event in HHmm format.
   * @throws IllegalArgumentException If the input does not represent a valid time in HHmm format.
   */
  public void setStartTime(String startTime) {
    if (startDay == null) {
      throw new IllegalStateException("Start day must be set before start time.");
    }
    this.startTime = this.validateTime(startTime);
  }

  /**
   * Gets the end day of the event.
   *
   * @return The {@link DayOfWeek} indicating the end day of the event.
   */
  public DayOfWeek getEndDay() {
    ValidationUtilities.validateGetNull(this.endDay);
    return endDay;
  }

  /**
   * Sets the end day of the event. Validates the input to ensure it is a valid day of the week.
   *
   * @param endDay The day of the week that the event ends on, as a String.
   * @throws IllegalArgumentException If the input does not correspond to a valid {@link DayOfWeek}.
   */
  public void setEndDay(String endDay) {
    if (startDay == null || startTime == null) {
      throw new IllegalStateException("Start day and start time must be set before end day.");
    }
    this.endDay = this.validateDay(endDay);
  }

  /**
   * Gets the end time of the event.
   *
   * @return The end time of the event as {@link LocalTime}.
   */
  public LocalTime getEndTime() {
    ValidationUtilities.validateGetNull(this.endTime);
    return endTime;
  }

  /**
   * Sets the end time of the event. Validates the input to ensure it follows the HHmm format and
   * does not result in a duration exceeding the maximum allowed (6 days, 23 hours, 59 minutes).
   *
   * @param endTime The end time of the event in HHmm format.
   * @throws IllegalArgumentException If the input does not represent a valid time in HHmm format
   *                                  or if setting this end time would exceed the maximum event
   *                                  duration.
   */
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

  /**
   * Checks if the time period of this Time object overlaps with that of another.
   *
   * @param other The other Time object to compare with.
   * @return true if there is an overlap, false otherwise.
   */
  public boolean overlap(Time other) {
    if (this.equals(other)) {
      return true;
    }
    int daysInWeek = 7;
    int minutesInDay = 1440;

    // Convert start and end times to minutes since the start of the week, considering Sunday as 0
    long thisStartMinutes = this.getMinutes(this.startDay, this.startTime);
    long thisEndMinutes = this.getMinutes(this.endDay, this.endTime);
    if (thisEndMinutes < thisStartMinutes) {
      thisEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    long otherStartMinutes = this.getMinutes(other.startDay, other.startTime);
    long otherEndMinutes = this.getMinutes(other.endDay, other.endTime);
    if (otherEndMinutes < otherStartMinutes) {
      otherEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    // Check for overlap considering wrap-around
    return !(otherEndMinutes <= thisStartMinutes || otherStartMinutes >= thisEndMinutes);
  }

  /**
   * Determines whether this Time instance occurs on a specific day and time.
   *
   * @param day  The day to check, as a string.
   * @param time The time to check, in HHmm format.
   * @return true if this Time occurs at the specified day and time, false otherwise.
   */
  public boolean occurs(String day, String time) {
    DayOfWeek givenDay = DayOfWeek.valueOf(day.toUpperCase());
    LocalTime givenTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));

    int daysInWeek = 7;
    int minutesInDay = 1440; // 24 hours * 60 minutes

    // Convert the event's start and end times to minutes since the start of the week
    long eventStartMinutes = this.getMinutes(this.startDay, this.startTime);
    long eventEndMinutes = this.getMinutes(this.endDay, this.endTime);

    if (eventEndMinutes < eventStartMinutes) {
      eventEndMinutes += daysInWeek * minutesInDay; // Adjust for wrap-around
    }

    // Convert the given day and time to minutes since the start of the week
    long givenMinutes = (givenDay.getValue() % 7) * minutesInDay
            + givenTime.getHour() * 60 + givenTime.getMinute();

    // Check if the given day and time occur during the event
    // It occurs if it's after the start and strictly before the end
    return givenMinutes >= eventStartMinutes && givenMinutes < eventEndMinutes;
  }

  /**
   * Checks if an event time continues into a new week.
   *
   * @return true if an event time continues into a new week otherwise return false.
   */
  public boolean wrapsAround() {
    return ((this.endDay.getValue() % 7) < (this.startDay.getValue() % 7))
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
    if (timeString == null || timeString.length() != 4) {
      throw new IllegalArgumentException("The chosen time is invalid");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    try {
      return LocalTime.parse(timeString, formatter);
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
    if (dayString == null || dayString.isEmpty()) {
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
   * @return The total number of minutes from the start of the week to the specified day and time.
   */
  private long getMinutes(DayOfWeek day, LocalTime time) {
    return (day.getValue() % 7) * 1440
            + time.getHour() * 60 + time.getMinute();
  }
}
