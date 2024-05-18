package schedulestrategy;

import java.util.List;

import schedule.IEvent;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;

/**
 * Defines the contract for auto-scheduling events. Implementations of this interface
 * are responsible for automatically scheduling an event at an appropriate time slot,
 * taking into account the event's duration and avoiding conflicts with existing schedules.
 */
public interface ScheduleStrategy {

  /**
   * Schedules an event based on its duration and a list of existing schedules,
   * returning the event with updated scheduling details if successful.
   *
   * @param event        The event to be scheduled.
   * @param duration     The duration of the event in minutes.
   * @param scheduleList A list of existing schedules to check for conflicts.
   * @return The scheduled event with updated start and end times, or null if
   *         scheduling was unsuccessful.
   */
  ReadOnlyEvent scheduleEvent(IEvent event, int duration, List<ISchedule> scheduleList);

  /**
   * Sets the first day of the week for the schedule strategy.
   *
   * @param firstDayOfWeek the day to be set.
   */
  void setFirstDayOfWeek(String firstDayOfWeek);
}
