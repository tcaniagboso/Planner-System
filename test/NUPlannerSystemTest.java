import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import plannersystem.NUPlannerSystem;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;

public class NUPlannerSystemTest {

  private PlannerSystem system;

  @Before
  public void init() {
    system = new NUPlannerSystem();
  }

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


}
