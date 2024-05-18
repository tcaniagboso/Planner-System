package schedule;

/**
 * Interface ILocation provides methods to manage and query the location status of an event.
 * It allows checking whether an event is online, toggling the event's mode between online and
 * offline, and managing the physical location details if the event is not online.
 */
public interface ILocation {

  /**
   * Checks if the event is an online event.
   *
   * @return true if the event is online, false otherwise.
   */
  boolean isOnline();

  /**
   * Sets the event's mode to online or offline.
   *
   * @param online A boolean value indicating whether the event is online (true) or not (false).
   */
  void setOnline(boolean online);

  /**
   * Retrieves the physical location of the event. The value is meaningful only if the event is
   * not online.
   *
   * @return A string representing the physical location of the event, or null if the event is
   *         online.
   */
  String getLocation();

  /**
   * Sets the physical location of the event. This value should be set only if the event is not
   * online.
   *
   * @param location A string representing the physical location of the event.
   * @throws IllegalArgumentException if location is null or is an empty string.
   */
  void setLocation(String location);

}
