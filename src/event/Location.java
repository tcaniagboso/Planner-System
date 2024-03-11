package event;

public class Location {

  private boolean isOnline;
  private String location;


  public boolean isOnline() {
    return isOnline;
  }

  public void setOnline(boolean online) {
    isOnline = online;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean getOnline() {
    return this.isOnline;
  }
}
