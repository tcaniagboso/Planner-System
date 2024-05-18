package plannersystem;

import java.io.File;
import java.util.List;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedulestrategy.ScheduleStrategy;
import controller.Observer;

/**
 * The PlannerSystem interface defines the contract for a system managing schedules and events for
 * users.
 * It provides functionalities to read and save user schedules from/to XML files, display schedules,
 * create, modify, and remove events, and even perform automatic scheduling.
 */
public interface PlannerSystem extends ReadonlyPlannerSystem {

  /**
   * Reads a user's schedule from an XML file and updates the system's user schedule map.
   * The method parses the XML file to create and add events to the user's schedule.
   *
   * @param xmlFile The XML file containing the user's schedule to be read.
   * @throws IllegalStateException    if there's an error creating the document builder, opening the
   *                                  file, or parsing the XML.
   * @throws IllegalArgumentException if the XML file contains invalid data that doesn't conform to
   *                                  expected structure.
   */
  void readUserSchedule(File xmlFile);

  /**
   * Creates an event and adds it to the schedule of the specified user and all invitees.
   * Validates event time to prevent schedule conflicts before adding the event.
   *
   * @param userId    The user ID of the event host.
   * @param name      The name of the event.
   * @param startDay  The start day of the event.
   * @param startTime The start time of the event.
   * @param endDay    The end day of the event.
   * @param endTime   The end time of the event.
   * @param isOnline  Indicates whether the event is online.
   * @param location  The location of the event (if not online).
   * @param invitees  A list of user IDs of the event invitees.
   * @throws IllegalArgumentException if event time conflicts with existing schedule.
   */
  void createEvent(String userId, String name, String startDay, String startTime, String endDay,
                   String endTime, boolean isOnline, String location, List<String> invitees);


  /**
   * Modifies an existing event with new details in the schedule of the specified user and
   * all invitees.
   * Validates the modified event time to prevent schedule conflicts.
   *
   * @param userId    The user ID of the event host.
   * @param event     The event to be modified.
   * @param name      The new name of the event.
   * @param startDay  The new start day of the event.
   * @param startTime The new start time of the event.
   * @param endDay    The new end day of the event.
   * @param endTime   The new end time of the event.
   * @param isOnline  Indicates whether the event is online.
   * @param location  The new location of the event (if not online).
   * @param invitees  A new list of user IDs of the event invitees.
   */
  void modifyEvent(String userId, ReadOnlyEvent event, String name, String startDay,
                   String startTime, String endDay, String endTime, boolean isOnline,
                   String location, List<String> invitees);

  /**
   * Removes an event from the user's schedule. If the user is the host of the event,
   * the event is removed from all invitees' schedules as well. If the user is not the host,
   * the event is only removed from their schedule.
   *
   * @param userId The user ID of the person attempting to remove the event.
   * @param event  The event to be removed.
   * @throws IllegalArgumentException If the event does not exist in the user's schedule or
   *                                  if the user does not exist.
   */
  void removeEvent(String userId, ReadOnlyEvent event);

  /**
   * Attempts to schedule a new event using the current scheduling strategy. This method constructs
   * a new event based on the provided parameters and uses the assigned scheduling strategy to find
   * an appropriate time slot. If a suitable time slot is found, the event is added to the schedules
   * of all its invitees. If no suitable time slot can be found, an exception is thrown.
   *
   * @param userId   The user ID of the event's host.
   * @param name     The name of the event to be scheduled.
   * @param isOnline Indicates whether the event is to be held online. True for online events; false
   *                 otherwise.
   * @param location The location of the event if it is not online.
   * @param duration The duration of the event in minutes.
   * @param invitees A list of user IDs representing the invitees to the event.
   * @throws IllegalArgumentException if no suitable time slot can be found for the event,
   *                                  indicating it is not possible to schedule the event as per the
   *                                  current scheduling constraints and availability.
   */
  void scheduleEvent(String userId, String name, boolean isOnline, String location, int duration,
                     List<String> invitees);

  /**
   * Registers an observer to be notified of changes to the planner system. Observers are typically
   * components interested in being informed about updates to the system's state, such as changes
   * to schedules or events. This allows for a decoupled architecture where components can react to
   * changes without having direct dependencies on each other.
   *
   * @param observer The {@link Observer} to be added to the list of registered observers. Once
   *                 added, the observer will receive updates on changes to the planner system.
   * @throws IllegalArgumentException if the observer is null, ensuring that only valid observers
   *                                  are registered.
   */
  void addObserver(Observer observer);

  /**
   * Unregisters an observer from the planner system. Once removed, the observer will no longer
   * receive updates about changes to the system's state. This method is typically called when an
   * observer is being destroyed or no longer needs to receive updates, helping to prevent memory
   * leaks and ensure that the system can manage its list of observers efficiently.
   *
   * @param observer The {@link Observer} to be removed from the list of registered observers.
   *                 If the observer is not currently registered, this method has no effect.
   * @throws IllegalArgumentException if the observer is null, ensuring that the method attempts
   *                                  to remove only valid observers.
   */
  void removeObserver(Observer observer);

  /**
   * Sets the scheduling strategy for the planner system. This strategy determines how events are
   * scheduled within the system, according to the specific rules and constraints of the strategy.
   * The strategy must implement the AutoSchedule interface.
   *
   * @param scheduleStrategy An instance of a class that implements the AutoSchedule interface,
   *                         representing the new scheduling strategy to be used by the planner
   *                         system.
   * @throws IllegalArgumentException if the provided scheduleStrategy is null, ensuring that the
   *                                  planner system always has a valid scheduling strategy.
   */
  void setScheduleStrategy(ScheduleStrategy scheduleStrategy);

  /**
   * Adds a schedule to the system for a specific user after validating that the schedule is not
   * null and that no schedule exists for that user. It checks for time conflicts between the events
   * in the schedule and existing events for the invitees. If a conflict is found or if the user
   * already has a schedule, it throws an IllegalArgumentException.
   *
   * @param schedule The schedule to be added. Must not be null and must not conflict with existing
   *                 schedules.
   * @throws IllegalArgumentException if the schedule is null, if a schedule for this user already
   *                                  exists, or if an event time conflicts with an existing event
   *                                  for its invitees.
   */
  void addSchedule(ISchedule schedule);

  /**
   * Adds a new user with a unique ID to the system. It first validates that the user ID is not null
   * and checks if the user already exists in the system. If the user exists, it throws an
   * IllegalArgumentException.
   *
   * @param userId The unique identifier for the new user to be added. Must not be null.
   * @throws IllegalArgumentException if the user ID is null or if a user with the same ID already
   *                                  exists.
   */
  void addUser(String userId);

  /**
   * Removes a user and their associated schedule from the system by user ID. It checks if the user
   * exists and, if so, first removes all associated events from the user's schedule before removing
   * the user. If the user does not exist, the method returns false.
   *
   * @param userId The unique identifier of the user to be removed. Must not be null.
   * @return true if the user was successfully removed; false if no such user exists.
   * @throws IllegalArgumentException if the user ID is null.
   */
  boolean removeUser(String userId);

  /**
   * Sets the first day of the week for the planner.
   *
   * @param firstDayOfWeek The first day of the week.
   * @throws IllegalArgumentException if the given day is null or invalid.
   */
  void setFirstDayOfWeek(String firstDayOfWeek);
}
