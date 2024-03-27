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
   * Initializes testing environment before each test case. This method sets up a new instance
   * of {@link NUPlannerSystem}, ensuring a clean state for every test.
   */
  @Before
  public void init() {
    system = new NUPlannerSystem();
  }

  /**
   * Tests the system's ability to correctly parse and load events from an XML file into
   * a user's schedule. Verifies that the loaded events match expected values, including
   * checking for correct parsing of event details and handling of multiple users' schedules.
   * Additionally, tests the system's response to XML files with overlapping events,
   * expecting an {@link IllegalArgumentException}.
   */
  @Test
  public void testReadUserSchedule() {
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);
    Schedule schedule = system.getSchedule("Prof. Lucia");
    Schedule anonSchedule = system.getSchedule("Student Anon");
    Schedule chatSchedule = system.getSchedule("Chat");

    Assert.assertEquals("Prof. Lucia", schedule.getUserId());

    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("Prof. Lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Event event2 = new Event();
    event2.setName("CS3500 Afternoon Lecture");
    event2.setEventTimes("Tuesday", "1335", "Tuesday", "1515");
    event2.setLocation(false, "Churchill Hall 101");
    event2.setHost("Prof. Lucia");
    event2.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Chat")));

    Event event3 = new Event();
    event3.setName("Sleep");
    event3.setEventTimes("Friday", "1800", "Sunday", "1200");
    event3.setLocation(true, "Home");
    event3.setHost("Prof. Lucia");
    event3.setInvitees(new ArrayList<>(List.of("Prof. Lucia")));

    List<Event> events = new ArrayList<>(Arrays.asList(event, event2, event3));
    Assert.assertEquals(schedule.getEvents(), events);
    Assert.assertTrue(anonSchedule.hasEvent(event));
    Assert.assertFalse(anonSchedule.hasEvent(event2));
    Assert.assertTrue(chatSchedule.hasEvent(event2));

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
    event4.setHost("Prof. Lucia");
    event4.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Event event5 = new Event();
    event5.setName("CS3500 Afternoon Lecture");
    event5.setEventTimes("Monday", "1335", "Monday", "1515");
    event5.setLocation(false, "Churchill Hall 101");
    event5.setHost("Prof. Lucia");
    event5.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Chat")));

    events.add(event4);
    events.add(event5);

    // tests updating prof's schedule using another user's xml file
    Assert.assertEquals(schedule.getEvents(), events);
  }

  /**
   * Ensures the system can accurately save a user's schedule to an XML file and that
   * this file correctly reflects the schedule's state. Tests serialization of events
   * into XML format and subsequent reloading of these events, verifying data integrity
   * and consistency.
   */
  @Test
  public void testSaveSchedule() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.saveUserSchedule("Prof. Lucia"));
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    this.system.saveUserSchedule("Prof. Lucia");
    File file = new File("prof. lucia.xml");

    PlannerSystem other = new NUPlannerSystem();
    other.readUserSchedule(file);
    Schedule schedule = other.getSchedule("Prof. Lucia");
    Schedule anonSchedule = other.getSchedule("Student Anon");

    Assert.assertEquals("Prof. Lucia", schedule.getUserId());

    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("Prof. Lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Event event2 = new Event();
    event2.setName("CS3500 Afternoon Lecture");
    event2.setEventTimes("Tuesday", "1335", "Tuesday", "1515");
    event2.setLocation(false, "Churchill Hall 101");
    event2.setHost("Prof. Lucia");
    event2.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Chat")));

    Event event3 = new Event();
    event3.setName("Sleep");
    event3.setEventTimes("Friday", "1800", "Sunday", "1200");
    event3.setLocation(true, "Home");
    event3.setHost("Prof. Lucia");
    event3.setInvitees(new ArrayList<>(List.of("Prof. Lucia")));

    List<Event> events = new ArrayList<>(Arrays.asList(event, event2, event3));
    Assert.assertEquals(schedule.getEvents(), events);
    Assert.assertTrue(anonSchedule.hasEvent(event));
  }

  /**
   * Confirms the display functionality accurately represents a user's schedule, including
   * event details and ordering. This test checks the system's ability to format the schedule
   * and events into a human-readable string that matches expected output.
   */
  @Test
  public void testDisplay() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.displayUserSchedule("Prof. Lucia"));

    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Assert.assertEquals("Sunday:" + System.lineSeparator()
            + "Monday:" + System.lineSeparator()
            + "Tuesday:" + System.lineSeparator()
            + "        name: CS3500 Morning Lecture" + System.lineSeparator()
            + "        time: Tuesday: 09:50 -> Tuesday: 11:30" + System.lineSeparator()
            + "        location: Churchill Hall 101" + System.lineSeparator()
            + "        online: false" + System.lineSeparator()
            + "        invitees: Prof. Lucia" + System.lineSeparator()
            + "                  Student Anon" + System.lineSeparator()
            + "                  Chat" + System.lineSeparator()
            + "        name: CS3500 Afternoon Lecture" + System.lineSeparator()
            + "        time: Tuesday: 13:35 -> Tuesday: 15:15" + System.lineSeparator()
            + "        location: Churchill Hall 101" + System.lineSeparator()
            + "        online: false" + System.lineSeparator()
            + "        invitees: Prof. Lucia" + System.lineSeparator()
            + "                  Chat" + System.lineSeparator()
            + "Wednesday:" + System.lineSeparator()
            + "Thursday:" + System.lineSeparator()
            + "Friday:" + System.lineSeparator()
            + "        name: Sleep" + System.lineSeparator()
            + "        time: Friday: 18:00 -> Sunday: 12:00" + System.lineSeparator()
            + "        location: Home" + System.lineSeparator()
            + "        online: true" + System.lineSeparator()
            + "        invitees: Prof. Lucia" + System.lineSeparator()
            + "Saturday:", system.displayUserSchedule("Prof. Lucia"));
  }

  /**
   * Validates the system's capability to create new events and add them to the appropriate
   * user schedules. This includes testing for correct event creation, handling of scheduling
   * conflicts, and management of event invitees. Also tests scenarios where events cannot
   * be added due to conflicts or invalid parameters, expecting an {@link IllegalArgumentException}.
   */
  @Test
  public void testCreateEvent() {

    // create an event without a xml file
    system.createEvent("Student Anon", "OH", "Sunday", "0950",
            "Sunday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    Event event1 = new Event();
    event1.setName("OH");
    event1.setEventTimes("Sunday", "0950", "Sunday", "1030");
    event1.setLocation(false, "ChurchHill Hall 101");
    event1.setHost("Student Anon");
    event1.setInvitees(new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    Schedule anonSchedule = system.getSchedule("Student Anon");
    Assert.assertTrue(anonSchedule.hasEvent(event1));

    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    // create an event the invitees can't attend
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.createEvent("Student Anon", "OH", "Tuesday",
                    "1335", "Tuesday", "1500", false,
                    "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat"))));

    // create an event the user cannot attend
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.createEvent("Student Anon", "OH", "Tuesday",
                    "0950", "Tuesday", "1030", false,
                    "ChurchHill Hall 101",
                    new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat"))));

    system.createEvent("Student Anon", "OH", "Monday", "0950",
            "Monday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));
    Event event = new Event();
    event.setName("OH");
    event.setEventTimes("Monday", "0950", "Monday", "1030");
    event.setLocation(false, "ChurchHill Hall 101");
    event.setHost("Student Anon");
    event.setInvitees(new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    Assert.assertTrue(anonSchedule.hasEvent(event));
  }

  /**
   * Examines the functionality to modify existing events within user schedules. Checks that
   * modifications to event details are accurately reflected in the schedules, and that the
   * system correctly handles invalid modification attempts. Includes testing modification
   * of various event attributes and validation of resulting event states.
   */
  @Test
  public void testModifyEvent() {
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("Prof. Lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    // testing null event
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", null, "CS3500 Revision",
                    "Tuesday", "0950", "Tuesday", "1130",
                    true, "ChurchHill 101",
                    new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    // testing user doesn't exist
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                    "Tuesday", "0950", "Tuesday", "1130",
                    true, "ChurchHill 101",
                    new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    // adding user by creating an event then checking if user has this event we want to modify
    system.createEvent("Student Anon", "OH", "Monday", "0950",
            "Monday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    // testing if user contains the CS3500 morning lecture
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                    "Tuesday", "0950", "Tuesday", "1130",
                    true, "ChurchHill 101",
                    new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    // adding the events to the system through a xml file
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Schedule anonSchedule = system.getSchedule("Student Anon");
    Assert.assertNotNull(anonSchedule);
    Assert.assertTrue(anonSchedule.hasEvent(event));

    //modifying event's name wrongly
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "", "Tuesday",
                "0950", "Tuesday", "1130", true,
                "ChurchHill 101", new ArrayList<>(Arrays.asList("Prof. Lucia",
                        "Student Anon", "Chat"))));

    // check if event changed
    Assert.assertEquals(event.getName(), "CS3500 Morning Lecture");
    Assert.assertTrue(anonSchedule.hasEvent(event));

    // successful modification
    system.modifyEvent("Student Anon", event, "CS3500 Revision", "Tuesday",
            "0950", "Tuesday", "1130", true,
            "ChurchHill 101", new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon",
                    "Chat")));
    Assert.assertEquals(event.getName(), "CS3500 Revision");

    // try modifying a time that is busy in another user's schedule
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                "Tuesday", "1200", "Tuesday", "1340", true,
                "ChurchHill 101", new ArrayList<>(Arrays.asList("Prof. Lucia",
                        "Student Anon", "Chat"))));

    // good time modification
    system.modifyEvent("Student Anon", event, "CS3500 Revision", "Tuesday",
            "1200", "Tuesday", "1315", true,
            "ChurchHill 101", new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon",
                    "Chat")));
    Assert.assertEquals(event.getTime(), new Time("Tuesday", "1200",
            "Tuesday", "1315"));

    // bad location modification
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                "Tuesday", "1200", "Tuesday", "1315", true,
                "", new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    system.modifyEvent("Student Anon", event, "CS3500 Revision",
            "Tuesday", "1200", "Tuesday", "1315", true,
            "Ryder Hall", new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon",
                    "Chat")));
    Assert.assertEquals("Ryder Hall", event.getLocation().getLocation());

    // bad invitees modification
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                "Tuesday", "1200", "Tuesday", "1315", true,
                "Ryder Hall", new ArrayList<>(Arrays.asList("Student Anon", "Chat"))));

    system.modifyEvent("Student Anon", event, "CS3500 Revision", "Tuesday",
            "1200", "Tuesday", "1315", true, "Ryder Hall",
            new ArrayList<>(List.of("Prof. Lucia")));

    Assert.assertEquals(event.getInvitees(), new ArrayList<>(List.of("Prof. Lucia")));
  }

  /**
   * Tests the system's process for removing events from user schedules, including special
   * handling for event cancellation by the host. Verifies that removed events are correctly
   * omitted from all relevant schedules and that invitee lists are updated accordingly.
   * Also tests removal in scenarios with invalid parameters, expecting an
   * {@link IllegalArgumentException}.
   */
  @Test
  public void testRemoveEvent() {
    Event event = new Event();
    event.setName("CS3500 Morning Lecture");
    event.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("Prof. Lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.removeEvent("Prof. Lucia", null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.removeEvent("Prof. Lucia", event));

    // adding user by creating an event then checking if user has this event we want to modify
    system.createEvent("Student Anon", "OH", "Monday", "0950",
            "Monday", "1030", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.removeEvent("Prof. Lucia", event));

    // adding the events to the system through a xml file
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    Event event2 = new Event();
    event2.setName("CS3500 Afternoon Lecture");
    event2.setEventTimes("Tuesday", "1335", "Tuesday", "1515");
    event2.setLocation(false, "Churchill Hall 101");
    event2.setHost("Prof. Lucia");
    event2.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Chat")));

    Schedule chatSchedule = system.getSchedule("Chat");
    Schedule profSchedule = system.getSchedule("Prof. Lucia");
    Schedule anonSchedule = system.getSchedule("Student Anon");
    system.removeEvent("Chat", event2);

    Assert.assertEquals(new ArrayList<>(List.of("Prof. Lucia")), event2.getInvitees());
    Assert.assertFalse(chatSchedule.hasEvent(event2));
    System.out.println(profSchedule.getEvents());
    Assert.assertTrue(profSchedule.hasEvent(event2));

    Assert.assertTrue(anonSchedule.hasEvent(event));
    Assert.assertTrue(profSchedule.hasEvent(event));
    Assert.assertTrue(chatSchedule.hasEvent(event));

    system.removeEvent("Prof. Lucia", event);
    Assert.assertFalse(anonSchedule.hasEvent(event));
    Assert.assertFalse(profSchedule.hasEvent(event));
    Assert.assertFalse(chatSchedule.hasEvent(event));
  }

  /**
   * Confirms the system's ability to accurately identify and display events occurring at a
   * specified time and day. Tests both the detection of scheduled events and handling of
   * periods with no scheduled events, ensuring the system provides appropriate feedback.
   */
  @Test
  public void testShowEvent() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.showEvent("Prof. Lucia", "Tuesday", "1000"));
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.showEvent("Prof. Lucia", null, "1000"));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.showEvent("Prof. Lucia", "Tuesday", null));
    Assert.assertEquals("CS3500 Morning Lecture happens at this time",
            system.showEvent("Prof. Lucia", "tuesday", "1000"));
    Assert.assertEquals("No event exists at this time",
            system.showEvent("Student Anon", "tuesday", "1130"));
  }
}
