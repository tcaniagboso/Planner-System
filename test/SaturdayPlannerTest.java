import org.junit.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import schedule.Event;
import schedule.IEvent;
import schedule.ISchedule;
import schedulestrategy.AnyTimeScheduleStrategy;

/**
 * A specialized test class that extends the NUPlannerSystemTest to handle test cases with
 * Saturday as the first day of the week. This class tests functionality such as event creation,
 * scheduling, and displaying events, ensuring that the system's behavior aligns with expectations
 * when Saturday is considered the start of the week.
 */
public class SaturdayPlannerTest extends NUPlannerSystemTest {

  /**
   * Initializes test environment, setting Saturday as the first day of the week.
   */
  @Override
  public void init() {
    super.init();
    system.setFirstDayOfWeek("Saturday");
  }

  /**
   * Tests the creation of an event in a scenario where an event that starts on Friday and ends on
   * Sunday already exists in the schedule. Verifies that a new event on Saturday does not conflict
   * with the existing one, adhering to the unique constraints of Saturday being the start of the
   * week.
   */
  @Override
  public void testCreateEvent() {
    super.testCreateEvent();

    // test creating an event on saturday when an event that starts on friday and ends on Sunday
    // already exists in prof. lucia schedule. This should not overlap because saturday is the
    // start day.
    system.createEvent("Student Anon", "Weekend", "Saturday", "0950",
            "Sunday", "0800", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    IEvent event = new Event();
    event.setName("Weekend");
    event.setEventTimes("Saturday", "0950", "Sunday", "0800");
    event.setLocation(false, "ChurchHill Hall 101");
    event.setHost("Student Anon");
    event.setInvitees(new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));

    ISchedule profSchedule = system.getSchedule("Prof. Lucia");
    ISchedule anonSchedule = system.getSchedule("Student Anon");
    ISchedule chatSchedule = system.getSchedule("Chat");

    Assert.assertTrue(profSchedule.hasEvent(event));
    Assert.assertTrue(anonSchedule.hasEvent(event));
    Assert.assertTrue(chatSchedule.hasEvent(event));
  }

  /**
   * Tests if anytime scheduling starts to schedule events from Saturday 00:00 for a saturday
   * planner.
   */
  @Override
  public void testAnyTimeScheduling() {
    system.readUserSchedule(new File("prof.xml"));
    system.readUserSchedule(new File("chat.xml"));

    system.setScheduleStrategy(new AnyTimeScheduleStrategy());

    ISchedule chatSchedule = system.getSchedule("Chat");
    ISchedule profSchedule = system.getSchedule("Prof. Lucia");
    ISchedule anonSchedule = system.getSchedule("Student Anon");

    // try scheduling with a duration that is more than 6 days 23 hours and 59 minutes.
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.scheduleEvent("Prof. Lucia", "Finals week", false,
                    "Ryder Hall", 10080,
                    new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // when duration is 0 minutes or less
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.scheduleEvent("Prof. Lucia", "Finals week", false,
                    "Ryder Hall", 0,
                    new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.scheduleEvent("Prof. Lucia", "Finals week", false,
                    "Ryder Hall", -100,
                    new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // test scheduling a four-day event, prof. lucia doesn't have space for it.
    Assert.assertThrows(IllegalArgumentException.class,
        () -> system.scheduleEvent("Prof. Lucia", "Finals week", false,
                    "Ryder Hall", 5760,
                    new ArrayList<>(List.of("Prof. Lucia", "Chat", "Student Anon"))));

    // schedule a one-day event, expected to be scheduled from Saturday 00:00 to Sunday 00:00
    system.scheduleEvent("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 1440,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));

    IEvent oneDayEvent = new Event();
    oneDayEvent.setName("Finals week");
    oneDayEvent.setEventTimes("Saturday", "0000", "Sunday",
            "0000");
    oneDayEvent.setLocation(false, "Ryder Hall");
    oneDayEvent.setHost("Prof. Lucia");
    oneDayEvent.setInvitees(new ArrayList<>(Arrays.asList("Prof. Lucia", "Student Anon", "Chat")));

    Assert.assertTrue(profSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(anonSchedule.hasEvent(oneDayEvent));
    Assert.assertTrue(chatSchedule.hasEvent(oneDayEvent));

    // test scheduling a three-day event it should start from the last tuesday cs Morning lecture
    system.scheduleEvent("Prof. Lucia", "Finals week", false,
            "Ryder Hall", 4320,
            new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat")));

    IEvent threeEvent = new Event();
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
  }

  /**
   * Tests displaying an event's details based on the specified time, verifying the correct
   * information is shown for an event occurring on a Saturday.
   */
  @Override
  public void testShowEvent() {
    super.testShowEvent();
    system.createEvent("Student Anon", "Weekend", "Saturday", "0950",
            "Sunday", "0800", false, "ChurchHill Hall 101",
            new ArrayList<>(Arrays.asList("Student Anon", "Prof. Lucia", "Chat")));
    Assert.assertEquals("Weekend happens at this time",
            system.showEvent("Student Anon", "Saturday", "1200"));
  }
}


