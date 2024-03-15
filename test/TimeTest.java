import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;

import schedule.Time;

public class TimeTest {

  private Time time;

  @Before
  public void init() {
    time = new Time();
  }

  @Test
  public void testTimeSettersGetters() {

    // testing start day setting, getting and validation
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartDay("boy"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartDay(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartDay(null));
    time.setStartDay("monday");
    Assert.assertEquals(DayOfWeek.MONDAY, time.getStartDay());

    // testing time validation and setting and getting values
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime("back"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime("12:60"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime("12:100"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setStartTime("12:50"));

    time.setStartTime("1250");
    Assert.assertEquals("12:50", time.getStartTime().toString());

    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay("girl"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay(null));
    time.setEndDay("monday");
    Assert.assertEquals(DayOfWeek.MONDAY, time.getEndDay());

    // test an event that spans 7 days.
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndTime("1250"));
    time.setEndTime("1249");
    Assert.assertEquals("12:49", time.getEndTime().toString());

    // test setting order. Start day -> start time -> end day -> end time
    Time newTime = new Time();
    Assert.assertThrows(IllegalStateException.class, () -> newTime.setStartTime("1250"));
    newTime.setStartDay("Monday");
    Assert.assertThrows(IllegalStateException.class, () -> newTime.setEndDay("wednesday"));
    newTime.setStartTime("1250");
    Assert.assertThrows(IllegalStateException.class, () -> newTime.setEndTime("1300"));
  }

  @Test
  public void testOverlap() {
    time = new Time("monday", "1000", "monday", "1100");
    Time other = new Time("monday", "1000", "monday", "1010");

    Assert.assertTrue(time.overlap(other));
    other.setStartTime("1005"); // tests starts in between an event
    Assert.assertTrue(time.overlap(other));
    other.setStartTime("0905"); // tests end in between an event
    Assert.assertTrue(time.overlap(other));

    time = new Time("monday", "1000", "monday", "1100");
    other = new Time("monday", "1100", "monday", "1200");

    Assert.assertFalse(time.overlap(other)); // tests starts right after an event

    time = new Time("monday", "1000", "wednesday", "1100");
    other = new Time("Tuesday", "1100", "monday", "1200");

    Assert.assertTrue(time.overlap(other)); // tests starts in between a multi day event
    other = new Time("Sunday", "1100", "monday", "1200");
    Assert.assertTrue(time.overlap(other)); // tests ends in between a multi day event

    other.setEndTime("1000");
    time = new Time("monday", "1000", "monday", "0900");
    Assert.assertFalse(time.overlap(other)); // starts before a multi week event

    other = new Time("Monday", "0900", "monday", "1000");
    Assert.assertFalse(time.overlap(other)); // starts right before a multi week event

    other = new Time("Tuesday", "0900", "Tuesday", "0800");
    Assert.assertTrue(time.overlap(other)); // tests if it starts within a multi week event

    other.setStartDay("Sunday");
    Assert.assertTrue(time.overlap(other)); // tests if it continues within a multi week event

  }

  @Test
  public void testEquals() {
    time = new Time("monday", "1000", "monday", "1100");
    Time other = new Time("monday", "1000", "monday", "1100");

    Assert.assertEquals(time, other);
    other.setStartDay("Monday");

    Assert.assertEquals(time, other);

    other.setStartDay("Sunday");
    Assert.assertNotEquals(time, other);
  }
}
