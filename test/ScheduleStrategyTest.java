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
import schedule.IEvent;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedule.Schedule;
import schedule.Time;
import schedulestrategy.AnyTimeScheduleStrategy;
import schedulestrategy.LenientScheduleStrategy;
import schedulestrategy.ScheduleStrategy;
import schedulestrategy.WorkHourScheduleStrategy;

/**
 * Tests the functionality of different scheduling strategies implemented in the PlannerSystem.
 * Each method within this class is designed to test various aspects of scheduling including
 * handling invalid inputs and ensuring correct event scheduling under normal and edge conditions.
 */
public class ScheduleStrategyTest {

  private ScheduleStrategy strategy;

  private PlannerSystem system;

  /**
   * Initializes the PlannerSystem instance before each test.
   */
  @Before
  public void init() {
    system = new NUPlannerSystem();
    system.setFirstDayOfWeek("Sunday");
  }

  /**
   * Tests the AnyTimeScheduleStrategy by loading schedules, checking input validations,
   * and verifying null or correct event placements based on provided conditions.
   */
  @Test
  public void testAnyTimeScheduling() {
    strategy = new AnyTimeScheduleStrategy();
    strategy.setFirstDayOfWeek("Sunday");

    system.readUserSchedule(new File("prof.xml"));
    system.readUserSchedule(new File("chat.xml"));

    ISchedule chatSchedule = system.getSchedule("Chat");
    ISchedule profSchedule = system.getSchedule("Prof. Lucia");
    ISchedule anonSchedule = system.getSchedule("Student Anon");

    List<ISchedule> scheduleList = new ArrayList<>(List.of(profSchedule, chatSchedule,
            anonSchedule));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(null, 200, scheduleList));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(new Event(), 0, scheduleList));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(new Event(), -10, scheduleList));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(new Event(), 10080, scheduleList));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(new Event(), 200, new ArrayList<>()));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(new Event(), 200, null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> strategy.scheduleEvent(null, 200,
                    new ArrayList<>(Arrays.asList(profSchedule, null))));

    List<String> invitees = new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat"));
    IEvent event = new Event();
    event.setName("Something");
    event.setLocation(true, "Somewhere");
    event.setHost("Prof. Lucia");
    event.setInvitees(invitees);

    // schedule a four-day event prof.lucia has no space for it.
    ReadOnlyEvent scheduledEvent = strategy.scheduleEvent(event, 5760, scheduleList);
    Assert.assertNull(scheduledEvent);

    // schedule a one-day event from Sunday 00:00 to Monday 00:00, every one can make it.
    scheduledEvent = strategy.scheduleEvent(event, 1440, scheduleList);
    Assert.assertEquals(scheduledEvent.getTime(),
            new Time("Sunday", "0000", "Monday", "0000"));

    Assert.assertEquals(scheduledEvent.getInvitees(), invitees);

  }

  /**
   * Tests the WorkHourScheduleStrategy and LenientScheduleStrategy. It validates the handling of
   * events during working hours, checks proper event placement within constrained work hours, and
   * tests the flexibility of the lenient strategy with less restrictive conditions.
   */
  @Test
  public void testWorkHourLenientScheduling() {
    strategy = new WorkHourScheduleStrategy();

    system.readUserSchedule(new File("prof.xml"));
    system.readUserSchedule(new File("chat.xml"));

    ISchedule chatSchedule = system.getSchedule("Chat");
    ISchedule profSchedule = system.getSchedule("Prof. Lucia");
    ISchedule anonSchedule = system.getSchedule("Student Anon");


    String[] days = {"Sunday", "Saturday"};
    for (String day : days) {
      List<ISchedule> scheduleList = new ArrayList<>(List.of(profSchedule, chatSchedule,
              anonSchedule));

      strategy.setFirstDayOfWeek(day);

      // these scheduling strategies do the same validation and majority of the validation has
      // been tested above in the testAnyTimeScheduling method.

      // more than 8 hours.
      Assert.assertThrows(IllegalArgumentException.class,
          () -> strategy.scheduleEvent(new Event(), 481, scheduleList));

      List<String> invitees = new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat"));
      IEvent event = new Event();
      event.setName("Something");
      event.setLocation(true, "Somewhere");
      event.setHost("Prof. Lucia");
      event.setInvitees(invitees);

      // returns an event on wednesday for 8 working hours because monday and tuesday are taken in
      // prof. lucia's schedule and chat's schedule
      ReadOnlyEvent scheduled = strategy.scheduleEvent(event, 480, scheduleList);

      Assert.assertEquals(scheduled.getTime(),
              new Time("Wednesday", "0900", "Wednesday", "1700"));
      Assert.assertTrue(scheduled.getInvitees().contains("Prof. Lucia"));
      Assert.assertTrue(scheduled.getInvitees().contains("Chat"));
      Assert.assertTrue(scheduled.getInvitees().contains("Student Anon"));

      // john and jake have a whole free week
      scheduleList.add(new Schedule("John"));
      scheduleList.add(new Schedule("Jake"));

      // change to lenient scheduling
      strategy = new LenientScheduleStrategy();
      strategy.setFirstDayOfWeek(day);
      IEvent event2 = new Event();
      event2.setName("Something");
      event2.setLocation(true, "Somewhere");
      event2.setHost("John");
      event2.setInvitees(
              new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat", "John", "Jake")));

      scheduled = strategy.scheduleEvent(event2, 480, scheduleList);

      Assert.assertEquals(scheduled.getTime(),
              new Time("Monday", "0900", "Monday", "1700"));
      Assert.assertEquals(scheduled.getInvitees(),
              new ArrayList<>(List.of("John", "Jake")));
    }

  }

  /**
   * Additional test for AnyTimeScheduleStrategy considering Saturday as the starting day of the
   * week. It checks the correct scheduling of events from Saturday through the following days,
   * ensuring system flexibility with different starting days.
   */
  @Test
  public void testSaturdayAnytimeScheduling() {
    strategy = new AnyTimeScheduleStrategy();
    strategy.setFirstDayOfWeek("Saturday");

    system.readUserSchedule(new File("prof.xml"));
    system.readUserSchedule(new File("chat.xml"));

    ISchedule chatSchedule = system.getSchedule("Chat");
    ISchedule profSchedule = system.getSchedule("Prof. Lucia");
    ISchedule anonSchedule = system.getSchedule("Student Anon");

    List<ISchedule> scheduleList = new ArrayList<>(List.of(profSchedule, chatSchedule,
            anonSchedule));

    List<String> invitees = new ArrayList<>(List.of("Prof. Lucia", "Student Anon", "Chat"));
    IEvent event = new Event();
    event.setName("Something");
    event.setLocation(true, "Somewhere");
    event.setHost("Prof. Lucia");
    event.setInvitees(invitees);

    // schedule a four-day event prof.lucia has no space for it.
    ReadOnlyEvent scheduledEvent = strategy.scheduleEvent(event, 5760, scheduleList);
    Assert.assertNull(scheduledEvent);

    // schedule a one-day event from Saturday 00:00 to Sunday 00:00, every one can make it.
    scheduledEvent = strategy.scheduleEvent(event, 1440, scheduleList);
    Assert.assertEquals(scheduledEvent.getTime(),
            new Time("Saturday", "0000", "Sunday", "0000"));

    Assert.assertEquals(scheduledEvent.getInvitees(), invitees);
  }
}
