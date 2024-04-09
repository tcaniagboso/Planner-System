package controller.command;

import controller.EventViewController;
import controller.EventViewControllerImpl;
import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;
import view.ScheduleEventView;

/**
 * A command for opening an event scheduling view for a specific user. This command initializes
 * an {@link EventView} specifically {@link ScheduleEventView} with a new {@link Event} object
 * and sets it up for the specified user, allowing the user to schedule a new event.
 * It extends the {@link OpenEventFrame} command to leverage the initial setup for opening a new event
 * but specifically targets scheduling by opening a {@link ScheduleEventView}.
 */
public class OpenScheduleEvent extends OpenEventFrame {

  /**
   * Constructs an {@code OpenScheduleEvent} command with the user ID of the current user
   * and the planner system model. The user ID is used to identify the user for whom the
   * event scheduling view will be opened, ensuring the event is scheduled under their name.
   *
   * @param userId The ID of the user who intends to schedule a new event. This ID must not be null.
   * @param event The event to be managed. This is usually an empty event.
   * @param model  The planner system model where the new event will be scheduled.
   * @throws IllegalArgumentException if the {@code userId} is null or empty, indicating that a
   *                                  valid user must be specified for the operation.
   */
  public OpenScheduleEvent(String userId, Event event, PlannerSystem model) {
    super(userId, event, model);
    this.eventView = new ScheduleEventView(this.event, this.userId);
    this.controller = new EventViewControllerImpl(this.eventView, this.model);
  }
}
