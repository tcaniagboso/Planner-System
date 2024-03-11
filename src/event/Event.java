package event;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import user.User;

/**
 * Represents an event with a specific name, time, location, host, and invitees.
 * This class encapsulates all the necessary details for creating and managing an event.
 */
public class Event {

  private String name;
  private Time time;
  private Location location;
  private User host;
  private List<User> invitees;

  /**
   * Constructs a new Event with default settings.
   * Initializes the time, location, and invitees list.
   */
  public Event() {
    this.time = new Time();
    this.location = new Location();
    this.invitees = new ArrayList<>();
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
   * Sets the name of the event.
   * The name cannot be null or empty, otherwise an IllegalArgumentException is thrown.
   *
   * @param name the name to set for this event
   * @throws IllegalArgumentException if the name is null or empty
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
   * @return the time of the event
   */
  public Time getTime() {
    return time;
  }

  /**
   * Sets the start and end times for the event, including the days.
   * This is a convenience method that sets all time-related fields at once.
   *
   * @param startTime the start time of the event
   * @param startDay the start day of the event
   * @param endTime the end time of the event
   * @param endDay the end day of the event
   */
  public void setEventTimes(String startTime, String startDay, String endTime, String endDay) {
    this.time.setStartTime(startTime);
    this.time.setStartDay(startDay);
    this.time.setEndTime(endTime);
    this.time.setEndDay(endDay);
  }

  public String getStartTime() {
    return this.time.getStartTime();
  }
  public DayOfWeek getStartDay() {
    return this.time.getStartDay();
  }
  public String getEndTime() {
    return this.time.getEndTime();
  }
  public DayOfWeek getEndDay() {
    return this.time.getEndDay();
  }

  /**
   * Sets the location for this event.
   *
   * @param isOnline true if event is online, false otherwise
   * @param location the location where the event will take place
   */
  public void setLocation(boolean isOnline, String location) {
    this.location.setOnline(isOnline);
    this.location.setLocation(location);
  }

  /**
   * Returns the Location object associated with this event.
   *
   * @return the location of the event
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Returns the List of User object associated with this event.
   *
   * @return the list of invitees for this event
   */
  public List<User> getInvitees() {
    return invitees;
  }

  /**
   * Sets the list of invitees for this event. The invitees list cannot be null and
   * cannot contain null elements. If either of these conditions is not met,
   * an IllegalArgumentException is thrown. This ensures that the event has a valid
   * list of invitees.
   *
   * @param invitees the list of User objects representing the users invited to the event.
   * @throws IllegalArgumentException if the invitees list is null or contains null elements.
   */
  public void setInvitees(List<User> invitees) {
    if (invitees == null || invitees.contains(null)) {
      throw new IllegalArgumentException("Invitees list cannot be null and cannot "
              + "contain null elements");
    }
    this.invitees = new ArrayList<>(invitees);
  }

  /**
   * Returns the User object representing the host of this event. The host is the
   * user responsible for creating or managing the event.
   *
   * @return the User object representing the host of the event.
   */
  public User getHost() {
    return host;
  }

  /**
   * Sets the host of this event. The host cannot be null; if a null host is provided,
   * an IllegalArgumentException is thrown. This ensures that the event always has a valid host.
   *
   * @param host the User object representing the host of the event.
   * @throws IllegalArgumentException if the host parameter is null.
   */
  public void setHost(User host) {
    if (host == null) {
      throw new IllegalArgumentException("Host cannot be null");
    }

    this.host = host;
  }
}
