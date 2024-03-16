package schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtilities {
  public static String formatTime(LocalTime time) {
    // Formatter to remove the colon
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

    // Format the time
    return time.format(formatter);
  }

  public static String formatDay(DayOfWeek day) {
    String dayString = day.toString();
    return dayString.charAt(0) + dayString.substring(1).toLowerCase();
  }
}
