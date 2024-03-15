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


    time.setStartTime("1250");
    Assert.assertEquals("12:50", time.getStartTime().toString());

    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay("girl"));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndDay(null));
    time.setEndDay("monday");
    Assert.assertEquals(DayOfWeek.MONDAY, time.getEndDay());

    Assert.assertThrows(IllegalArgumentException.class, () -> time.setEndTime("1250"));

    Time newTime = new Time();
  }


}
