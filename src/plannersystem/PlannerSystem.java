package plannersystem;

import java.io.File;
import java.util.List;

import schedule.Event;

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
   * Saves the current schedule of a user to an XML file.
   *
   * @param userId The ID of the user whose schedule is being saved.
   * @throws IllegalStateException if any error occurs during the saving process, encapsulating
   *                               the original exception message.
   */
  void saveUserSchedule(String userId);

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
  void modifyEvent(String userId, Event event, String name, String startDay, String startTime,
                   String endDay, String endTime, boolean isOnline, String location,
                   List<String> invitees);

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
  void removeEvent(String userId, Event event);

  /**
   * Automatically schedules an event, attempting to find a suitable time slot that accommodates
   * all participants. This method is a placeholder and not yet implemented.
   *
   * @param userId   The ID of the user requesting the automatic scheduling.
   * @param name     The name of the event to be scheduled.
   * @param isOnline Indicates if the event is to be held online.
   * @param location The physical location of the event, if not online.
   * @param invitees A list of IDs for users to be invited to the event.
   */
  void automaticScheduling(String userId, String name, boolean isOnline,
                           String location, List<String> invitees);
}
