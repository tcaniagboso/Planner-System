package provider.model;

import java.util.List;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;

/**
 * A ReadOnlyCentralSystem that only allows users to retrieve some data about the central system.
 * It does not allow for any sort of mutating capabilities.
 */
public interface ReadOnlyCentralSystem {

  /**
   * A method that returns a list of strings with every single username in the system so far.
   *
   * @return a list of strings with all the userNames in the system
   */
  List<String> allUsersInSystem();

  /**
   * A method that checks if the given event conflicts with any other event for the invited users.
   *
   * @param event the event that is going to be checked if conflicted
   * @return true if the event is conflicting somewhere, otherwise false
   */
  boolean conflictingEvent(ReadOnlyEvent event);

  /**
   * A method that returns a list of events in a users schedule.
   *
   * @param userName the user being searched
   * @return a list of events in the user's schedule
   * @throws IllegalArgumentException if the given user is not found
   */
  List<ReadOnlyEvent> eventList(String userName);

  /**
   * Returns a particular user's schedule within the system.
   *
   * @param userName the user being searched
   * @throws IllegalArgumentException if the given user is not found
   */
  ISchedule getUserSchedule(String userName);
}
