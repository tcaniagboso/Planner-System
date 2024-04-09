import java.util.List;

import schedule.Event;
import schedule.Location;
import schedule.Time;

public class MockEvent extends Event {

  private final StringBuilder log;

  public MockEvent(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null.");
    }
    this.log = log;
  }

  @Override
  public String getName() {
    this.log.append("This method gets the name of the event.").append(System.lineSeparator());
    return null;
  }

  @Override
  public void setName(String name) {
    this.log.append("This method sets the name of the event.").append(System.lineSeparator());
  }

  @Override
  public Time getTime() {
    this.log.append("This method gets the Time of the event.").append(System.lineSeparator());
    return null;
  }


  @Override
  public void setEventTimes(String startDay, String startTime, String endDay, String endTime) {
    this.log.append("This method sets the Time of the event.").append(System.lineSeparator());
  }

  @Override
  public void setLocation(boolean isOnline, String location) {
    this.log.append("This method sets the location of the event.").append(System.lineSeparator());
  }

  @Override
  public Location getLocation() {
    this.log.append("This method gets the Location of the event.").append(System.lineSeparator());
    return null;
  }

  @Override
  public List<String> getInvitees() {
    this.log.append("This method gets the invitees of the event.").append(System.lineSeparator());
    return null;
  }

  @Override
  public void setInvitees(List<String> invitees) {
    this.log.append("This method sets the invitees of the event.").append(System.lineSeparator());
  }

  @Override
  public String getHost() {
    this.log.append("This method gets the host of the event.").append(System.lineSeparator());
    return null;
  }

  @Override
  public void setHost(String host) {
    this.log.append("This method sets the host of the event.").append(System.lineSeparator());
  }

  @Override
  public void addInvitee(String invitee) {
    this.log.append("This method adds an invitee to the event.").append(System.lineSeparator());
  }

  @Override
  public void removeInvitee(String invitee) {
    this.log.append("This method removes an invitee from the event.")
            .append(System.lineSeparator());
  }

  @Override
  public void clearInvitees() {
    this.log.append("This method clears the invitees of the event.").append(System.lineSeparator());
  }

  @Override
  public boolean overlap(Event event) {
    this.log.append("This method returns true if an event overlaps with this event, ")
            .append("otherwise false").append(System.lineSeparator());
    return false;
  }

  @Override
  public boolean occurs(String day, String time) {
    this.log.append("This method returns true if an event occurs at the given day and time, ")
            .append("otherwise false").append(System.lineSeparator());
    return false;
  }

  @Override
  public boolean wrapsAround() {
    this.log.append("This method returns true if an event extends to a new week, ")
            .append("otherwise false").append(System.lineSeparator());
    return false;
  }
}
