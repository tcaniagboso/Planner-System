package user;

import java.util.Objects;

import event.Event;
import schedule.Schedule;

public class User {
  private final String userId; // represents the ID of the user
  private Schedule schedule; // represents the schedule of the user

  public User(String userId) {
    this.validateUserID(userId);
    this.userId = userId;
    this.schedule = new Schedule(this.userId);


  }

  // to validate the user ID
  private void validateUserID(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  public String getUserId() {
    return userId;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void addEvent(Event event) {
    this.schedule.addEvent(event);
  }

  public void modifyEvent(Event event) {
    this.schedule.modifyEvent(event);
  }

  public void removeEvent(Event event) {
    this.schedule.removeEvent(event);
  }
}
