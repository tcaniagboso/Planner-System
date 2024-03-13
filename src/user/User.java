package user;

import java.util.List;
import java.util.Objects;

import event.Event;
import schedule.Schedule;

/**
 * Represents a user with a unique identifier and a personal schedule.
 * The user can add, modify, and remove events from their schedule, as well as participate in
 * events.
 */
public class User {
  private final String userId; // Represents the ID of the user.
  private final Schedule schedule; // Represents the schedule of the user.

  /**
   * Constructs a new User instance with the specified user ID.
   * Initializes a new, empty schedule for the user.
   *
   * @param userId the unique identifier for the user.
   * @throws IllegalArgumentException if the user ID is null.
   */
  public User(String userId) {
    this.validateUserID(userId);
    this.userId = userId;
    this.schedule = new Schedule(this.userId);
  }

  /**
   * Validates that the provided user ID is not null.
   *
   * @param userId the user ID to validate.
   * @throws IllegalArgumentException if the user ID is null.
   */
  private void validateUserID(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  /**
   * Returns the user's unique identifier.
   *
   * @return the user ID.
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Returns the schedule associated with the user.
   *
   * @return the user's schedule.
   */
  public Schedule getSchedule() {
    return schedule;
  }

  /**
   * Adds the specified event to the user's schedule and marks the user as an invitee to the event.
   *
   * @param event the event to add.
   */
  public void addEvent(Event event) {
    this.schedule.addEvent(event);
    event.addInvitee(this);
  }

  /**
   * Modifies an existing event in the user's schedule with the provided details.
   *
   * @param event     the event to modify.
   * @param name      the new name of the event.
   * @param startDay  the new start day of the event.
   * @param startTime the new start time of the event.
   * @param endDay    the new end day of the event.
   * @param endTime   the new end time of the event.
   * @param isOnline  the new online status of the event.
   * @param location  the new location of the event.
   * @param invitees  the new list of invitees to the event.
   */
  public void modifyEvent(Event event, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<User> invitees) {
    this.schedule.modifyEvent(event, name, startDay, startTime, endDay, endTime, isOnline,
            location, invitees);
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
    this.schedule.modifyEvent(event, name);
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
    this.schedule.modifyEvent(event, startDay, startTime, endDay, endDay);
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
    this.schedule.modifyEvent(event, isOnline, location);
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
    this.schedule.modifyEvent(event, invitees);
  }

  /**
   * Removes the specified event from the user's schedule and unmarks the user as an invitee
   * to the event.
   *
   * @param event the event to remove.
   */
  public void removeEvent(Event event) {
    this.schedule.removeEvent(event);
    event.removeInvitee(this);
  }

  /**
   * Compares this User with the specified object for equality. Two users are considered equal
   * if they have the same userId.
   *
   * @param obj the object to be compared for equality with this User
   * @return true if the specified object is equal to this User
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    User user = (User) obj;
    return Objects.equals(userId, user.userId);
  }

  /**
   * Returns the hash code value for this User. The hash code of a User is computed based on
   * its userId.
   *
   * @return the hash code value for this User
   */
  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

}
