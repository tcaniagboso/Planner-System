package event;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import user.User;

/**
 * Represents an event, encapsulating details such as the event's name, timing, location,
 * host, and a list of invitees.
 * This class provides mechanisms to manage event details including adding and
 * removing invitees and checking for time overlaps with other events.
 */
public class Event {

  private String name;
  private Time time;
  private Location location;
  private User host;
  private Set<User> invitees;

  /**
   * Initializes a new Event instance with default Time and Location settings,
   * and an empty set of invitees.
   */
  public Event() {
    this.time = new Time();
    this.location = new Location();
    this.invitees = new HashSet<>();
  }

  /**
   * Returns the name of the event.
   *
   * @return the name of the event
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the event. The name cannot be null or empty.
   *
   * @param name The name to set for this event.
   * @throws IllegalArgumentException if the name is null or empty.
   */
  public void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }
    this.name = name;
  }

  /**
   * Returns the Time object associated with this event.
   *
   * @return The time of the event.
   */
  public Time getTime() {
    return time;
  }

  /**
   * Sets the start and end times for the event, including the days. This method
   * is a convenience method that sets all time-related fields at once.
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
   * Sets the location for this event.
   *
   * @param isOnline True if the event is online, false otherwise.
   * @param location The location where the event will take place.
   */
  public void setLocation(boolean isOnline, String location) {
    this.location.setOnline(isOnline);
    this.location.setLocation(location);
  }

  /**
   * Returns the Location object associated with this event.
   *
   * @return The location of the event.
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Returns the set of Users associated with this event as invitees.
   *
   * @return The set of invitees for this event.
   */
  public Set<User> getInvitees() {
    return invitees;
  }

  /**
   * Sets the list of invitees for this event. The invitees list cannot be null and
   * cannot contain null elements.
   *
   * @param invitees The list of Users representing the users invited to the event.
   * @throws IllegalArgumentException if the invitees list is null or contains null elements.
   */
  public void setInvitees(List<User> invitees) {
    if (invitees == null || invitees.contains(null)) {
      throw new IllegalArgumentException("Invitees list cannot be null and cannot "
              + "contain null elements");
    }
    for (User invited: invitees) {
      invited.addEvent(this);
    }
  }

  /**
   * Returns the User object representing the host of this event. The host is the
   * user responsible for creating or managing the event.
   *
   * @return The User object representing the host of the event.
   */
  public User getHost() {
    return host;
  }

  /**
   * Sets the host of this event. The host cannot be null.
   *
   * @param host The User object representing the host of the event.
   * @throws IllegalArgumentException if the host parameter is null.
   */
  public void setHost(User host) {
    this.validateUser(host);

    this.host = host;
  }

  /**
   * Adds a User as an invitee to the event. Validates that the User is not null before adding.
   *
   * @param invitee The user to be added as an invitee to the event.
   * @throws IllegalArgumentException if the invitee is null.
   */
  public void addInvitee(User invitee) {
    this.validateUser(invitee);
    this.invitees.add(invitee);

  }

  /**
   * Removes a User from the event's list of invitees.
   * Validates that the User is not null before attempting removal.
   *
   * @param invitee The user to be removed from the event's invitees.
   * @throws IllegalArgumentException if the invitee is null.
   */
  public void removeInvitee(User invitee) {
    this.validateUser(invitee);
    this.invitees.remove(invitee);
  }

  /**
   * Validates that a User object is not null.
   *
   * @param user The user to validate.
   * @throws IllegalArgumentException if the user is null.
   */
  private void validateUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
  }

  /**
   * Checks if this event overlaps in time with another event. Two events overlap if they
   * have the same time or if their time ranges intersect.
   *
   * @param event The event to check for overlap with.
   * @return True if the events overlap in time, false otherwise.
   */
  public boolean overlap(Event event) {
    return this.equals(event) || this.time.overlap(event.time);
  }

  /**
   * Compares this event with the specified object for equality. Two events are considered equal
   * if all their respective fields (name, time, location, invitees, and host) are equal. This
   * method uses {@link Objects#equals(Object, Object)} for field comparisons to handle nulls
   * gracefully.
   *
   * @param object the object to be compared for equality with this event.
   * @return true if the specified object is equal to this event based on the criteria defined
   * above.
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
    return Objects.equals(this.name, other.getName())
            && Objects.equals(this.time, other.getTime())
            && Objects.equals(this.location, other.getLocation())
            && Objects.equals(this.invitees, other.getInvitees())
            && Objects.equals(this.host, other.getHost());
  }

  /**
   * Returns a hash code value for this event. The hash code is generated by combining the hash
   * codes of all the event's fields using {@link Objects#hash(Object...)} method. This ensures that
   * {@code hashCode} is consistent with {@code equals} as per the contract specified in
   * {@link Object#hashCode()}.
   *
   * @return a hash code value for this event.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.time, this.location, this.invitees, this.host);
  }

}
