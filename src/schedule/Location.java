package schedule;

import java.util.Objects;

import validationutilities.ValidationUtilities;

/**
 * Represents the location details of an event, including whether it is an online event
 * and its physical location if applicable.
 */
public class Location implements ILocation {

  private boolean isOnline;
  private String location;

  @Override
  public boolean isOnline() {
    return isOnline;
  }

  @Override
  public void setOnline(boolean online) {
    isOnline = online;
  }

  @Override
  public String getLocation() {
    ValidationUtilities.validateGetNull(this.location);
    return location;
  }

  @Override
  public void setLocation(String location) {
    if (location == null || location.isBlank()) {
      throw new IllegalArgumentException("Invalid location");
    }
    this.location = location.trim();
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
