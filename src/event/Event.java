package event;

import java.time.DayOfWeek;
import java.util.List;

import user.User;

public class Event {

  private String name;
  private Time time;
  private Location location;
  private User host;
  private List<User> invitees;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Time getTime() {
    return time;
  }

  public void setStartTime(String startTime) {
    this.time.setStartTime(startTime);
  }
  public void setStartDay(String startDay) {
    this.time.setStartDay(startDay);
  }
  public void setEndTime(String endTime) {
    this.time.setEndTime(endTime);
  }
  public void setEndDay(String endDay) {
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

  public boolean getOnline() {
    return this.location.getOnline();
  }


  public List<User> getInvitees() {
    return invitees;
  }

  public void setInvitees(List<User> invitees) {
    this.invitees = invitees;
  }

  public User getHost() {
    return host;
  }

  public void setHost(User host) {
    this.host = host;
  }
}
