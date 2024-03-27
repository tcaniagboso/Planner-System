import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import schedule.Event;
import schedule.Location;
import schedule.Time;

/**
 * A test class for the {@link schedule.Event} class.
 * This class tests cases that verify the functionality of the Event class, including its getters,
 * setters, and other methods.
 * It ensures that Event objects are created and managed correctly,
 * with valid properties, and that they behave as expected under various scenarios.
 */
public class EventTest {
  private Event event;

  /**
   * Sets up the test environment before each test method is executed.
   * Initializes a new {@link schedule.Event} object for testing.
   */
  @Before
  public void init() {
    event = new Event();
  }

  /**
   * Tests the getter and setter methods of the {@link schedule.Event} class.
   * It verifies that the methods throw IllegalArgumentException for invalid inputs,
   * and that they correctly assign and retrieve values for the event's properties.
   * It also checks the proper functionality of setting and getting complex objects
   * like {@link schedule.Time} and {@link schedule.Location}.
   */
  @Test
  public void testGettersSetters() {
    Assert.assertThrows(IllegalStateException.class, () -> event.getName());
    Assert.assertThrows(IllegalStateException.class, () -> event.getHost());

    Assert.assertThrows(IllegalArgumentException.class, () -> event.setName(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> event.setName(""));
    event.setName("OOD");
    Assert.assertEquals("OOD", event.getName());

    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setEventTimes("monday", "1200",
                    "monday", "1200"));

    event.setEventTimes("monday", "1200",
            "monday", "1230");
    Assert.assertEquals(new Time("monday", "1200",
            "monday", "1230"), event.getTime());

    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setLocation(true, null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setLocation(true, ""));
    event.setLocation(true, "Ryder");

    Location location = new Location();
    location.setOnline(true);
    location.setLocation("Ryder");
    Assert.assertEquals(location, event.getLocation());

    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setHost(null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setHost(""));

    event.setHost("Jack");
    Assert.assertEquals("Jack", event.getHost());

    Assert.assertThrows(IllegalStateException.class,
        () -> new Event().setInvitees(new ArrayList<>()));
    Assert.assertThrows(IllegalArgumentException.class, () -> event.setInvitees(null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> event.setInvitees(new ArrayList<>(Arrays.asList(null, null))));
    List<String> invitees = new ArrayList<>(Arrays.asList("John", "James", "Peter"));

    Assert.assertThrows(IllegalArgumentException.class, () -> event.setInvitees(invitees));

    invitees.add("Jack");
    event.setInvitees(invitees);
    Assert.assertTrue(event.getInvitees().contains("Jack"));
    Assert.assertTrue(event.getInvitees().contains("John"));
    Assert.assertTrue(event.getInvitees().contains("James"));
    Assert.assertTrue(event.getInvitees().contains("Peter"));

    // try mutating the invitees list
    event.getInvitees().add("Ron");
    Assert.assertFalse(event.getInvitees().contains("Ron"));
  }


  /**
   * Tests other methods of the {@link schedule.Event} class,
   * including the ability to add and remove invitees, check for event overlap,
   * and clone events. It also tests the new equals method It verifies that the methods
   * behave correctly under various scenarios, including handling of invalid inputs. It also tests
   * the wrapAround method.
   */
  @Test
  public void testOtherMethods() {
    event.setName("Reading week");
    event.setEventTimes("Monday", "1100", "Monday", "1000");
    event.setLocation(true, "Ryder");
    event.setHost("John");
    event.setInvitees(new ArrayList<>(Arrays.asList("John", "James", "Jack")));

    Assert.assertTrue(event.wrapsAround());

    Event other = new Event();
    other.setName("Reading");
    other.setEventTimes("Sunday", "1100", "Monday", "1100");
    other.setLocation(true, "Ryder");
    other.setHost("John");
    other.setInvitees(new ArrayList<>(Arrays.asList("John", "James", "Jack")));
    Assert.assertFalse(other.wrapsAround());

    Assert.assertFalse(event.overlap(other));
    other.setEventTimes("Tuesday", "1100", "Monday", "1100");
    Assert.assertTrue(event.overlap(other));

    Assert.assertThrows(IllegalArgumentException.class, () -> event.removeInvitee(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> event.removeInvitee(""));

    event.removeInvitee("James");
    Assert.assertFalse(event.getInvitees().contains("James"));

    Assert.assertThrows(IllegalArgumentException.class, () -> event.addInvitee(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> event.addInvitee(""));

    event.addInvitee("James");
    Assert.assertTrue(event.getInvitees().contains("James"));

    Assert.assertEquals(event, event);
    Assert.assertNotEquals(event, other);
  }
}
