package schedulestrategy;

import java.util.ArrayList;
import java.util.List;

import schedule.IEvent;
import schedule.ISchedule;

/**
 * Extends WorkHourSchedule to implement a lenient scheduling strategy.
 * This strategy attempts to schedule events even if not all invitees are available,
 * requiring only the host and at least one other invitee to be available.
 */
public class LenientScheduleStrategy extends WorkHourScheduleStrategy {

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
  protected boolean validateTime(IEvent event, List<ISchedule> scheduleList) {
    List<String> availableUsers = new ArrayList<>();
    for (ISchedule schedule : scheduleList) {
      // Check if the event does not overlap with this schedule
      if (!schedule.overlap(event, firstDayOfWeek)) {
        availableUsers.add(schedule.getUserName());
      }
    }
    // Ensure both the host is available and at least one other user
    boolean isHostAvailable = availableUsers.contains(event.getHost());
    if (isHostAvailable && availableUsers.size() > 1) {
      event.setInvitees(availableUsers);
      return true;
    }
    return false;
  }
}
