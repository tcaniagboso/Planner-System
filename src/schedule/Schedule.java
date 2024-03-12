package schedule;

import java.util.ArrayList;
import java.util.List;

import event.Event;

public class Schedule {
  private final String userId;
  private List<Event> events;
  public Schedule(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
    this.userId = userId;
    this.events = new ArrayList<>();
  }


  public void addEvent(Event event) {
    // TODO: fix the implementation
    if (this.events.contains(event)) {
      //TODO: What happens if an event exists
    }
    else {
      this.events.add(event);
    }
  }

  public void modifyEvent(Event event, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location) {
  }

  public void removeEvent(Event event) {
  }
}
