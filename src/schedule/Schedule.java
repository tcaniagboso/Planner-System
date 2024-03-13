package schedule;

import java.util.ArrayList;
import java.util.List;

import event.Event;
import event.Time;
import user.User;

/**
 * Represents a schedule for a user, containing a list of events.
 * Each schedule is associated with a specific user ID and can add, modify, or remove events,
 * ensuring no overlapping events exist within the schedule.
 */
public class Schedule {
  private final String userId; // The ID of the user owning this schedule
  private List<Event> events; // A list of events in this schedule

  /**
   * Constructs a new Schedule for the specified user.
   * Initializes an empty list of events.
   *
   * @param userId The unique identifier for the user owning this schedule.
   * @throws IllegalArgumentException If the user ID is null.
   */
  public Schedule(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
    this.userId = userId;
    this.events = new ArrayList<>();
  }

  /**
   * Adds an event to the schedule after checking for any time overlap with existing events.
   *
   * @param event The event to add to the schedule.
   * @throws IllegalArgumentException If the event is null or overlaps
   *                                  with an existing event in the schedule.
   */
  public void addEvent(Event event) {
    this.validateEvent(event);
    for (Event cur : events) {
      if (cur.overlap(event)) {
        throw new IllegalArgumentException("Time conflict");
      }
    }
    this.events.add(event);
  }

  /**
   * Modifies an existing event in the schedule with new details provided.
   *
   * @param event     The event to modify.
   * @param name      The new name for the event.
   * @param startDay  The new start day for the event.
   * @param startTime The new start time for the event.
   * @param endDay    The new end day for the event.
   * @param endTime   The new end time for the event.
   * @param isOnline  The new online status for the event.
   * @param location  The new location for the event.
   * @param invitees  The new list of invitees for the event.
   * @throws IllegalArgumentException If the event does not exist in the schedule
   * or the new event details are invalid.
   */
  public void modifyEvent(Event event, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<User> invitees) {
    this.validateEvent(event);
    this.validateEventExists(event);

    this.modifyEvent(event, name);
    this.modifyEvent(event, startDay, startTime, endDay, endTime);
    this.modifyEvent(event, isOnline, location);
    this.modifyEvent(event, invitees);
  }

  /**
   * Updates the name of the specified event.
   * Validates that the event is not null and exists in the schedule before setting its new name.
   *
   * @param event The event to be modified.
   * @param name The new name to assign to the event.
   * @throws IllegalArgumentException if the event is null or does not exist in the schedule.
   */
  public void modifyEvent(Event event, String name) {
    this.validateEvent(event);
    this.validateEventExists(event);

    event.setName(name);
  }

  /**
   * Updates the timing details of the specified event.
   * Validates that the event is not null and exists in the schedule before setting
   * its new time.
   *
   * @param event The event to be modified.
   * @param startDay The new start day for the event.
   * @param startTime The new start time for the event.
   * @param endDay The new end day for the event.
   * @param endTime The new end time for the event.
   * @throws IllegalArgumentException if the event is null or does not exist in the schedule.
   */
  public void modifyEvent(Event event, String startDay, String startTime,
                          String endDay, String endTime) {
    this.validateEvent(event);
    this.validateEventExists(event);
    event.setEventTimes(startDay, startTime, endDay, endTime);
  }

  /**
   * Updates the location details of the specified event.
   * Validates that the event is not null and exists in the schedule before setting
   * its new location.
   *
   * @param event The event to be modified.
   * @param isOnline Specifies whether the event is online or not.
   * @param location The new location of the event if it is not online.
   * @throws IllegalArgumentException if the event is null or does not exist in the schedule.
   */
  public void modifyEvent(Event event, boolean isOnline, String location) {
    this.validateEvent(event);
    this.validateEventExists(event);
    event.setLocation(isOnline, location);
  }

  /**
   * Updates the list of invitees for the specified event.
   * Validates that the event is not null and exists in the schedule before modifying its
   * list of invitees.
   *
   * @param event The event to be modified.
   * @param invitees The new list of invitees for the event.
   * @throws IllegalArgumentException if the event is null, does not exist in the schedule,
   *                                  or if any of the invitees are null.
   */
  public void modifyEvent(Event event, List<User> invitees) {
    this.validateEvent(event);
    this.validateEventExists(event);
    event.setInvitees(invitees);
  }

  /**
   * Removes the specified event from this schedule, ensuring consistency across invitees.
   * It validates that the event is not null and exists in the schedule. If the user is the
   * host, the event is also removed from all invitees' schedules. This maintains consistent
   * state across related users' schedules.
   *
   * @param event The event to be removed. Must not be null and must exist in the schedule.
   * @throws IllegalArgumentException If the event is null or doesn't exist in this schedule.
   */
  public void removeEvent(Event event) {
    this.validateEvent(event);
    this.validateEventExists(event);
    if (event.getHost().getUserId().equals(this.userId)) {
      for (User user : event.getInvitees()) {
        user.removeEvent(event);
      }
    }
    else {
      events.remove(event);
    }
  }

  /**
   * Validates that an event is not null.
   *
   * @param event The event to validate.
   * @throws IllegalArgumentException If the event is null.
   */
  private void validateEvent(Event event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }
  }

  /**
   * Validates that an event exists within the schedule.
   *
   * @param event The event to check for existence.
   * @throws IllegalArgumentException If the event does not exist in the schedule.
   */
  private void validateEventExists(Event event) {
    if (!events.contains(event)) {
      throw new IllegalArgumentException("This event does not exist");
    }
  }
}
