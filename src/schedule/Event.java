package schedule;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import validationutilities.ValidationUtilities;

/**
 * Represents an event within a user's schedule, encapsulating all necessary details such as
 * event name, timing, location, host, and invitees. This class provides functionality to manage
 * these details efficiently, including the ability to check for time overlaps with other events.
 */
public class Event {
  private String name;
  private final Time time;
  private final Location location;
  private String host;
  private final Set<String> invitees;

  /**
   * Constructs a new Event instance with default settings for time and location,
   * initializing with an empty set of invitees.
   */
  public Event() {
    this.time = new Time();
    this.location = new Location();
    this.invitees = new LinkedHashSet<>();
  }

  /**
   * Retrieves the name of the event.
   *
   * @return The name of the event.
   * @throws IllegalStateException If the event name has not been set.
   */
  public String getName() {
    ValidationUtilities.validateGetNull(this.name);
    return name;
  }

  /**
   * Sets the name of the event. The name must not be null or empty.
   *
   * @param name The name to assign to this event.
   * @throws IllegalArgumentException If the name is null or empty.
   */
  public void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }
    this.name = name;
  }

  /**
   * Retrieves the time details associated with this event.
   *
   * @return The {@link Time} object representing the event's timing.
   */
  public Time getTime() {
    return time;
  }


  /**
   * Sets the start and end times for the event, including the days. This is a convenience
   * method to update all time-related fields in one call.
   *
   * @param startDay  The start day of the event.
   * @param startTime The start time of the event.
   * @param endDay    The end day of the event.
   * @param endTime   The end time of the event.
   */
  public void setEventTimes(String startDay, String startTime, String endDay, String endTime) {
    this.time.setStartDay(startDay);
    this.time.setStartTime(startTime);
    this.time.setEndDay(endDay);
    this.time.setEndTime(endTime);
  }

  /**
   * Configures the location details for this event.
   *
   * @param isOnline Indicates whether the event is held online.
   * @param location The physical location of the event if it's not online.
   */
  public void setLocation(boolean isOnline, String location) {
    this.location.setOnline(isOnline);
    this.location.setLocation(location);
  }

  /**
   * Retrieves the location details for this event.
   *
   * @return The {@link Location} object representing the event's location.
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Retrieves a list of invitees to this event.
   *
   * @return A list containing the invitees' IDs.
   */
  public List<String> getInvitees() {
    return new ArrayList<>(invitees);
  }

  /**
   * Updates the list of invitees for this event. The provided list must not be null and must
   * include the host.
   *
   * @param invitees The new list of invitees' IDs.
   * @throws IllegalArgumentException If the list is null, contains null elements,
   *                                  or does not include the host.
   */
  public void setInvitees(List<String> invitees) {
    if (invitees == null || invitees.contains(null)) {
      throw new IllegalArgumentException("Invitees list cannot be null and cannot "
              + "contain null elements");
    }
    if (this.host == null) {
      throw new IllegalStateException("Host is null");
    }
    if (!invitees.contains(this.host.toLowerCase())) {
      throw new IllegalArgumentException("The list of invitees must contain the host of the event");
    }
    for (String string : invitees) {
      this.invitees.add(string.toLowerCase());
    }
  }

  /**
   * Retrieves the host's ID for this event.
   *
   * @return The host's ID.
   * @throws IllegalStateException If the host has not been set.
   */
  public String getHost() {
    ValidationUtilities.validateGetNull(this.host);
    return host;
  }

  /**
   * Sets the host of this event. The host cannot be null.
   *
   * @param host The User object representing the host of the event.
   * @throws IllegalArgumentException if the host parameter is null.
   */
  public void setHost(String host) {
    this.validateUser(host);

    this.host = host.toLowerCase();
  }

  /**
   * Adds a User as an invitee to the event. Validates that the User is not null before adding.
   *
   * @param invitee The user to be added as an invitee to the event.
   * @throws IllegalArgumentException if the invitee is null.
   */
  public void addInvitee(String invitee) {
    this.validateUser(invitee);
    this.invitees.add(invitee.toLowerCase());

  }

  /**
   * Removes a User from the event's list of invitees.
   * Validates that the User is not null before attempting removal.
   *
   * @param invitee The user to be removed from the event's invitees.
   * @throws IllegalArgumentException if the invitee is null.
   */
  public void removeInvitee(String invitee) {
    this.validateUser(invitee);
    this.invitees.remove(invitee.toLowerCase());
  }

  /**
   * Determines if this event overlaps with another event in terms of time.
   * Overlapping is determined based on the start and end times of both events.
   *
   * @param event The event to check for an overlap with.
   * @return true if the events overlap in time, false otherwise.
   */
  public boolean overlap(Event event) {
    return this.time.overlap(event.time);
  }

  /**
   * Checks if this event occurs on a specific day and time.
   * This method is used to find if an event matches a given day and time,
   * useful for querying events in a schedule.
   *
   * @param day  The day to check the event against.
   * @param time The time to check the event against.
   * @return true if the event occurs on the specified day and time, false otherwise.
   */
  public boolean occurs(String day, String time) {
    return this.time.occurs(day, time);
  }

  /**
   * Compares this event with another object for equality. Two events are considered equal
   * if their names, times, locations, lists of invitees, and hosts are all equal.
   * This method allows for null-safe comparisons of event fields.
   *
   * @param object The object to compare this event with.
   * @return true if the provided object represents an event equivalent to this one, false
   * otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Event)) {
      return false;
    }
    Event other = (Event) object;
    return Objects.equals(this.name, other.name)
            && Objects.equals(this.time, other.time)
            && Objects.equals(this.location, other.location)
            && Objects.equals(this.invitees, other.invitees)
            && Objects.equals(this.host, other.host);
  }

  /**
   * Generates a hash code for this event. The hash code is calculated using all the event's
   * fields to ensure consistency with the {@code equals} method.
   *
   * @return A hash code value for this event.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.time, this.location, this.invitees, this.host);
  }

  /**
   * Validates that the given string representing a user is not null or empty. This method
   * is used to ensure that user-related parameters in event operations are valid.
   *
   * @param user The string representing the user to validate.
   * @throws IllegalArgumentException if the user string is null or empty.
   */
  private void validateUser(String user) {
    if (user == null || user.isEmpty()) {
      throw new IllegalArgumentException("User cannot be null or empty.");
    }
  }
}
