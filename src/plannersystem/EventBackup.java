package plannersystem;

import java.util.ArrayList;
import java.util.List;

import schedule.Event;
import schedule.TimeUtilities;

/**
 * The {@code EventBackup} class is used to create a backup of an {@link Event}'s state.
 * This includes all pertinent details of the event such as start day, start time,
 * end day, end time, name, host, place, online status, and the list of invitees.
 * The purpose of this class is to facilitate operations that may require rolling back
 * to the original state of an event, such as during modifications that are not successfully
 * completed.
 *
 * <p>Backup data is captured at the moment of {@code EventBackup} object creation and includes
 * deep copies of mutable objects to ensure the backup is not affected by changes to the original
 * {@code Event} object after the backup is made.</p>
 */
class EventBackup {
  String startDay;
  String startTime;
  String endDay;
  String endTime;
  String name;
  String host;
  String place;
  boolean isOnline;
  List<String> invitees;

  /**
   * Constructs an {@code EventBackup} object capturing the current state of the specified
   * {@code Event}.
   * The state includes event timing, identifying information, location
   * (both physical and online status), and the list of invitees.
   * This constructor uses utility methods to format date and time for consistency in
   * representation.
   *
   * @param event The {@code Event} from which to back up information. It is assumed
   *              that the event is fully initialized and contains valid data. The event object
   *              itself is not modified by this operation.
   */
  EventBackup(Event event) {
    // Backup current state
    this.startDay = TimeUtilities.formatDay(event.getTime().getStartDay());
    this.startTime = TimeUtilities.formatTime(event.getTime().getStartTime());
    this.endDay = TimeUtilities.formatDay(event.getTime().getEndDay());
    this.endTime = TimeUtilities.formatTime(event.getTime().getEndTime());
    this.name = event.getName();
    this.host = event.getHost();
    this.place = event.getLocation().getLocation();
    this.isOnline = event.getLocation().isOnline();
    this.invitees = new ArrayList<>(event.getInvitees());
  }
}
