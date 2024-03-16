package schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides utility methods for formatting time-related information.
 * This class includes methods for formatting instances of {@link LocalTime} and {@link DayOfWeek} into strings.
 */
public class TimeUtilities {

  /**
   * Formats a {@link LocalTime} object into a string without colons, in HHmm format.
   * For example, 09:30 will be formatted as "0930".
   *
   * @param time The time to format.
   * @return A string representation of the provided time in HHmm format.
   */
  public static String formatTime(LocalTime time) {
    // Formatter to remove the colon
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

    // Format the time
    return time.format(formatter);
  }

  /**
   * Formats a {@link DayOfWeek} object into a capitalized string.
   * Only the first letter of the day will be in uppercase; the rest will be in lowercase.
   * For example, MONDAY will be formatted as "Monday".
   *
   * @param day The day of the week to format.
   * @return A string representation of the provided day with only the first letter capitalized.
   */
  public static String formatDay(DayOfWeek day) {
    String dayString = day.toString();
    return dayString.charAt(0) + dayString.substring(1).toLowerCase();
  }
}
