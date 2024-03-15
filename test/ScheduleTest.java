import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import schedule.Event;
import schedule.Schedule;

/**
 * A test class for the {@link schedule.Schedule} class.
 * Tests the functionality of the Schedule class,
 * ensuring that it correctly manages a collection of {@link schedule.Event} objects,
 * including their addition, removal, and queries regarding events' presence and overlaps.
 */
public class ScheduleTest {

  private Schedule schedule;
  private Event event;

  /**
   * Initializes the test environment before each test method.
   * Creates a {@link schedule.Schedule} for a specific user and initializes
   * an {@link schedule.Event} with predefined properties for testing.
   */
  @Before
  public void init() {
    schedule = new Schedule("john");
    event = new Event();
    event.setName("something");
    event.setEventTimes("Monday", "1000", "Monday", "1200");
    event.setHost("john");
    event.setInvitees(new ArrayList<>(List.of("john")));
    event.setLocation(true, "somewhere");
  }

  /**
   * Tests the constructors and getters of the {@link schedule.Schedule} class.
   * Verifies that the constructor throws IllegalArgumentException for invalid inputs,
   * and checks that getters return the correct initial values. It also tests the immutability
   * of the events list by attempting to modify it through a getter method.
   */
  @Test
  public void testGettersAndConstructor() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new Schedule(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> new Schedule(""));

    Assert.assertEquals(schedule.getUserId(), "john");
    Assert.assertEquals(schedule.getEvents(), new ArrayList<>());

    // try mutating
    schedule.getEvents().add(event);

    Assert.assertFalse(schedule.hasEvent(event));
  }

  /**
   * Tests various methods of the {@link schedule.Schedule} class,
   * including adding, removing, and checking for events.
   * This test ensures that the Schedule class correctly manages events,
   * prevents the addition of overlapping events, and maintains the integrity
   * of the schedule through operations like adding, removing, and sorting events.
   */
  @Test
  public void testOtherMethods() {
    Assert.assertThrows(IllegalArgumentException.class, () -> schedule.addEvent(null));
    schedule.addEvent(event);
    Assert.assertTrue(schedule.hasEvent(event));

    Event other = new Event();
    other.setName("something");
    other.setEventTimes("Monday", "1030", "Monday", "1100");
    other.setHost("john");
    other.setInvitees(new ArrayList<>(List.of("john")));
    other.setLocation(true, "somewhere");

    Assert.assertTrue(event.overlap(other));
    Assert.assertThrows(IllegalArgumentException.class, () -> schedule.addEvent(other));
    Assert.assertFalse(schedule.hasEvent(other));

    Event something = new Event();
    something.setName("something");
    something.setEventTimes("Monday", "1200", "Monday", "1100");
    something.setHost("john");
    something.setInvitees(new ArrayList<>(List.of("john")));
    something.setLocation(true, "somewhere");
    Assert.assertFalse(event.overlap(something));
    schedule.addEvent(something);
    Assert.assertTrue(schedule.hasEvent(something));

    Assert.assertThrows(IllegalArgumentException.class, () -> schedule.removeEvent(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> schedule.removeEvent(other));
    schedule.removeEvent(something);
    Assert.assertFalse(schedule.hasEvent(something));

    schedule.addEvent(something);

    Event sunday = new Event();
    sunday.setName("something");
    sunday.setEventTimes("Sunday", "1200", "Monday", "1000");
    sunday.setHost("john");
    sunday.setInvitees(new ArrayList<>(List.of("john")));
    sunday.setLocation(true, "somewhere");

    schedule.addEvent(sunday);
    schedule.sortSchedule();
    // test sort schedule
    Assert.assertEquals(schedule.getEvents(),
            new ArrayList<>(Arrays.asList(sunday, event, something)));

  }

}
