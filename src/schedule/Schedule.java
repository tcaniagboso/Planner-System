package schedule;

import java.util.ArrayList;
import java.util.List;

import validationutilities.ValidationUtilities;


/**
 * Represents a schedule for a user, containing a list of events.
 * Each schedule is associated with a specific user ID and can add, modify, or remove events,
 * ensuring no overlapping events exist within the schedule.
 */
public class Schedule {
  private final String userId; // The ID of the user owning this schedule
  private final List<Event> events; // A list of events in this schedule

  /**
   * Constructs a new Schedule for the specified user.
   * Initializes an empty list of events.
   *
   * @param userId The unique identifier for the user owning this schedule.
   * @throws IllegalArgumentException If the user ID is null.
   */
  public Schedule(String userId) {
    if (userId == null || userId.isEmpty()) {
      throw new IllegalArgumentException("User ID cannot be null ot empty");
    }
    this.userId = userId;
    this.events = new ArrayList<>();
  }

  public String getUserId() {
    return userId;
  }

  public List<Event> getEvents() {
    List<Event> newEvents = new ArrayList<>();
    for (Event event : events) {
      newEvents.add(event);
    }
    return newEvents;
  }

  /**
   * Adds an event to the schedule after checking for any time overlap with existing events.
   *
   * @param event The event to add to the schedule.
   * @throws IllegalArgumentException If the event is null or overlaps
   *                                  with an existing event in the schedule.
   */
  public void addEvent(Event event) {
    ValidationUtilities.validateNull(event);
    if (this.overlap(event)) {
      throw new IllegalArgumentException("Time conflict");
    }
    this.events.add(event);
  }

  /**
   * Removes the specified event from this schedule, ensuring consistency across invitees.
   * It validates that the event is not null and exists in the schedule. If the user is the
   * host, the event is also removed from all invitees' schedules. This maintains consistent
   * state across related users' schedules.
   *
   * @param event The event to be removed. Must not be null and must exist in the schedule.
   * @throws IllegalArgumentException If the event is null or doesn't exist in this schedule.
   */
  public void removeEvent(Event event) {
    ValidationUtilities.validateNull(event);
    this.validateEventExists(event);
    this.events.remove(event);
  }

  public boolean overlap(Event newEvent) {
    for (Event event: events) {
      if (event.overlap(newEvent)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasEvent(Event event) {
    return events.contains(event);
  }

  public void sortSchedule() {
    events.sort((o1, o2) -> {
      if ((o1.getTime().getStartDay().getValue() % 7)
              < (o2.getTime().getStartDay().getValue() % 7)) {
        return -1;
      } else if ((o1.getTime().getStartDay().getValue() % 7)
              > (o2.getTime().getStartDay().getValue() % 7)) {
        return 1;
      } else {
        if (o1.getTime().getStartTime().isBefore(o2.getTime().getStartTime())) {
          return -1;
        }
        if (o1.getTime().getStartTime().isAfter(o2.getTime().getStartTime())) {
          return 1;
        }
      }
      return 0;
    });
  }

  public Event findEvent(String day, String time) {
    for (Event event: events) {
      if (event.occurs(day, time)) {
        return event;
      }
    }
    return null;
  }

  /**
   * Validates that an event exists within the schedule.
   *
   * @param event The event to check for existence.
   * @throws IllegalArgumentException If the event does not exist in the schedule.
   */
  private void validateEventExists(Event event) {
    if (!events.contains(event)) {
      throw new IllegalArgumentException("This event does not exist");
    }
  }

}
