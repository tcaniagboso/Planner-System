package schedule;

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
   * Gets the start day of the event.
   *
   * @return The {@link DayOfWeek} indicating the start day of the event.
   */
  public DayOfWeek getStartDay() {
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
    return startTime;
  }

  /**
   * Sets the start time of the event. Validates the input to ensure it follows the HHmm format.
   *
   * @param startTime The start time of the event in HHmm format.
   * @throws IllegalArgumentException If the input does not represent a valid time in HHmm format.
   */
  public void setStartTime(String startTime) {
    this.startTime = this.validateTime(startTime);;
  }

  /**
   * Gets the end day of the event.
   *
   * @return The {@link DayOfWeek} indicating the end day of the event.
   */
  public DayOfWeek getEndDay() {
    return endDay;
  }

  /**
   * Sets the end day of the event. Validates the input to ensure it is a valid day of the week.
   *
   * @param endDay The day of the week that the event ends on, as a String.
   * @throws IllegalArgumentException If the input does not correspond to a valid {@link DayOfWeek}.
   */
  public void setEndDay(String endDay) {
    this.endDay = this.validateDay(endDay);
  }

  /**
   * Gets the end time of the event.
   *
   * @return The end time of the event as {@link LocalTime}.
   */
  public LocalTime getEndTime() {
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
    LocalTime parsedEndTime = this.validateTime(endTime);
    if (this.startDay != null) {
      // Calculate the total duration in minutes from start to end
      long daysBetween = (this.startDay.getValue() % 7)
              - (this.endDay.getValue() % 7);
      if (daysBetween < 0) { // If end day is in the next week
        daysBetween += 7;
      }
      long durationMinutes = daysBetween * 24 * 60; // Convert days to minutes
      durationMinutes += Duration.between(this.startTime, parsedEndTime).toMinutes();
      // Check if duration exceeds 6 days, 23 hours, 59 minutes
      if (durationMinutes > ((6 * 24 * 60) + (23 * 60) + 59)) {
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
   * Checks if the time period of this Time object overlaps with that of another.
   *
   * @param other The other Time object to compare with.
   * @return true if there is an overlap, false otherwise.
   */
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
