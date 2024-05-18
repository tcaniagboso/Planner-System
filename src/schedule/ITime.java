package schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * ITime interface provides methods to manage and query the timing aspects of an event.
 * It handles setting and getting the start and end times and days of an event, checking for time
 * overlaps, and validating time continuity over weeks.
 */
public interface ITime {

  /**
   * Gets the start day of the event.
   *
   * @return The {@link DayOfWeek} indicating the start day of the event.
   */
  DayOfWeek getStartDay();

  /**
   * Sets the start day of the event. Validates the input to ensure it is a valid day of the week.
   *
   * @param startDay The day of the week as a String that the event starts on.
   * @throws IllegalArgumentException If the input does not correspond to a valid {@link DayOfWeek}.
   */
  void setStartDay(String startDay);

  /**
   * Gets the start time of the event.
   *
   * @return The start time of the event as {@link LocalTime}.
   */
  LocalTime getStartTime();

  /**
   * Sets the start time of the event. Validates the input to ensure it follows the HHmm format.
   *
   * @param startTime The start time of the event in HHmm format.
   * @throws IllegalArgumentException If the input does not represent a valid time in HHmm format.
   */
  void setStartTime(String startTime);

  /**
   * Gets the end day of the event.
   *
   * @return The {@link DayOfWeek} indicating the end day of the event.
   */
  DayOfWeek getEndDay();

  /**
   * Sets the end day of the event. Validates the input to ensure it is a valid day of the week.
   *
   * @param endDay The day of the week that the event ends on, as a String.
   * @throws IllegalArgumentException If the input does not correspond to a valid {@link DayOfWeek}.
   */
  void setEndDay(String endDay);

  /**
   * Gets the end time of the event.
   *
   * @return The end time of the event as {@link LocalTime}.
   */
  LocalTime getEndTime();

  /**
   * Sets the end time of the event. Validates the input to ensure it follows the HHmm format and
   * does not result in a duration exceeding the maximum allowed (6 days, 23 hours, 59 minutes).
   *
   * @param endTime The end time of the event in HHmm format.
   * @throws IllegalArgumentException If the input does not represent a valid time in HHmm format
   *                                  or if setting this end time would exceed the maximum event
   *                                  duration.
   */
  void setEndTime(String endTime);

  /**
   * Checks if the time period of this Time object overlaps with that of another.
   *
   * @param other The other ITime object to compare with.
   * @param firstDayOfWeek the first day of the week.
   * @return true if there is an overlap, false otherwise.
   */
  boolean overlap(ITime other, String firstDayOfWeek);

  /**
   * Determines whether this Time instance occurs on a specific day and time.
   *
   * @param day  The day to check, as a string.
   * @param time The time to check, in HHmm format.
   * @param firstDayOfWeek the first day of the week.
   * @return true if this Time occurs at the specified day and time, false otherwise.
   */
  boolean occurs(String day, String time, String firstDayOfWeek);

  /**
   * Checks if an event time continues into a new week.
   *
   * @param firstDayOfWeek The first day of the week.
   * @return true if an event time continues into a new week otherwise return false.
   */
  boolean wrapsAround(String firstDayOfWeek);
}
