package event;

import java.time.DayOfWeek;
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
    if (this.startDay == this.endDay && this.startDay != null && this.startTime.equals(endTime)) {
      throw new IllegalArgumentException("An event cannot last for more than 6 days 23 hours and "
              + "59 minutes.");
    }
    this.endTime = this.validateTime(endTime);
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

  public boolean overlap(Time time) {
    if (this.sameDay(this.startDay, time.startDay)) {
      if(time.endNextWeek() && !this.endNextWeek()) {
        return true;
      }
      if (this.endNextWeek()) {
        if(time.endNextWeek()) {
          return true;
        }
        if(this.laterDay(time.endDay, this.startDay)) {
          return true;
        }
        if(this.sameDay(time.endDay, this.startDay) && time.endTime.isAfter(this.startTime)) {
          return true;
        }
      }
      if (time.startTime.isAfter(this.startTime)) {
        if (this.sameDay(time.startDay, this.endDay) && time.startTime.isBefore(this.endTime)) {
          return true;
        }
        if (this.laterDay(this.endDay, time.startDay)) {
          return true;
        }
      }
      if (this.startTime.isAfter(time.startTime)) {
        if (this.sameDay(this.startDay, time.endDay) && this.startTime.isBefore(time.endTime)) {
          return true;
        }
        if (this.laterDay(time.endDay, this.startDay)) {
          return true;
        }
      }
    }
    if (this.laterDay(this.startDay, time.startDay)) {
      if (time.endNextWeek()) {
        return true;
      }
      if (this.laterDay(time.endDay, this.startDay)) {
        return true;
      }
      if (this.sameDay(time.endDay, this.startDay) && time.endTime.isAfter(this.startTime)) {
        return true;
      }
    }
    if (this.laterDay(time.startDay, this.startDay)) {
      if (this.endNextWeek()) {
        return true;
      }
      if (this.laterDay(this.endDay, time.startDay)) {
        return true;
      }
      if (this.sameDay(this.endDay, time.startDay) && this.endTime.isAfter(time.startTime)) {
        return true;
      }
    }

    return false;
  }

  private boolean endNextWeek() {
    return this.laterDay(this.startDay, this.endDay) || (this.sameDay(this.startDay, this.endDay)
            && this.endTime.isBefore(this.startTime));
  }

  private boolean sameDay(DayOfWeek day1, DayOfWeek day2) {
    return ((day1.getValue() % 7) == (day2.getValue() % 7));
  }

  private boolean laterDay(DayOfWeek day1, DayOfWeek day2) {
    return ((day1.getValue() % 7) > (day2.getValue() % 7));
  }


}
