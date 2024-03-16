package scheduleview;

import schedule.Schedule;

/**
 * The ScheduleView interface defines a method for rendering a schedule into a string
 * representation.
 */
public interface ScheduleView {

  /**
   * Renders the provided Schedule object into a String format that includes
   * details about each event organized by day of the week.
   *
   * <p>The method iterates over each day of the week and for each day, lists all
   * events occurring on that day along with their details such as name, time,
   * location, online status, and invitees. Events are sorted prior to rendering.
   *
   * @param schedule The Schedule object to be rendered.
   * @return A human-readable String representation of the Schedule, organized
   * by day of the week and listing details of each event.
   * @throws IllegalArgumentException if the provided Schedule is null.
   */
  String render(Schedule schedule);
}
