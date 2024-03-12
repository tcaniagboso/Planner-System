package event;

/**
 * Represents the location details of an event, including whether it is an online event
 * and its physical location if applicable.
 */
public class Location {

  private boolean isOnline;
  private String location;


  /**
   * Checks if the event is an online event.
   *
   * @return true if the event is online, false otherwise.
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Sets the event's mode to online or offline.
   *
   * @param online A boolean value indicating whether the event is online (true) or not (false).
   */
  public void setOnline(boolean online) {
    isOnline = online;
  }

  /**
   * Retrieves the physical location of the event. The value is meaningful only if the event is
   * not online.
   *
   * @return A string representing the physical location of the event, or null if the event is
   * online.
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the physical location of the event. This value should be set only if the event is not
   * online.
   *
   * @param location A string representing the physical location of the event.
   */
  public void setLocation(String location) {
    this.location = location;
  }

}
