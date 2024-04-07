package autoscheduling;

import java.util.ArrayList;
import java.util.List;

import schedule.Event;
import schedule.Schedule;

/**
 * Extends WorkHourSchedule to implement a lenient scheduling strategy.
 * This strategy attempts to schedule events even if not all invitees are available,
 * requiring only the host and at least one other invitee to be available.
 */
public class LenientSchedule extends WorkHourSchedule {

  /**
   * Validates event timing with a lenient approach. Checks if the event does not overlap
   * with the schedules of the host and at least one other user.
   *
   * @param event        The event to be scheduled.
   * @param scheduleList A list of schedules against which to check the event.
   * @return true if the event can be scheduled with the host and at least one other user available,
   *         false otherwise.
   */
  @Override
  protected boolean validateTime(Event event, List<Schedule> scheduleList) {
    List<String> availableUsers = new ArrayList<>();
    for (Schedule schedule : scheduleList) {
      // Check if the event does not overlap with this schedule
      if (!schedule.overlap(event)) {
        availableUsers.add(schedule.getUserId());
      }
    }
    // Ensure both the host is available and at least one other user
    boolean isHostAvailable = availableUsers.contains(event.getHost());
    return isHostAvailable && availableUsers.size() > 1;
  }
}
