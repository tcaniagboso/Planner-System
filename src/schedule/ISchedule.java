package schedule;

import java.util.List;

/**
 * Defines the contract for a schedule management system associated with a specific user.
 * This interface provides methods to manipulate and query events within a user's schedule.
 */
public interface ISchedule {

  /**
   * Gets the user ID associated with this schedule.
   *
   * @return The user ID.
   */
  String getUserName();

  /**
   * Retrieves a copy of the list of events in this schedule.
   *
   * @return A new list containing all the events.
   */
  List<ReadOnlyEvent> getEvents();

  /**
   * Adds a new event to this schedule if there's no time overlap with existing events.
   *
   * @param event The event to be added.
   * @throws IllegalArgumentException If the event is null or if an overlap with an existing event
   *                                  is detected.
   */
  void addEvent(ReadOnlyEvent event);

  /**
   * Removes a specified event from the schedule after validating its presence.
   *
   * @param event The event to remove.
   * @throws IllegalArgumentException If the event is null
   */
  void removeEvent(ReadOnlyEvent event);

  /**
   * Checks for time overlaps between the new event and any event already in the schedule.
   *
   * @param newEvent The new event being checked for overlap.
   * @param firstDayOfWeek The first day of the week.
   * @return True if an overlap is detected, otherwise false.
   */
  boolean overlap(ReadOnlyEvent newEvent, String firstDayOfWeek);

  /**
   * Determines whether a specific event is present in this schedule.
   *
   * @param event The event to check.
   * @return True if the event exists in the schedule, otherwise false.
   */
  boolean hasEvent(ReadOnlyEvent event);

  /**
   * Sorts the schedule's events first by day of the week and then by start time.
   */
  void sortSchedule();

  /**
   * Finds an event occurring at a specified day and time.
   *
   * @param day  The day of the event.
   * @param time The time of the event.
   * @param firstDayOfWeek The first day of the week.
   * @return The event if found, otherwise null.
   */
  ReadOnlyEvent findEvent(String day, String time, String firstDayOfWeek);

  /**
   * Retrieves a specific event from the schedule if it exists.
   * This method checks for the presence of an event in the schedule by comparing it with
   * the provided event parameter. The comparison is based on the implementation of the
   * {@code equals} method in the {@code Event} class. If the event is found, it is returned;
   * otherwise, {@code null} is returned.
   *
   * @param event The event to be retrieved from the schedule.
   * @return The matching event from the schedule if it exists; otherwise, {@code null}.
   * @throws IllegalArgumentException if the provided event is {@code null} or does not exist
   *                                  in the schedule.
   */
  ReadOnlyEvent getEvent(ReadOnlyEvent event);
}
