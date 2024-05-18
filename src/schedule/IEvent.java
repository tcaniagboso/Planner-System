package schedule;

import java.util.List;

/**
 * Represents an editable event within a scheduling system. This interface extends
 * {@link ReadOnlyEvent} to provide methods that allow modifications to an event's properties.
 */
public interface IEvent extends ReadOnlyEvent {

  /**
   * Sets the name of the event. The name must not be null or empty.
   *
   * @param name The name to assign to this event.
   * @throws IllegalArgumentException If the name is null or empty.
   */
  void setName(String name);

  /**
   * Sets the start and end times for the event, including the days. This is a convenience
   * method to update all time-related fields in one call.
   *
   * @param startDay  The start day of the event.
   * @param startTime The start time of the event.
   * @param endDay    The end day of the event.
   * @param endTime   The end time of the event.
   */
  void setEventTimes(String startDay, String startTime, String endDay, String endTime);

  /**
   * Configures the location details for this event.
   *
   * @param isOnline Indicates whether the event is held online.
   * @param location The physical location of the event if it's not online.
   */
  void setLocation(boolean isOnline, String location);

  /**
   * Updates the list of invitees for this event. The provided list must not be null and must
   * include the host.
   *
   * @param invitees The new list of invitees' IDs.
   * @throws IllegalArgumentException If the list is null, contains null elements,
   *                                  or does not include the host.
   */
  void setInvitees(List<String> invitees);

  /**
   * Sets the host of this event. The host cannot be null.
   *
   * @param host The User object representing the host of the event.
   * @throws IllegalArgumentException if the host parameter is null.
   */
  void setHost(String host);

  /**
   * Adds a User as an invitee to the event. Validates that the User is not null before adding.
   *
   * @param invitee The user to be added as an invitee to the event.
   * @throws IllegalArgumentException if the invitee is null.
   */
  void addInvitee(String invitee);

  /**
   * Removes a User from the event's list of invitees.
   * Validates that the User is not null before attempting removal.
   *
   * @param invitee The user to be removed from the event's invitees.
   * @throws IllegalArgumentException if the invitee is null.
   */
  void removeInvitee(String invitee);

  /**
   * Clears all invitees from the event.
   */
  void clearInvitees();
}
