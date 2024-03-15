package schedule;

import java.util.Objects;

import validationutilities.ValidationUtilities;

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
    ValidationUtilities.validateGetNull(this.location);
    return location;
  }

  /**
   * Sets the physical location of the event. This value should be set only if the event is not
   * online.
   *
   * @param location A string representing the physical location of the event.
   * @throws IllegalArgumentException if location is null or is an empty string.
   */
  public void setLocation(String location) {
    if (location == null || location.isEmpty()) {
      throw new IllegalArgumentException("Invalid location");
    }
    this.location = location;
  }

  /**
   * Indicates whether some other object is "equal to" this one. The equality is based
   * on the online status and physical location of the event.
   *
   * @param object the reference object with which to compare.
   * @return true if this object is the same as the object argument; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Location)) {
      return false;
    }
    Location other = (Location) object;

    return this.isOnline == other.isOnline() && this.location.equals(other.getLocation());
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit
   * of hash tables such as those provided by {@link java.util.HashMap}.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.isOnline, this.location);
  }

}
