package provider.dto;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to encapsulate event data for transfer between the view and controller layers.
 */
public class EventData {
  private final String name;
  private final String location;
  private final boolean isOnline;
  private final DayOfWeek startDay;
  private final DayOfWeek endDay;
  private final int startTime;
  private final int endTime;
  private final String host;
  private final List<String> invitees;

  /**
   * Constructor for the event data being transferred from one spot to the other.
   *
   * @param name      name of event
   * @param location  location of event
   * @param isOnline  true if online, false otherwise
   * @param startDay  the day the event starts
   * @param endDay    the day the event ends
   * @param startTime the time the event starts
   * @param endTime   the time the event ends
   * @param host      user hosting the event
   * @param invitees  users invited/attending the event
   */
  public EventData(String name, String location, boolean isOnline,
                   DayOfWeek startDay, DayOfWeek endDay, int startTime,
                   int endTime, String host, List<String> invitees) {
    this.name = name;
    this.location = location;
    this.isOnline = isOnline;
    this.startDay = startDay;
    this.endDay = endDay;
    this.startTime = startTime;
    this.endTime = endTime;
    this.host = host;
    this.invitees = new ArrayList<>(invitees);
  }

  /**
   * Returns the name of the event.
   *
   * @return name of the event
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the location of the event.
   *
   * @return location of the event
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * Returns the online status of the event.
   *
   * @return true if online, false otherwise
   */
  public boolean isOnline() {
    return this.isOnline;
  }

  /**
   * Returns the start time of the event.
   *
   * @return start time of the event
   */
  public int getStartTime() {
    return this.startTime;
  }

  /**
   * Returns the start day of the event.
   *
   * @return start day of the event
   */
  public DayOfWeek getStartDay() {
    return this.startDay;
  }

  /**
   * Returns the end time of the event.
   *
   * @return end time of the event
   */
  public int getEndTime() {
    return this.endTime;
  }

  /**
   * Returns the end day of the event.
   *
   * @return end day of the event
   */
  public DayOfWeek getEndDay() {
    return this.endDay;
  }

  /**
   * Returns the host of the event.
   *
   * @return name of the event
   */
  public String getHost() {
    return this.host;
  }

  /**
   * Returns a list of invitees of the event.
   *
   * @return a list of invitees of the event
   */
  public List<String> getInvitees() {
    return this.invitees;
  }

}
