package autoscheduling;

import java.util.List;

import schedule.Event;
import schedule.Schedule;
import validationutilities.ValidationUtilities;

/**
 * Extends AnyTimeSchedule to specifically schedule events within standard work hours
 * (09:00 to 17:00) from Monday to Friday.
 */
public class WorkHourSchedule extends AnyTimeSchedule {

  /**
   * Schedules an event within standard work hours (09:00 to 17:00) from Monday to Friday,
   * ensuring no overlap with existing events.
   *
   * @param event        The event to be scheduled. Must not be null.
   * @param duration     The duration of the event in minutes. Must be positive and fit within work
   *                     hours.
   * @param scheduleList A list of existing schedules against which the event will be checked
   *                     for overlaps.
   * @return The scheduled event with updated start and end times if a suitable slot is found;
   * otherwise, null.
   * @throws IllegalArgumentException If the event is null, duration is non-positive, or scheduling
   *                                  is not possible within specified work hours.
   */
  @Override
  public Event scheduleEvent(Event event, int duration, List<Schedule> scheduleList) {
    ValidationUtilities.validateNull(event);
    this.validateDuration(duration);
    int minutesInDay = 1440;
    int maxDuration = 8 * 60;
    final int workStart = 9 * 60;
    final int workEnd = 17 * 60;
    boolean foundTime = false;
    if (duration > maxDuration) {
      throw new IllegalArgumentException("The duration cannot be more than 8 working hours");
    }
    int day = 1;
    int startMinute = (day * minutesInDay) + workStart;
    while (day <= 5 && !foundTime) {
      String curDay = daysOfWeek[day];
      String startTime = durationToHours(startMinute) + durationToMinutes(startMinute);
      int endMinuteOfDuration = startMinute + duration;
      String endTime = durationToHours(endMinuteOfDuration)
              + durationToMinutes(endMinuteOfDuration);
      int endOfDay = (day * minutesInDay) + workEnd;
      if (startMinute > (endOfDay - duration)) {
        day++;
        startMinute = (day * minutesInDay) + workStart;
      } else {
        event.setEventTimes(curDay, startTime, curDay, endTime);
        foundTime = this.validateTime(event, scheduleList);
        startMinute++;
      }
    }

    if (foundTime) {
      return event;
    } else {
      return null;
    }
  }
}
