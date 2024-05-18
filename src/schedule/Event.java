package schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
public class Event implements IEvent {
  private String name;
  private final ITime time;
  private final ILocation location;
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

  @Override
  public String getName() {
    ValidationUtilities.validateGetNull(this.name);
    return name;
  }

  @Override
  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }
    this.name = name.trim();
  }

  @Override
  public ITime getTime() {
    return time;
  }


  @Override
  public void setEventTimes(String startDay, String startTime, String endDay, String endTime) {
    this.time.setStartDay(startDay);
    this.time.setStartTime(startTime);
    this.time.setEndDay(endDay);
    this.time.setEndTime(endTime);
  }

  @Override
  public void setLocation(boolean isOnline, String location) {
    this.location.setOnline(isOnline);
    this.location.setLocation(location);
  }

  @Override
  public ILocation getEventLocation() {
    return this.location;
  }

  @Override
  public List<String> getInvitees() {
    return new ArrayList<>(invitees);
  }

  @Override
  public void setInvitees(List<String> invitees) {
    if (invitees == null || invitees.contains(null)) {
      throw new IllegalArgumentException("Invitees list cannot be null and cannot "
              + "contain null elements");
    }
    if (this.host == null) {
      throw new IllegalStateException("Host is null");
    }
    if (!invitees.contains(this.host)) {
      throw new IllegalArgumentException("The list of invitees must contain the host of the event");
    }
    this.clearInvitees();
    this.invitees.add(this.host);
    this.invitees.addAll(invitees);
  }

  @Override
  public String getHost() {
    ValidationUtilities.validateGetNull(this.host);
    return host;
  }

  @Override
  public void setHost(String host) {
    this.validateUser(host);

    this.host = host.trim();
  }

  @Override
  public void addInvitee(String invitee) {
    this.validateUser(invitee);
    this.invitees.add(invitee);

  }

  @Override
  public void removeInvitee(String invitee) {
    this.validateUser(invitee);
    this.invitees.remove(invitee);
  }

  @Override
  public void clearInvitees() {
    this.invitees.clear();
  }

  @Override
  public boolean overlap(ReadOnlyEvent event, String firstDayOfWeek) {
    return this.time.overlap(event.getTime(), firstDayOfWeek);
  }

  @Override
  public boolean occurs(String day, String time, String firstDayOfWeek) {
    return this.time.occurs(day, time, firstDayOfWeek);
  }

  @Override
  public boolean wrapsAround(String firstDayOfWeek) {
    return this.time.wrapsAround(firstDayOfWeek);
  }

  @Override
  public DayOfWeek getStartDay() {
    return this.time.getStartDay();
  }

  @Override
  public int getStartTime() {
    return timeToInteger(this.time.getStartTime());
  }

  @Override
  public DayOfWeek getEndDay() {
    return this.time.getEndDay();
  }

  @Override
  public int getEndTime() {
    return timeToInteger(this.time.getEndTime());
  }

  @Override
  public String getLocation() {
    return this.location.getLocation();
  }

  @Override
  public boolean isOnline() {
    return this.location.isOnline();
  }

  /**
   * Compares this event with another object for equality. Two events are considered equal
   * if their names, times, locations, lists of invitees, and hosts are all equal.
   * This method allows for null-safe comparisons of event fields.
   *
   * @param object The object to compare this event with.
   * @return true if the provided object represents an event equivalent to this one, false
   *         otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ReadOnlyEvent)) {
      return false;
    }

    ReadOnlyEvent other = (ReadOnlyEvent) object;
    return Objects.equals(this.name, other.getName())
            && Objects.equals(this.time, other.getTime())
            && Objects.equals(this.location, other.getEventLocation())
            && Objects.equals(this.invitees, new LinkedHashSet<>(other.getInvitees()))
            && Objects.equals(this.host, other.getHost());
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
    if (user == null || user.isBlank()) {
      throw new IllegalArgumentException("User cannot be null or empty.");
    }
  }

  /**
   * Converts a LocalTime object to an integer representation by formatting it to a string without a
   * colon, then converting that string to an integer.
   *
   * @param time The LocalTime object to convert.
   * @return The integer representation of the given LocalTime. For example, 10:15 becomes 1015.
   */
  private int timeToInteger(LocalTime time) {
    String timeString = TimeUtilities.formatTime(time);
    return Integer.parseInt(timeString);
  }
}
