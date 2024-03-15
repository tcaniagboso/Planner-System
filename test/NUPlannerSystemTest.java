import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import plannersystem.NUPlannerSystem;
import plannersystem.PlannerSystem;
import schedule.Schedule;

public class NUPlannerSystemTest {

  private PlannerSystem system;

  @Before
  public void init() {
    system = new NUPlannerSystem();
  }

  @Test
  public void testReadUserSchedule() {
    Assert.assertThrows(IllegalArgumentException.class, () -> system.readUserSchedule(null));
    String fileName = "prof.xml";
    File file = new File(Paths.get("", fileName).toUri());
    System.out.println(file.getAbsolutePath());
    system.readUserSchedule(file);
    Schedule schedule = system.getSchedule("Prof. Lucia");

    Assert.assertNotNull(schedule);
  }
}
