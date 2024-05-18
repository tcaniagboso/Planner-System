package schedule;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Provides a read-only view of event details. This interface is used to access information about
 * an event without modifying it. It includes methods for retrieving detailed attributes like the
 * event's name, time, location, and participants.
 */
public interface ReadOnlyEvent {

  /**
   * Retrieves the name of the event.
   *
   * @return The name of the event.
   * @throws IllegalStateException If the event name has not been set.
   */
  String getName();

  /**
   * Retrieves the time details associated with this event.
   *
   * @return The {@link ITime} object representing the event's timing.
   */
  ITime getTime();

  /**
   * Retrieves the location details for this event.
   *
   * @return The {@link ILocation} object representing the event's location.
   */
  ILocation getEventLocation();

  /**
   * Retrieves a list of invitees to this event.
   *
   * @return A list containing the invitees' IDs.
   */
  List<String> getInvitees();

  /**
   * Retrieves the host's ID for this event.
   *
   * @return The host's ID.
   * @throws IllegalStateException If the host has not been set.
   */
  String getHost();

  /**
   * Determines if this event overlaps with another event in terms of time.
   * Overlapping is determined based on the start and end times of both events.
   *
   * @param event The event to check for an overlap with.
   * @param firstDayOfWeek The first day of the week.
   * @return true if the events overlap in time, false otherwise.
   */
  boolean overlap(ReadOnlyEvent event, String firstDayOfWeek);

  /**
   * Checks if this event occurs on a specific day and time.
   * This method is used to find if an event matches a given day and time,
   * useful for querying events in a schedule.
   *
   * @param day  The day to check the event against.
   * @param time The time to check the event against.
   * @param firstDayOfWeek The first day of the week.
   * @return true if the event occurs on the specified day and time, false otherwise.
   */
  boolean occurs(String day, String time, String firstDayOfWeek);

  /**
   * Checks if an event continues into a new week.
   *
   * @param firstDayOfWeek The first day of the week.
   * @return true if an event continues into a new week otherwise return false.
   */
  boolean wrapsAround(String firstDayOfWeek);

  /**
   * Returns the starting day of the event.
   *
   * @return the {@link DayOfWeek} representing the start day of the event.
   */
  DayOfWeek getStartDay();

  /**
   * Converts and returns the start time of the event from a LocalTime to an integer representation.
   *
   * @return the start time as an integer. For example, 09:30 would be returned as 930.
   */
  int getStartTime();

  /**
   * Returns the ending day of the event.
   *
   * @return the {@link DayOfWeek} representing the end day of the event.
   */
  DayOfWeek getEndDay();

  /**
   * Converts and returns the end time of the event from a LocalTime to an integer representation.
   *
   * @return the end time as an integer. For example, 17:45 would be returned as 1745.
   */
  int getEndTime();

  /**
   * Returns the location of the event.
   *
   * @return a string representing the location of the event.
   */
  String getLocation();

  /**
   * Determines if the event is conducted online.
   *
   * @return true if the event is online, false otherwise.
   */
  boolean isOnline();
}
