package scheduleview;

/**
 * The ScheduleView interface defines a method for rendering a schedule into a string
 * representation.
 */
public interface ScheduleView {


  /**
   * Renders the schedule of a specified user into a string representation.
   * The method retrieves the user's schedule from the PlannerSystem, sorts
   * it, and formats it into a readable string that lists events by day of
   * the week, including details for each event.
   *
   * <p>Event details include the event name, start and end times (with days),
   * location (with a distinction between physical and online locations),
   * and a list of invitees. Times are formatted using the TimeUtilities class.</p>
   *
   * @param user the identifier of the user whose schedule is to be rendered;
   *             must not be null.
   * @return a string representation of the user's schedule, formatted by day
   * of the week with details for each event.
   * @throws IllegalArgumentException if the user argument is null.
   */
  String render(String user);
}
