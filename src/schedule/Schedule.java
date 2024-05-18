package schedule;

import java.util.ArrayList;
import java.util.List;

import validationutilities.ValidationUtilities;

/**
 * Represents a schedule associated with a specific user. This class manages a list of events,
 * ensuring there are no overlapping events and providing functionality to add, remove, and
 * query events.
 */
public class Schedule implements ISchedule {
  private final String userId; // The ID of the user owning this schedule
  private final List<ReadOnlyEvent> events; // A list of events in this schedule

  /**
   * Constructs a Schedule instance for a specified user, initializing with an empty list of events.
   *
   * @param userId The unique identifier for the user owning this schedule.
   * @throws IllegalArgumentException If the user ID is null or empty.
   */
  public Schedule(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new IllegalArgumentException("User ID cannot be null ot empty");
    }
    this.userId = userId.trim();
    this.events = new ArrayList<>();
  }

  @Override
  public String getUserName() {
    return userId;
  }

  @Override
  public List<ReadOnlyEvent> getEvents() {
    List<ReadOnlyEvent> newEvents = new ArrayList<>();
    for (ReadOnlyEvent event : events) {
      newEvents.add(event);
    }
    return newEvents;
  }

  @Override
  public void addEvent(ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    this.events.add(event);
  }

  @Override
  public void removeEvent(ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    this.events.remove(event);
  }

  @Override
  public boolean overlap(ReadOnlyEvent newEvent, String firstDayOfWeek) {
    for (ReadOnlyEvent event : events) {
      if (event.overlap(newEvent, firstDayOfWeek)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean hasEvent(ReadOnlyEvent event) {
    return events.contains(event);
  }

  @Override
  public void sortSchedule() {
    events.sort((o1, o2) -> {
      if ((o1.getStartDay().getValue() % 7)
              < (o2.getStartDay().getValue() % 7)) {
        return -1;
      } else if ((o1.getStartDay().getValue() % 7)
              > (o2.getStartDay().getValue() % 7)) {
        return 1;
      } else {
        if (o1.getStartTime() < o2.getStartTime()) {
          return -1;
        }
        if (o1.getStartTime() > o2.getStartTime()) {
          return 1;
        }
      }
      return 0;
    });
  }

  @Override
  public ReadOnlyEvent findEvent(String day, String time, String firstDayOfWeek) {
    for (ReadOnlyEvent event : events) {
      if (event.occurs(day, time, firstDayOfWeek)) {
        return event;
      }
    }
    return null;
  }

  @Override
  public ReadOnlyEvent getEvent(ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    this.validateEventExists(event);
    for (ReadOnlyEvent e: events) {
      if (e.equals(event)) {
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
  private void validateEventExists(ReadOnlyEvent event) {
    if (!this.hasEvent(event)) {
      throw new IllegalArgumentException("This event does not exist in " + userId + " schedule");
    }
  }

}
