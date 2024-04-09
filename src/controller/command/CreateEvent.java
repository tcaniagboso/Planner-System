package controller.command;

import java.util.List;

import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * A command that encapsulates the process of creating a new event in the planner system.
 * It validates input fields from an {@link EventView}, constructs an event, and adds it to
 * the {@link PlannerSystem}.
 */
public class CreateEvent implements Command {

  protected final String userId;
  protected final PlannerSystem model;
  protected final EventView eventView;
  protected final Event event;


  /**
   * Constructs a CreateEvent command with the necessary information to add a new event.
   *
   * @param userId    The ID of the user creating the event.
   * @param model     The planner system model where the event will be added.
   * @param eventView The view capturing user input for the event details.
   * @param event     The event to be added to the planner system. This object may initially
   *                  be partially filled or empty, and is populated based on user input.
   * @throws IllegalArgumentException If any of the required parameters
   *                                  (userId, model, eventView, event) are null or userId is blank,
   *                                  indicating invalid or incomplete command initialization.
   */
  public CreateEvent(String userId, PlannerSystem model, EventView eventView, Event event) {
    if (model == null || eventView == null || event == null) {
      throw new IllegalArgumentException("Invalid Argument(s)");
    }
    if (userId == null || userId.isBlank()) {
      throw new IllegalArgumentException("User ID is null or blank");
    }
    this.userId = userId;
    this.model = model;
    this.eventView = eventView;
    this.event = event;
  }

  /**
   * Executes the process of creating a new event. This method retrieves event details from
   * the {@link EventView}, validates them, and if valid, adds the new event to the planner system.
   * If the event already exists or if any input validation fails, an exception is thrown.
   *
   * @throws IllegalStateException If required fields are empty or if an attempt is made to
   *                               create an event that already exists in the system.
   */
  @Override
  public void execute() {
    try {
      this.eventView.checkFieldsNotEmpty();
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
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
      this.model.createEvent(userId, name, startDay, startTime, endDay, endTime, isOnline, location,
              invitees);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot create event: " + e.getMessage());
    }
  }
}
