package provider.model;

import java.util.ArrayList;
import java.util.List;

import plannersystem.PlannerSystem;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedule.TimeUtilities;

/**
 * The NUPlannerSystemAdapter class adapts the PlannerSystem to the ICentralSystem interface,
 * allowing for the management of user schedules and events within a central system.
 */
public class NUPlannerSystemAdapter implements ICentralSystem {

  private final PlannerSystem plannerSystem;

  /**
   * Constructs a new NUPlannerSystemAdapter with a specified PlannerSystem.
   *
   * @param plannerSystem the PlannerSystem to be adapted, must not be null.
   * @throws IllegalArgumentException if the provided PlannerSystem is null.
   */
  public NUPlannerSystemAdapter(PlannerSystem plannerSystem) {
    if (plannerSystem == null) {
      throw new IllegalArgumentException("Planner System is null");
    }

    this.plannerSystem = plannerSystem;
  }

  @Override
  public void addSchedule(String userName) {
    this.plannerSystem.addUser(userName);
  }

  @Override
  public boolean removeSchedule(String userName) {
    return this.plannerSystem.removeUser(userName);
  }

  @Override
  public void createNewEvent(ReadOnlyEvent event) {
    String name = event.getName();
    String startDay = TimeUtilities.formatDay(event.getStartDay());
    String startTime = TimeUtilities.formatTime(event.getTime().getStartTime());
    String endDay = TimeUtilities.formatDay(event.getEndDay());
    String endTime = TimeUtilities.formatTime(event.getTime().getEndTime());
    String location = event.getLocation();
    boolean isOnline = event.isOnline();
    String host = event.getHost();
    List<String> invitees = event.getInvitees();
    this.plannerSystem.createEvent(host, name, startDay, startTime, endDay, endTime, isOnline,
            location, invitees);
  }

  @Override
  public void hostDeleteEvent(String userName, ReadOnlyEvent event) {
    if (!userName.equals(event.getHost())) {
      throw new IllegalArgumentException("Only the host of the event can delete the event");
    }
    this.plannerSystem.removeEvent(userName, event);
  }

  @Override
  public void modifyEvent(ReadOnlyEvent oldEvent, ReadOnlyEvent updateEvent) {
    String name = updateEvent.getName();
    String startDay = TimeUtilities.formatDay(updateEvent.getStartDay());
    String startTime = String.format("%04d", updateEvent.getStartTime());
    String endDay = TimeUtilities.formatDay(updateEvent.getEndDay());
    String endTime = String.format("%04d", updateEvent.getEndTime());
    String location = updateEvent.getLocation();
    boolean isOnline = updateEvent.isOnline();
    String host = updateEvent.getHost();
    List<String> invitees = updateEvent.getInvitees();

    this.plannerSystem.modifyEvent(host, oldEvent, name, startDay, startTime, endDay, endTime,
            isOnline, location, invitees);
  }

  @Override
  public void addFullSchedule(ISchedule newSchedule) {
    this.plannerSystem.addSchedule(newSchedule);
  }

  @Override
  public List<String> allUsersInSystem() {
    return new ArrayList<>(this.plannerSystem.getUsers());
  }

  @Override
  public boolean conflictingEvent(ReadOnlyEvent event) {
    return this.plannerSystem.checkEventConflict(event);
  }

  @Override
  public List<ReadOnlyEvent> eventList(String userName) {
    return this.plannerSystem.getSchedule(userName).getEvents();
  }

  @Override
  public ISchedule getUserSchedule(String userName) {
    return this.plannerSystem.getSchedule(userName);
  }

  /**
   * Returns a reference to the underlying PlannerSystem.
   * Warning: Exposing this internal component can lead to tight coupling and
   * makes it harder to maintain the encapsulation principles of OOP.
   *
   * @return A reference to the internal PlannerSystem.
   */
  public PlannerSystem getPlannerSystem() {
    return this.plannerSystem;
  }
}
