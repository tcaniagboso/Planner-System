package schedule;

import java.util.ArrayList;
import java.util.List;

import validationutilities.ValidationUtilities;

/**
 * Represents a schedule associated with a specific user. This class manages a list of events,
 * ensuring there are no overlapping events and providing functionality to add, remove, and
 * query events.
 */
public class Schedule {
  private final String userId; // The ID of the user owning this schedule
  private final List<Event> events; // A list of events in this schedule

  /**
   * Constructs a Schedule instance for a specified user, initializing with an empty list of events.
   *
   * @param userId The unique identifier for the user owning this schedule.
   * @throws IllegalArgumentException If the user ID is null or empty.
   */
  public Schedule(String userId) {
    if (userId == null || userId.isEmpty()) {
      throw new IllegalArgumentException("User ID cannot be null ot empty");
    }
    this.userId = userId;
    this.events = new ArrayList<>();
  }

  /**
   * Gets the user ID associated with this schedule.
   *
   * @return The user ID.
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Retrieves a copy of the list of events in this schedule.
   *
   * @return A new list containing all the events.
   */
  public List<Event> getEvents() {
    List<Event> newEvents = new ArrayList<>();
    for (Event event : events) {
      newEvents.add(event);
    }
    return newEvents;
  }

  /**
   * Adds a new event to this schedule if there's no time overlap with existing events.
   *
   * @param event The event to be added.
   * @throws IllegalArgumentException If the event is null or if an overlap with an existing event
   *                                  is detected.
   */
  public void addEvent(Event event) {
    ValidationUtilities.validateNull(event);
    if (this.overlap(event)) {
      throw new IllegalArgumentException("Time conflict");
    }
    this.events.add(event);
  }

  /**
   * Attempts to find and return a specific event in the schedule.
   *
   * @param event The event to find.
   * @return The event if found, otherwise null.
   */
  public Event getEvent(Event event) {
    for (Event ev : events) {
      if (ev.equals(event)) {
        return event;
      }
    }
    return null;
  }

  /**
   * Removes a specified event from the schedule after validating its presence.
   *
   * @param event The event to remove.
   * @throws IllegalArgumentException If the event is null or does not exist in the schedule.
   */
  public void removeEvent(Event event) {
    ValidationUtilities.validateNull(event);
    this.validateEventExists(event);
    this.events.remove(event);
  }

  /**
   * Checks for time overlaps between the new event and any event already in the schedule.
   *
   * @param newEvent The new event being checked for overlap.
   * @return True if an overlap is detected, otherwise false.
   */
  public boolean overlap(Event newEvent) {
    for (Event event : events) {
      if (event.overlap(newEvent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether a specific event is present in this schedule.
   *
   * @param event The event to check.
   * @return True if the event exists in the schedule, otherwise false.
   */
  public boolean hasEvent(Event event) {
    return events.contains(event);
  }

  /**
   * Sorts the schedule's events first by day of the week and then by start time.
   */
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

  /**
   * Finds an event occurring at a specified day and time.
   *
   * @param day  The day of the event.
   * @param time The time of the event.
   * @return The event if found, otherwise null.
   */
  public Event findEvent(String day, String time) {
    for (Event event : events) {
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
