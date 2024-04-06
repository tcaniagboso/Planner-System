package command;

import java.util.List;

import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * A command that encapsulates the action of removing an existing event from the planner system.
 * It extends {@link CreateEvent} to reuse the logic for validating event fields and ensures
 * that the event to be removed exists within the system before attempting its removal.
 */
public class RemoveEvent extends CreateEvent {

  /**
   * Constructs a {@code RemoveEvent} command with the specified user ID, planner system model,
   * event view, and the event to be removed.
   *
   * @param userId    The ID of the user attempting to remove the event.
   * @param model     The planner system model from which the event will be removed.
   * @param eventView The view capturing user input for the event details. This view is used
   *                  to validate that required fields are not empty before attempting removal.
   * @param event     The event that is targeted for removal.
   * @throws IllegalArgumentException if any of the required parameters (model, eventView, event)
   *                                  are null or if the userId is invalid, indicating that valid
   *                                  and meaningful arguments are required for the operation.
   */
  public RemoveEvent(String userId, PlannerSystem model, EventView eventView, Event event) {
    super(userId, model, eventView, event);
  }

  /**
   * Executes the operation to remove an existing event. This method validates that the event
   * fields are not empty and checks if the event is indeed an existing event within the system
   * before proceeding with its removal.
   * <p>
   * If the event does not exist (i.e., it is considered as a new event) or if any validation
   * fails, an exception is thrown to prevent the removal of a non-existing event.
   *
   * @throws IllegalStateException If the event is considered new (does not exist in the system)
   *                               or if required fields are empty, indicating that removal cannot
   *                               proceed.
   */
  @Override
  public void execute() {
    try {
      this.eventView.checkFieldsNotEmpty();
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
    }
    if (isNewEvent()) {
      throw new IllegalStateException("Cannot remove a new event.");
    }

    String name = this.eventView.getEventName();
    String startDay = this.eventView.getStartDay();
    String startTime = this.eventView.getStartTime();
    String endDay = this.eventView.getEndDay();
    String endTime = this.eventView.getEndTime();
    String location = this.eventView.getEventLocation();
    boolean isOnline = this.eventView.getOnline();
    List<String> invitees = this.eventView.getInvitees();

    Event curEvent = new Event();
    curEvent.setName(name);
    curEvent.setEventTimes(startDay, startTime, endDay, endTime);
    curEvent.setLocation(isOnline, location);
    curEvent.setHost(invitees.get(0));
    curEvent.setInvitees(invitees);
    try {
      this.model.removeEvent(userId, curEvent);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot remove event: " + e.getMessage());
    }
  }

}
