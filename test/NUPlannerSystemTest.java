import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import plannersystem.NUPlannerSystem;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;
import schedule.Time;

/**
 * Test class for {@link NUPlannerSystem}. It includes tests for reading, saving, displaying,
 * creating, modifying, and removing user schedules and events. These tests ensure that the
 * planner system functions correctly across various use cases, including handling scheduling
 * conflicts, updating event details, and managing event invitees.
 */
public class NUPlannerSystemTest {

  private PlannerSystem system;

  /**
   * Initializes a new instance of {@link NUPlannerSystem} before each test.
   */
  @Before
  public void init() {
    system = new NUPlannerSystem();
  }

  /**
   * Tests the ability of the system to read and load a user's schedule from an XML file.
   * This test verifies that the schedule is correctly parsed and that the events match
   * the expected values.
   */
  @Test
  public void testReadUserSchedule() {
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);
    Schedule schedule = system.getSchedule("Prof. Lucia");

    Assert.assertEquals("prof. lucia", schedule.getUserId());

    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    Event event2 = new Event();
    event2.setName("CS3500 Afternoon Lecture");
    event2.setEventTimes("Tuesday", "1335", "Tuesday", "1515");
    event2.setLocation(false, "Churchill Hall 101");
    event2.setHost("prof. lucia");
    event2.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "chat")));

    Event event3 = new Event();
    event3.setName("Sleep");
    event3.setEventTimes("Friday", "1800", "Sunday", "1200");
    event3.setLocation(true, "Home");
    event3.setHost("prof. lucia");
    event3.setInvitees(new ArrayList<>(List.of("prof. lucia")));

    List<Event> events = new ArrayList<>(Arrays.asList(event, event2, event3));
    Assert.assertEquals(schedule.getEvents(), events);

    File xmlFile2 = new File("prof2.xml");

    // test reading xml file with overlapping events
    Assert.assertThrows(IllegalArgumentException.class, () -> system.readUserSchedule(xmlFile2));

    Assert.assertEquals(schedule.getEvents(), events);

    // Convert URL to File
    File xmlFile3 = new File("chat.xml");
    system.readUserSchedule(xmlFile3);

    Event event4 = new Event();
    event4.setName("CS3500 Morning Lecture");
    event4.setEventTimes("Monday", "0950", "Monday", "1130");
    event4.setLocation(false, "Churchill Hall 101");
    event4.setHost("prof. lucia");
    event4.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    Event event5 = new Event();
    event5.setName("CS3500 Afternoon Lecture");
    event5.setEventTimes("Monday", "1335", "Monday", "1515");
    event5.setLocation(false, "Churchill Hall 101");
    event5.setHost("prof. lucia");
    event5.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "chat")));

    events.add(event4);
    events.add(event5);

    // tests updating prof's schedule using another user's xml file
    Assert.assertEquals(schedule.getEvents(), events);
  }

  /**
   * Tests the system's ability to save a user's schedule to an XML file. This test ensures
   * that schedules are correctly serialized and can be reloaded to match the original schedule.
   */
  @Test
  public void testSaveSchedule() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.saveUserSchedule("prof. lucia"));
    // Convert URL to File
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    this.system.saveUserSchedule("prof. lucia");
    File file = new File("prof. lucia.xml");

    PlannerSystem other = new NUPlannerSystem();
    other.readUserSchedule(file);
    Schedule schedule = other.getSchedule("Prof. Lucia");

    Assert.assertEquals("prof. lucia", schedule.getUserId());

    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    Event event2 = new Event();
    event2.setName("CS3500 Afternoon Lecture");
    event2.setEventTimes("Tuesday", "1335", "Tuesday", "1515");
    event2.setLocation(false, "Churchill Hall 101");
    event2.setHost("prof. lucia");
    event2.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "chat")));

    Event event3 = new Event();
    event3.setName("Sleep");
    event3.setEventTimes("Friday", "1800", "Sunday", "1200");
    event3.setLocation(true, "Home");
    event3.setHost("prof. lucia");
    event3.setInvitees(new ArrayList<>(List.of("prof. lucia")));

    List<Event> events = new ArrayList<>(Arrays.asList(event, event2, event3));
    Assert.assertEquals(schedule.getEvents(), events);
  }

  /**
   * Tests displaying a user's schedule as a formatted string. This test verifies that the
   * schedule view correctly represents the schedule's events and their details.
   */
  @Test
  public void testDisplay() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.displayUserSchedule("prof. lucia"));

    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Assert.assertEquals("Sunday:" + System.lineSeparator()
            + "Monday:" + System.lineSeparator()
            + "Tuesday:" + System.lineSeparator()
            + "        name: CS3500 Morning Lecture" + System.lineSeparator()
            + "        time: Tuesday: 09:50 -> Tuesday: 11:30" + System.lineSeparator()
            + "        location: Churchill Hall 101" + System.lineSeparator()
            + "        online: false" + System.lineSeparator()
            + "        invitees: prof. lucia" + System.lineSeparator()
            + "                  student anon" + System.lineSeparator()
            + "                  chat" + System.lineSeparator()
            + "        name: CS3500 Afternoon Lecture" + System.lineSeparator()
            + "        time: Tuesday: 13:35 -> Tuesday: 15:15" + System.lineSeparator()
            + "        location: Churchill Hall 101" + System.lineSeparator()
            + "        online: false" + System.lineSeparator()
            + "        invitees: prof. lucia" + System.lineSeparator()
            + "                  chat" + System.lineSeparator()
            + "Wednesday:" + System.lineSeparator()
            + "Thursday:" + System.lineSeparator()
            + "Friday:" + System.lineSeparator()
            + "        name: Sleep" + System.lineSeparator()
            + "        time: Friday: 18:00 -> Sunday: 12:00" + System.lineSeparator()
            + "        location: Home" + System.lineSeparator()
            + "        online: true" + System.lineSeparator()
            + "        invitees: prof. lucia" + System.lineSeparator()
            + "Saturday:", system.displayUserSchedule("prof. lucia"));
  }

  /**
   * Tests the creation of new events and their addition to user schedules, including handling
   * of scheduling conflicts and invitee management.
   */
  @Test
  public void testCreateEvent() {

    // create an event without a xml file
    system.createEvent("Student Anon", "OH", "Sunday", "0950",
            "Sunday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    Event event1 = new Event();
    event1.setName("OH");
    event1.setEventTimes("Sunday", "0950", "Sunday", "1030");
    event1.setLocation(false, "ChurchHill Hall 101");
    event1.setHost("Student Anon");
    event1.setInvitees(new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    Schedule anonSchedule = system.getSchedule("Student Anon");
    Assert.assertTrue(anonSchedule.hasEvent(event1));

    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    // create an event the invitees can't attend
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.createEvent("Student Anon", "OH", "Tuesday",
                    "1335", "Tuesday", "1500", false,
                    "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat"))));

    // create an event the user cannot attend
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.createEvent("Student Anon", "OH", "Tuesday",
                    "0950", "Tuesday", "1030", false,
                    "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat"))));

    system.createEvent("Student Anon", "OH", "Monday", "0950",
            "Monday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));
    Event event = new Event();
    event.setName("OH");
    event.setEventTimes("Monday", "0950", "Monday", "1030");
    event.setLocation(false, "ChurchHill Hall 101");
    event.setHost("Student Anon");
    event.setInvitees(new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    Assert.assertTrue(anonSchedule.hasEvent(event));
  }

  /**
   * Tests modifying an existing event within a user's schedule. This includes changes to
   * event times, locations, and invitees, with checks for scheduling conflicts.
   */
  @Test
  public void testModifyEvent() {
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", null, "OH",
                    "Tuesday", "1335", "Tuesday", "1500",
                    false, "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat"))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "OH",
                    "Tuesday", "1335", "Tuesday", "1500",
                    false, "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat"))));

    system.createEvent("Student Anon", "OH", "Sunday", "0950",
            "Sunday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "OH",
                    "Tuesday", "1335", "Tuesday", "1500",
                    false, "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat"))));

    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);
    system.modifyEvent("student anon", event, "OH",
            "Tuesday", "1000", "Tuesday", "1200",
            false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    Time time = new Time("Tuesday", "1000", "Tuesday", "1200");
    Assert.assertEquals(event.getTime(), time);
  }

  /**
   * Tests specifically modifying the name of an event. This test ensures that event names
   * can be changed without affecting other event properties.
   */
  @Test
  public void modifyEventName() {
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    // tests modifying a null event
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("student anon", null, "some"));

    // tests modifying when the user doesn't exist
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("student anon", event, "some"));

    // adding the user to the system by creating a valid event
    system.createEvent("Student Anon", "OH", "Sunday", "0950",
            "Sunday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    // test modifying when the user exists but doesn't have the event
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "some"));

    // adding the event and more users to the system
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    // tests modifying with empty string
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, ""));

    //test modifying when everything is valid
    system.modifyEvent("Student Anon", event, "some");
    Assert.assertEquals("some", event.getName());
  }

  /**
   * Tests specifically modifying the time of an event. This includes verifying that changes
   * do not create scheduling conflicts with existing events.
   */
  @Test
  public void modifyEventTime() {
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    // tests modifying a null event
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("student anon", null, "Tuesday",
                    "1000", "Tuesday", "1130"));

    // tests modifying when the user doesn't exist
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("student anon", event, "Tuesday",
                    "1000", "Tuesday", "1130"));

    // adding the user to the system by creating a valid event
    system.createEvent("Student Anon", "OH", "Sunday", "0950",
            "Sunday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("student anon", "prof. lucia", "chat")));

    // test modifying when the user exists but doesn't have the event
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "Tuesday",
                    "1000", "Tuesday", "1130"));

    // adding the event and more users to the system
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    //test modifying when everything is valid
    system.modifyEvent("Student Anon", event, "Tuesday",
            "1000", "Tuesday", "1130");

    Time time = new Time("Tuesday", "1000", "Tuesday", "1930");
    Assert.assertEquals(event.getTime(), time);
  }

  /**
   * Tests modifying the location and online status of an event. This test verifies that
   * such changes are applied correctly and reflected in the schedule.
   */
  @Test
  public void testModifyEventLocation() {
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("student anon", event, true, null));

    system.modifyEvent("student anon", event, true, "Ryder");

    event.setLocation(true, "Ryder");

    Schedule anonSchedule = system.getSchedule("Student Anon");
    Assert.assertTrue(anonSchedule.hasEvent(event));
  }

  /**
   * Tests removing an event from a user's schedule and ensuring it is also removed from
   * the schedules of all its invitees.
   */
  @Test
  public void testRemoveEvent() {
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("prof. lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("prof. lucia", "student anon", "chat")));

    system.removeEvent("prof. lucia", event);

    Schedule anonSchedule = system.getSchedule("Student Anon");
    Schedule prof = system.getSchedule("Prof. Lucia");
    Schedule chat = system.getSchedule("chat");

    Assert.assertFalse(anonSchedule.hasEvent(event));
    Assert.assertFalse(prof.hasEvent(event));
    Assert.assertFalse(chat.hasEvent(event));
  }

  /**
   * Tests the functionality to display details of a specific event based on a given time and day.
   * This includes checking for events that exist at the specified time as well as handling
   * cases where no event is scheduled.
   */
  @Test
  public void testShowEvent() {
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Assert.assertEquals("CS3500 Morning Lecture happens at this time",
            system.showEvent("prof. lucia", "tuesday", "1000"));
    Assert.assertEquals("No event exists at this time",
            system.showEvent("student anon", "tuesday", "1130"));
  }
}
