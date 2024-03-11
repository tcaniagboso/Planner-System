package event;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents time information for an event, including start and end times along with their
 * respective days.
 */
public class Time {

  private DayOfWeek startDay;
  private String startTime;
  private DayOfWeek endDay;
  private String endTime;


  /**
   * Retrieves the start day of the event.
   *
   * @return The {@link DayOfWeek} indicating the start day of the event.
   */
  public DayOfWeek getStartDay() {
    return startDay;
  }

  /**
   * Sets the start day of the event.
   * The day is validated to ensure it represents a valid day of the week.
   *
   * @param startDay The day of the week as a String that the event starts on.
   * @throws IllegalArgumentException If the provided string does not represent a valid
   *                                 {@link DayOfWeek}.
   */
  public void setStartDay(String startDay) {
    this.startDay = this.validateDay(startDay);
  }

  /**
   * Retrieves the start time of the event.
   *
   * @return A string representing the start time in HHmm format.
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * Sets the start time of the event.
   * The time is validated to ensure it follows the HHmm format.
   *
   * @param startTime The start time of the event in HHmm format.
   * @throws IllegalArgumentException If the provided string does not represent a valid time in
   *                                  HHmm format.
   */
  public void setStartTime(String startTime) {
    this.validateTime(startTime);
    this.startTime = startTime;
  }

  /**
   * Retrieves the end day of the event.
   *
   * @return The {@link DayOfWeek} indicating the end day of the event.
   */
  public DayOfWeek getEndDay() {
    return endDay;
  }

  /**
   * Sets the end day of the event.
   * The day is validated to ensure it represents a valid day of the week.
   *
   * @param endDay The day of the week as a String that the event ends on.
   * @throws IllegalArgumentException If the provided string does not represent a valid
   *                                  {@link DayOfWeek}.
   */
  public void setEndDay(String endDay) {
    this.endDay = this.validateDay(endDay);
  }

  /**
   * Retrieves the end time of the event.
   *
   * @return A string representing the end time in HHmm format.
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * Sets the end time of the event.
   * The time is validated to ensure it follows the HHmm format. Additionally,
   * it checks that the end time does not create an event duration that exceeds
   * the logical constraints.
   *
   * @param endTime The end time of the event in HHmm format.
   * @throws IllegalArgumentException If the provided string does not represent a valid time in
   *                                  HHmm format or violates event duration constraints.
   */
  public void setEndTime(String endTime) {
    this.validateTime(endTime);
    if (this.startDay == this.endDay && this.startDay != null && this.startTime.equals(endTime)) {
      throw new IllegalArgumentException("An event cannot last for more than 6 days 23 hours and "
              + "59 minutes.");
    }
    this.endTime = endTime;
  }

  /**
   * Validates the provided time string to ensure it represents a valid time in HHmm format.
   *
   * @param timeString The time string to validate.
   * @throws IllegalArgumentException If the time string is not a valid time in HHmm format.
   */
  private void validateTime(String timeString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    try {
      LocalTime.parse(timeString, formatter);
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
   *                                 {@link DayOfWeek}.
   */
  private DayOfWeek validateDay(String dayString) {
    try {
      return DayOfWeek.valueOf(dayString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
