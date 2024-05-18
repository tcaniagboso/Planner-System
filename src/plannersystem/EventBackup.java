package plannersystem;

import java.util.ArrayList;
import java.util.List;

import schedule.ReadOnlyEvent;
import schedule.TimeUtilities;

/**
 * The {@code EventBackup} class is used to create a backup of an {@link ReadOnlyEvent}'s state.
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
  private final String startDay;
  private final String startTime;
  private final String endDay;
  private final String endTime;
  private final String name;
  private final String host;
  private final String place;
  private final boolean isOnline;
  private final List<String> invitees;

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
  EventBackup(ReadOnlyEvent event) {
    // Backup current state
    this.startDay = TimeUtilities.formatDay(event.getStartDay());
    this.startTime = String.format("%04d", event.getStartTime());
    this.endDay = TimeUtilities.formatDay(event.getEndDay());
    this.endTime = String.format("%04d", event.getEndTime());
    this.name = event.getName();
    this.host = event.getHost();
    this.place = event.getLocation();
    this.isOnline = event.isOnline();
    this.invitees = new ArrayList<>(event.getInvitees());
  }

  /**
   * Gets the backup start day of the event.
   * @return The start day of the event.
   */
  public String getStartDay() {
    return startDay;
  }

  /**
   * Gets the backup start time of the event.
   * @return The start time of the event.
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * Gets the backup end day of the event.
   * @return The end day of the event.
   */
  public String getEndDay() {
    return endDay;
  }

  /**
   * Gets the backup end time of the event.
   * @return The end time of the event.
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * Gets the backup name of the event.
   * @return The name of the event.
   */
  public String getName() {
    return name;
  }

  /**
   * Checks if the event was online.
   * @return {@code true} if the event was online, {@code false} otherwise.
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Gets the backup location of the event.
   * @return The location of the event.
   */
  public String getPlace() {
    return place;
  }

  /**
   * Gets the backup host of the event.
   * @return The host of the event.
   */
  public String getHost() {
    return host;
  }

  /**
   * Gets a copy of the invitees list from the backup.
   * @return A list of invitees.
   */
  public List<String> getInvitees() {
    List<String> invitedList = new ArrayList<>();
    for (String user: this.invitees) {
      invitedList.add(user);
    }
    return invitedList;
  }
}
