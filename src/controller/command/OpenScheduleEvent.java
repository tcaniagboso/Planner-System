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
 * It extends the {@link OpenNewEvent} command to leverage the initial setup for opening a new event
 * but specifically targets scheduling by opening a {@link ScheduleEventView}.
 */
public class OpenScheduleEvent extends OpenNewEvent {

  /**
   * Constructs an {@code OpenScheduleEvent} command with the user ID of the current user
   * and the planner system model. The user ID is used to identify the user for whom the
   * event scheduling view will be opened, ensuring the event is scheduled under their name.
   *
   * @param userId The ID of the user who intends to schedule a new event. This ID must not be null.
   * @param model  The planner system model where the new event will be scheduled.
   * @throws IllegalArgumentException if the {@code userId} is null or empty, indicating that a
   *                                  valid user must be specified for the operation.
   */
  public OpenScheduleEvent(String userId, PlannerSystem model) {
    super(userId, model);
  }

  /**
   * Executes the operation to open an event scheduling view for the specified user. This method
   * initializes a {@link ScheduleEventView} with a new {@link Event} and sets it up for the user
   * identified by {@code userId}. This specifically tailored view allows for scheduling
   * functionalities distinct from the generic event creation or modification, emphasizing on
   * finding a suitable time slot for the event.
   * If the {@code userId} indicates that no user is selected (e.g., {@code userId} is "<none>"),
   * an exception is thrown to signal that a valid user selection is required to proceed with
   * scheduling an event.
   *
   * @throws IllegalStateException if no user is selected (userId is "<none>"), enforcing the
   *                               requirement that a valid user must be selected before scheduling
   *                               an event.
   */
  @Override
  public void execute() {
    if (this.userId.equals("<none>")) {
      throw new IllegalStateException("No user is selected");
    }
    Event event = new Event();
    EventView newEvent = new ScheduleEventView(event, userId);
    EventViewController controller = new EventViewControllerImpl(newEvent, model);
    controller.launch(event);
  }
}
