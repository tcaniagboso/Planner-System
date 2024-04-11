import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import autoscheduling.AnyTimeSchedule;
import autoscheduling.AutoSchedule;
import autoscheduling.LenientSchedule;
import autoscheduling.WorkHourSchedule;
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
        () -> system.saveUserSchedule("Prof. Lucia", "prof2.xml"));
    File xmlFile = new File("prof.xml");
    system.readUserSchedule(xmlFile);

    this.system.saveUserSchedule("Prof. Lucia", "prof2.xml");
    File file = new File("prof2.xml");

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
                    "ChurchHill 101",
                new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    // check if event changed
    Assert.assertEquals(event.getName(), "CS3500 Morning Lecture");
    Assert.assertTrue(anonSchedule.hasEvent(event));

    // successful modification
    system.modifyEvent("Student Anon", event, "CS3500 Revision", "Tuesday",
            "0950", "Tuesday", "1130", true,
            "ChurchHill 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));
    Assert.assertEquals(event.getName(), "CS3500 Revision");

    // try modifying a time that is busy in another user's schedule
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                    "Tuesday", "1200", "Tuesday", "1340",
                true, "ChurchHill 101",
                new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

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
                    "Tuesday", "1200", "Tuesday", "1315",
                true, "",
                new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat"))));

    system.modifyEvent("Student Anon", event, "CS3500 Revision",
            "Tuesday", "1200", "Tuesday", "1315", true,
            "Ryder Hall",
            new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));
    Assert.assertEquals("Ryder Hall", event.getLocation().getLocation());

    // bad invitees modification
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.modifyEvent("Student Anon", event, "CS3500 Revision",
                    "Tuesday", "1200", "Tuesday", "1315",
                true, "Ryder Hall",
                new ArrayList<>(Arrays.asList("Student Anon", "Chat"))));

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

  /**
   * Verifies the functionality of retrieving a specific user's schedule from the system.
   * The test ensures that an attempt to retrieve a schedule for a non-existent user results
   * in the expected exception. It also checks that after loading schedules from an XML file,
   * the schedules are correctly retrieved and contain the expected events, demonstrating the
   * system's ability to parse and load user schedules accurately.
   */
  @Test
  public void getSchedule() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.getSchedule("Prof. Lucia"));
    system.readUserSchedule(new File("prof.xml"));
    Schedule schedule = system.getSchedule("Prof. Lucia");
    Assert.assertNotNull(schedule);
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

    Assert.assertEquals(schedule.getEvents(), new ArrayList<>(List.of(event, event2, event3)));
  }

  /**
   * Tests the system's capability to list all users with schedules. Initially verifies that the
   * system correctly reports an empty list when no schedules have been loaded, ensuring the
   * system's initial state is as expected. After loading schedules, the test confirms that all
   * users present in the XML file are accurately identified and listed, validating the system's
   * ability to track user schedules.
   */
  @Test
  public void testGetUsers() {
    Assert.assertTrue(system.getUsers().isEmpty());

    system.readUserSchedule(new File("prof.xml"));
    Set<String> existingUsers = system.getUsers();

    Assert.assertTrue(existingUsers.contains("Prof. Lucia"));
    Assert.assertTrue(existingUsers.contains("Student Anon"));
    Assert.assertTrue(existingUsers.contains("Chat"));
    Assert.assertEquals(existingUsers.size(), 3);
  }

  /**
   * Tests the system's ability to detect scheduling conflicts for a given event across all user
   * schedules. It begins by asserting that passing a null event to the conflict check method throws
   * an exception, ensuring that the system correctly handles invalid input. The test further
   * examines the system's response to both non-conflicting and conflicting events when compared
   * against existing schedules, validating the conflict detection logic.
   */
  @Test
  public void testCheckEventConflict() {
    Assert.assertThrows(IllegalArgumentException.class, () -> system.checkEventConflict(null));

    // conflicting event
    Event event = new Event();
    event.setName("OH");
    event.setEventTimes("Tuesday", "1100", "Tuesday", "1300");
    event.setLocation(false, "Churchill Hall 101");
    event.setHost("Prof. Lucia");
    event.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));


    // Test with no events in the system
    Assert.assertTrue(system.checkEventConflict(event));

    // add events and users to the schedule
    system.readUserSchedule(new File("prof.xml"));
    Assert.assertFalse(system.checkEventConflict(event));

    // change the time of an event to a non-conflicting time
    event.setEventTimes("Monday", "1100", "Monday", "1300");
    Assert.assertTrue(system.checkEventConflict(event));
  }

  /**
   * Ensures the system's observer management mechanisms function correctly. Specifically, the test
   * checks that attempts to add or remove a null observer result in the appropriate exception,
   * safeguarding against invalid manipulation of the system's observer list. This verifies the
   * integrity of the observer pattern implementation within the system.
   */
  @Test
  public void testAddRemoveObserver() {
    Assert.assertThrows(IllegalArgumentException.class, () -> system.addObserver(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> system.removeObserver(null));
  }

  /**
   * Validates the constraints and error handling within the system's automatic scheduling feature.
   * The test ensures that invalid inputs such as null scheduling strategies or inappropriate event
   * durations lead to expected exceptions. This confirms the system's robustness in managing auto
   * scheduling requests and its adherence to defined scheduling constraints.
   */
  @Test
  public void testAutoSchedulingValidation() {
    // throws for null
    Assert.assertThrows(IllegalArgumentException.class, () -> system.setScheduleStrategy(null));

    // throws because scheduling strategy hasn't been set
    Assert.assertThrows(IllegalStateException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 10080,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));
  }

  /**
   * Tests the "Any Time" scheduling strategy, focusing on its capability to find scheduling
   * opportunities within a wide temporal window. The strategy's handling of various event durations
   * and its interaction with existing schedules are examined, particularly its ability to schedule
   * events even when the requested duration approaches the maximum allowed. This test not only
   * verifies the strategy's effectiveness but also its adherence to logical constraints regarding
   * event duration and scheduling feasibility.
   */
  @Test
  public void testAnyTimeScheduling() {
    system.readUserSchedule(new File("prof.xml"));
    system.readUserSchedule(new File("chat.xml"));

    system.setScheduleStrategy(new AnyTimeSchedule());

    Schedule chatSchedule = system.getSchedule("Chat");
    Schedule profSchedule = system.getSchedule("Prof. Lucia");
    Schedule anonSchedule = system.getSchedule("Student Anon");

    // try scheduling with a duration that is more than 6 days 23 hours and 59 minutes.
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 10080,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // when duration is 0 minutes or less
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 0,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", -100,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // test scheduling a four-day event, prof. lucia doesn't have space for it.
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 5760,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // schedule a one-day event, expected to be scheduled from Sunday 00:00 to Monday 00:00
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 1440,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));

    Event oneDayEvent = new Event();
    oneDayEvent.setName("Finals week");
    oneDayEvent.setEventTimes("Sunday", "0000", "Monday", "0000");
    oneDayEvent.setLocation(false, "Ryder Hall");
    oneDayEvent.setHost("Prof. Lucia");
    oneDayEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(anonSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(chatSchedule.hasEvent(oneDayEvent));

    // test scheduling a three-day event it should start from the last tuesday cs Morning lecture
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 4320,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));

    Event threeEvent = new Event();
    threeEvent.setName("Finals week");
    threeEvent.setEventTimes("Tuesday", "1515", "Friday", "1515");
    threeEvent.setLocation(false, "Ryder Hall");
    threeEvent.setHost("Prof. Lucia");
    threeEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(threeEvent));
    Assert.assertTrue(anonSchedule.hasEvent(threeEvent));
    Assert.assertTrue(chatSchedule.hasEvent(threeEvent));

    // confirming that the right number of events are in the user's schedules
    Assert.assertEquals(profSchedule.getEvents().size(), 7);
    Assert.assertEquals(anonSchedule.getEvents().size(), 4);
    Assert.assertEquals(chatSchedule.getEvents().size(), 6);

    PlannerSystem newSystem = new NUPlannerSystem();
    newSystem.readUserSchedule(new File("chat.xml"));
    newSystem.setScheduleStrategy(new AnyTimeSchedule());
    Schedule chatSchedule2 = newSystem.getSchedule("Chat");
    Schedule profSchedule2 = newSystem.getSchedule("Prof. Lucia");

    // schedule an event in this new empty system from Monday 15:15 to saturday 23:59
    newSystem.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 7724,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));
    Event weekEvent = new Event();
    weekEvent.setName("Finals week");
    weekEvent.setEventTimes("Monday", "1515", "Saturday", "2359");
    weekEvent.setLocation(false, "Ryder Hall");
    weekEvent.setHost("Prof. Lucia");
    weekEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Schedule anonSchedule2 = newSystem.getSchedule("Student Anon");
    Assert.assertTrue(chatSchedule2.hasEvent(weekEvent));
    Assert.assertTrue(profSchedule2.hasEvent(weekEvent));
    Assert.assertTrue(anonSchedule2.hasEvent(weekEvent));

    // schedule an event from saturday 23:59 to saturday 23:58
    newSystem.automaticScheduling("Prof. Lucia", "Spring Break", false,
            "Cabo", 10079,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));

    Event breakEvent = new Event();
    breakEvent.setName("Spring Break");
    breakEvent.setEventTimes("Saturday", "2359", "Saturday",
            "2358");
    breakEvent.setLocation(false, "Cabo");
    breakEvent.setHost("Prof. Lucia");
    breakEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));
    Assert.assertTrue(chatSchedule2.hasEvent(breakEvent));
    Assert.assertTrue(profSchedule2.hasEvent(breakEvent));
    Assert.assertTrue(anonSchedule2.hasEvent(breakEvent));

    // confirming that the right number of events are in the user's schedules
    Assert.assertEquals(profSchedule2.getEvents().size(), 4);
    Assert.assertEquals(anonSchedule2.getEvents().size(), 3);
    Assert.assertEquals(chatSchedule2.getEvents().size(), 4);
  }

  /**
   * Evaluates the combined functionality of the Work Hour and Lenient scheduling strategies,
   * emphasizing their distinct constraints and flexibilities. Through scheduling attempts that
   * explore the limits of work hours and lenient scheduling parameters, the test assesses each
   * strategy's ability to accommodate scheduling requests under tight constraints. The goal is to
   * ensure that these strategies can correctly apply their unique scheduling rules to accommodate
   * or reject events based on the specific conditions set forth by the strategy.
   */
  @Test
  public void testWorkHourLenientSchedule() {
    system.readUserSchedule(new File("prof.xml"));
    system.setScheduleStrategy(new WorkHourSchedule());

    Schedule chatSchedule = system.getSchedule("Chat");
    Schedule profSchedule = system.getSchedule("Prof. Lucia");
    Schedule anonSchedule = system.getSchedule("Student Anon");

    // try scheduling with a duration that is more than 8 hours.
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 481,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // when duration is 0 minutes or less
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", 0,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Prof. Lucia", "Finals week", false,
                "Ryder Hall", -100,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // Schedules an event on monday for 8 hours
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 480,
            new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon")));

    Event oneDayEvent = new Event();
    oneDayEvent.setName("Finals week");
    oneDayEvent.setEventTimes("Monday", "0900", "Monday", "1700");
    oneDayEvent.setLocation(false, "Ryder Hall");
    oneDayEvent.setHost("Prof. Lucia");
    oneDayEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(anonSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(chatSchedule.hasEvent(oneDayEvent));

    // Schedules an event on wednesday for 8 hours because there is no 8 hours window on tuesday
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 480,
            new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon")));
    Event nextEvent = new Event();
    nextEvent.setName("Finals week");
    nextEvent.setEventTimes("Wednesday", "0900", "Wednesday",
            "1700");
    nextEvent.setLocation(false, "Ryder Hall");
    nextEvent.setHost("Prof. Lucia");
    nextEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(nextEvent));
    Assert.assertTrue(anonSchedule.hasEvent(nextEvent));
    Assert.assertTrue(chatSchedule.hasEvent(nextEvent));

    // Schedules an event on Thursday for prof lucia and chat
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 480, new ArrayList<>(List.of("Prof. Lucia", "Chat")));
    Event thirdEvent = new Event();
    thirdEvent.setName("Finals week");
    thirdEvent.setEventTimes("Thursday", "0900", "Thursday",
            "1700");
    thirdEvent.setLocation(false, "Ryder Hall");
    thirdEvent.setHost("Prof. Lucia");
    thirdEvent.setInvitees(new ArrayList<>(List.of("Prof. Lucia", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(thirdEvent));
    Assert.assertTrue(chatSchedule.hasEvent(thirdEvent));

    // Schedules an event on Friday for prof. lucia
    system.automaticScheduling("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 480, new ArrayList<>(List.of("Prof. Lucia")));

    Event friEvent = new Event();
    friEvent.setName("Finals week");
    friEvent.setEventTimes("Friday", "0900", "Friday",
            "1700");
    friEvent.setLocation(false, "Ryder Hall");
    friEvent.setHost("Prof. Lucia");
    friEvent.setInvitees(new ArrayList<>(List.of("Prof. Lucia")));

    Assert.assertTrue(profSchedule.hasEvent(friEvent));

    // tries to schedule an 8-hour event when prof. lucia has no free working days
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Student Anon", "Finals week", false,
                "Ryder Hall", 480,
                new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // test Lenient schedule strategy
    system.setScheduleStrategy(new LenientSchedule());

    // Schedules an event on Thursday for Student Anon, John and Jake but not Prof Lucia and Chat
    system.automaticScheduling("Student Anon", "TGIT", false,
            "Ryder Hall", 480,
            new ArrayList<>(List.of("Student Anon", "Chat", "Prof. Lucia", "John", "Jake")));

    Event anonEvent = new Event();
    anonEvent.setName("TGIT");
    anonEvent.setEventTimes("Thursday", "0900", "Thursday",
            "1700");
    anonEvent.setLocation(false, "Ryder Hall");
    anonEvent.setHost("Student Anon");
    anonEvent.setInvitees(new ArrayList<>(List.of("Student Anon", "John", "Jake")));

    Schedule jakeSchedule = system.getSchedule("Jake");
    Schedule johnSchedule = system.getSchedule("John");

    Assert.assertFalse(profSchedule.hasEvent(anonEvent));
    Assert.assertTrue(anonSchedule.hasEvent(anonEvent));
    Assert.assertFalse(chatSchedule.hasEvent(anonEvent));
    Assert.assertTrue(jakeSchedule.hasEvent(anonEvent));
    Assert.assertTrue(johnSchedule.hasEvent(anonEvent));

    // Schedules an event on Friday for Student Anon Chat John and Jake but not Prof Lucia
    system.automaticScheduling("Student Anon", "TGIF", false,
            "Ryder Hall", 480,
            new ArrayList<>(List.of("Student Anon", "Chat", "Prof. Lucia", "John", "Jake")));

    Event tgif = new Event();
    tgif.setName("TGIF");
    tgif.setEventTimes("Friday", "0900", "Friday",
            "1700");
    tgif.setLocation(false, "Ryder Hall");
    tgif.setHost("Student Anon");
    tgif.setInvitees(new ArrayList<>(List.of("Student Anon", "Chat", "John", "Jake")));

    Assert.assertFalse(profSchedule.hasEvent(tgif));
    Assert.assertTrue(anonSchedule.hasEvent(tgif));
    Assert.assertTrue(chatSchedule.hasEvent(tgif));
    Assert.assertTrue(jakeSchedule.hasEvent(tgif));
    Assert.assertTrue(johnSchedule.hasEvent(tgif));


    // can't schedule because only anon the host can attend even though it is lenient
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.automaticScheduling("Student Anon", "TGIF", false,
                "Ryder Hall", 480,
                new ArrayList<>(List.of("Student Anon", "Chat", "Prof. Lucia"))));

    // confirming that the right number of events are in the user's schedules
    Assert.assertEquals(profSchedule.getEvents().size(), 7);
    Assert.assertEquals(anonSchedule.getEvents().size(), 5);
    Assert.assertEquals(chatSchedule.getEvents().size(), 6);
  }

  /**
   * Tests the validation checks within the anytime scheduling strategy implementations.
   * Verifies that illegal arguments such as null events, zero or negative durations, and null
   * or empty lists of schedules trigger the expected exceptions. This ensures the system's
   * robustness in handling invalid input scenarios during automatic event scheduling.
   */
  @Test
  public void testAnyTimeSchedulingValidation() {
    AutoSchedule autoSchedule = new AnyTimeSchedule();

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(null, 20,
                new ArrayList<>(List.of(new Schedule("Tobe")))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 0,
                new ArrayList<>(List.of(new Schedule("Tobe")))));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), -10,
                new ArrayList<>(List.of(new Schedule("Tobe")))));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100, null));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100, new ArrayList<>()));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100,
                new ArrayList<>(Arrays.asList(null, null))));
  }

  /**
   * Validates the constraints specific to the work hour schedule strategy, including the handling
   * of events outside the predefined work hours. Tests assert that the strategy correctly rejects
   * scheduling requests that violate the work hour limits or other constraints, ensuring that the
   * scheduling behavior aligns with expected work hour policies.
   */
  @Test
  public void testWorkHourScheduleValidation() {

    // work hour schedule and lenient schedule strategy use the same method
    AutoSchedule autoSchedule = new WorkHourSchedule();

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(null, 20,
                new ArrayList<>(List.of(new Schedule("Tobe")))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 0,
                new ArrayList<>(List.of(new Schedule("Tobe")))));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), -10,
                new ArrayList<>(List.of(new Schedule("Tobe")))));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100, null));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100, new ArrayList<>()));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> autoSchedule.scheduleEvent(new Event(), 100,
                new ArrayList<>(Arrays.asList(null, null))));
  }
}
