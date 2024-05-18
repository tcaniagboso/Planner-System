package schedulestrategy;

import java.time.DayOfWeek;
import java.util.List;

import schedule.IEvent;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import validationutilities.ValidationUtilities;

/**
 * Implements auto-scheduling for events within a predefined one-week period.
 * This scheduler attempts to find the earliest possible start time for an event
 * within the week that does not conflict with existing scheduled events.
 */
public class AnyTimeScheduleStrategy implements ScheduleStrategy {

  protected static final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
      "Thursday", "Friday", "Saturday"};

  protected String firstDayOfWeek;

  /**
   * Schedules an event at the earliest possible time within a week,
   * ensuring no overlap with existing events.
   *
   * @param event        The event to be scheduled. Must not be null.
   * @param duration     The duration of the event in minutes. Must be positive and cannot exceed
   *                     the length of a week.
   * @param scheduleList A list of existing schedules against which the event will be checked for
   *                     overlaps.
   * @return The scheduled event with updated start and end times if a suitable slot is found;
   *         otherwise, null.
   * @throws IllegalArgumentException If the event is null, duration is non-positive,
   *                                  or duration exceeds the maximum allowed length.
   */
  @Override
  public ReadOnlyEvent scheduleEvent(IEvent event, int duration, List<ISchedule> scheduleList) {
    ValidationUtilities.validateNull(event);
    this.validateDuration(duration);
    this.validateSchedules(scheduleList);
    int startMinute = 0;
    int endMinuteOfWeek = (6 * 1440) + (23 * 60) + 59;
    boolean foundTime = false;
    if (duration > endMinuteOfWeek) {
      throw new IllegalArgumentException("The duration of an event cannot be more than 6 days "
              + "23 hours and 59 minutes");
    }

    while (!foundTime && startMinute <= endMinuteOfWeek) {
      String startDay = durationToDay(startMinute);
      String startTime = durationToHours(startMinute) + durationToMinutes(startMinute);
      int endDuration = startMinute + duration;
      String endDay = durationToDay(endDuration);
      String endTime = durationToHours(endDuration) + durationToMinutes(endDuration);
      event.setEventTimes(startDay, startTime, endDay, endTime);
      foundTime = this.validateTime(event, scheduleList);
      startMinute += 1;
    }

    if (foundTime) {
      return event;
    }
    return null;
  }

  @Override
  public void setFirstDayOfWeek(String firstDayOfWeek) {
    if (this.firstDayOfWeek == null) {
      this.firstDayOfWeek = firstDayOfWeek.toUpperCase();
    }
  }


  /**
   * Validates if the proposed event times overlap with any event in the provided schedules.
   *
   * @param event        The event with proposed times to check.
   * @param scheduleList The list of schedules to check against.
   * @return true if an overlap exists; false otherwise.
   */
  protected boolean validateTime(IEvent event, List<ISchedule> scheduleList) {
    for (ISchedule schedule : scheduleList) {
      if (schedule.overlap(event, firstDayOfWeek)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Validates the duration of an event to ensure it's positive and does not exceed a week.
   *
   * @param duration The duration of an event in minutes.
   * @throws IllegalArgumentException If the duration is invalid.
   */
  protected void validateDuration(int duration) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Duration is invalid");
    }
  }

  /**
   * Converts a given duration in minutes to the corresponding day of the week.
   * The calculation is based on a week starting from Sunday as day 0.
   *
   * @param duration The duration in minutes from the start of the week (Sunday 00:00).
   * @return A string representing the day of the week for the given duration.
   */
  private String durationToDay(int duration) {
    int index = (duration / 1440) % 7;
    int difference = DayOfWeek.valueOf(firstDayOfWeek).getValue();
    return daysOfWeek[(index + difference) % 7];
  }

  /**
   * Converts a given duration in minutes to the corresponding hour of the day.
   * This method formats the hour in a two-digit string (e.g., "03" or "11").
   *
   * @param duration The duration in minutes from the start of the day (00:00).
   * @return A string formatted as "HH", representing the hour of the day.
   */
  protected String durationToHours(int duration) {
    int hour = (duration / 60) % 24;
    return String.format("%02d", hour);
  }

  /**
   * Converts a given duration in minutes to the minutes part of an hour.
   * This method formats the minutes in a two-digit string (e.g., "05" or "30").
   *
   * @param duration The total duration in minutes.
   * @return A string formatted as "MM", representing the minutes part of the hour.
   */
  protected String durationToMinutes(int duration) {
    int minutes = duration % 60;
    return String.format("%02d", minutes);
  }

  protected void validateSchedules(List<ISchedule> scheduleList) {
    if (scheduleList == null || scheduleList.isEmpty() || scheduleList.contains(null)) {
      throw new IllegalArgumentException("Invalid list of schedule.");
    }
  }
}
