package plannersystem;

import schedule.Schedule;

/**
 * The ReadonlyPlannerSystem interface defines a read-only view of the PlannerSystem.
 * It provides methods for accessing the state of the planner system without modifying it.
 * This interface is designed to be used in contexts where only data retrieval is needed,
 * ensuring that the underlying model cannot be altered.
 */
public interface ReadonlyPlannerSystem {

  /**
   * Generates and returns a string representation of a user's schedule, formatted for display.
   * This method allows viewing the details of a user's schedule without modifying it.
   *
   * @param userId The ID of the user whose schedule is to be displayed.
   * @return A string representation of the user's schedule.
   * @throws IllegalArgumentException if the specified user does not exist.
   */
  String displayUserSchedule(String userId);

  /**
   * Searches for and displays information about an event occurring at a specified time and day
   * for a given user. This method provides a way to query the system for events without
   * changing any data.
   *
   * @param userId The ID of the user whose schedule is being queried.
   * @param day    The day of the event.
   * @param time   The time of the event.
   * @return A string indicating whether an event exists at the specified time and its details,
   *         or a message stating no event exists at that time.
   * @throws IllegalArgumentException if the specified user does not exist or if day/time
   *                                  parameters are null.
   */
  String showEvent(String userId, String day, String time);

  /**
   * Retrieves the schedule for a specified user. This method provides read-only access to the
   * schedule of a user, allowing for the inspection of scheduled events without the ability
   * to modify them.
   *
   * @param userId The ID of the user whose schedule is requested.
   * @return The {@link Schedule} associated with the given user ID.
   * @throws IllegalArgumentException if no schedule exists for the specified user.
   */
  Schedule getSchedule(String userId);
}
