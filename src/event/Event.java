package event;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import user.User;

public class Event {

  private String name;
  private Time time;
  private Location location;
  private User host;
  private List<User> invitees;

  public Event() {
    this.time = new Time();
    this.location = new Location();
    this.invitees = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) throws IllegalArgumentException{
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }
    this.name = name;
  }

  public Time getTime() {
    return time;
  }

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

  public void setLocation(String location) {
    this.location.setLocation(location);
  }

  public void setOnline(boolean isOnline) {
    this.location.setOnline(isOnline);
  }

  public String getLocation() {
    return this.location.getLocation();
  }

  public List<User> getInvitees() {
    return invitees;
  }

  public void setInvitees(List<User> invitees) {
    if (invitees == null || invitees.contains(null)) {
      throw new IllegalArgumentException("Invitees list cannot be null and cannot " +
              "contain null elements");
    }
    this.invitees = new ArrayList<>(invitees);
  }

  public User getHost() {
    return host;
  }

  public void setHost(User host) {
    if (host == null) {
      throw new IllegalArgumentException("Host cannot be null");
    }

    this.host = host;
  }
}
