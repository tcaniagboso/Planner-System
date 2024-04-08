package controller.command;

import java.util.List;

import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * A command that encapsulates the process of modifying an existing event in the planner system.
 * This class extends {@link CreateEvent} to leverage common functionality for event data
 * validation and retrieval from an {@link EventView}. The {@code execute} method is overridden
 * to update an existing event rather than creating a new one.
 */
public class ModifyEvent extends CreateEvent {

  /**
   * Constructs a ModifyEvent command with the necessary information to modify an existing event.
   *
   * @param userId    The ID of the user modifying the event.
   * @param model     The planner system model where the event exists and will be modified.
   * @param eventView The view capturing user input for the event details.
   * @param event     The existing event to be modified with new details.
   * @throws IllegalArgumentException If any of the required parameters (model, eventView, event)
   *                                  are null, indicating invalid or incomplete command
   *                                  initialization.
   */
  public ModifyEvent(String userId, PlannerSystem model, EventView eventView, Event event) {
    super(userId, model, eventView, event);
  }

  /**
   * Executes the process of modifying an existing event. This method retrieves new event details
   * from the {@link EventView}, validates them, and if valid, updates the existing event in the
   * planner system.
   * If the event is identified as new (i.e., it does not exist in the system) or if any input
   * validation fails, an exception is thrown. This ensures only existing events are modified
   * and that modifications meet the system's requirements for event details.
   *
   * @throws IllegalStateException If required fields are empty, or if an attempt is made to
   *                               modify a non-existing event in the system.
   */
  @Override
  public void execute() {
    try {
      this.eventView.checkFieldsNotEmpty();
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
    }
    if (isNewEvent()) {
      throw new IllegalStateException("Cannot modify a new event.");
    }

    try {
      String name = this.eventView.getEventName();
      String startDay = this.eventView.getStartDay();
      String startTime = this.eventView.getStartTime();
      String endDay = this.eventView.getEndDay();
      String endTime = this.eventView.getEndTime();
      String location = this.eventView.getEventLocation();
      boolean isOnline = this.eventView.getOnline();
      List<String> invitees = this.eventView.getInvitees();
      this.model.modifyEvent(userId, event, name, startDay, startTime, endDay, endTime, isOnline,
              location, invitees);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot modify event: " + e.getMessage());
    }
  }
}
