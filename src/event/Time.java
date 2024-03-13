package event;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents time information for an event, including start and end times along with their
 * respective days.
 */
public class Time {

  private DayOfWeek startDay;
  private LocalTime startTime;
  private DayOfWeek endDay;
  private LocalTime endTime;


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
  public LocalTime getStartTime() {
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
    this.startTime = this.validateTime(startTime);;
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
  public LocalTime getEndTime() {
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
    LocalTime parsedEndTime = this.validateTime(endTime);
    if (this.startDay != null) {
      // Calculate the total duration in minutes from start to end
      long daysBetween = DayOfWeek.valueOf(this.startDay.name()).getValue()
              - DayOfWeek.valueOf(this.endDay.name()).getValue();
      if (daysBetween < 0) { // If end day is in the next week
        daysBetween += 7;
      }
      long durationMinutes = daysBetween * 24 * 60; // Convert days to minutes
      durationMinutes += Duration.between(this.startTime, parsedEndTime).toMinutes();
      // Check if duration exceeds 6 days, 23 hours, 59 minutes
      if (durationMinutes > (6 * 24 * 60) + (23 * 60) + 59) {
        throw new IllegalArgumentException("Event duration cannot exceed 6 days, "
                + "23 hours, and 59 minutes.");
      }
    }
    this.endTime = parsedEndTime;
  }

  /**
   * Validates the provided time string to ensure it represents a valid time in HHmm format.
   *
   * @param timeString The time string to validate.
   * @throws IllegalArgumentException If the time string is not a valid time in HHmm format.
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
   *                                 {@link DayOfWeek}.
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

  public boolean overlap(Time other) {
    // Convert start and end times to minutes since start of week for comparison
    long thisStart = (this.startDay.getValue() % 7) * 1440 + this.startTime.getHour() * 60
            + this.startTime.getMinute();
    long thisEnd = (this.endDay.getValue() % 7) * 1440 + this.endTime.getHour() * 60
            + this.endTime.getMinute();
    long otherStart = (other.startDay.getValue() % 7) * 1440 + other.startTime.getHour() * 60
            + other.startTime.getMinute();
    long otherEnd = (other.endDay.getValue() % 7) * 1440 + other.endTime.getHour() * 60
            + other.endTime.getMinute();

    // Check for overlap
    return !(otherEnd <= thisStart || otherStart >= thisEnd);
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
}
