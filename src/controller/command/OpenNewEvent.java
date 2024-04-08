package controller.command;

import controller.EventViewController;
import controller.EventViewControllerImpl;
import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;
import view.EventViewImpl;

/**
 * A command that facilitates the opening of a view for creating a new event in the planner system.
 * It initializes an empty event and an associated view, and uses an event controller to present
 * the view to the user, allowing for the input of event details.
 */
public class OpenNewEvent implements Command {

  protected final String userId;
  protected final PlannerSystem model;

  /**
   * Constructs an {@code OpenNewEvent} command with the specified user ID and planner system model.
   * This setup is intended for creating a new event associated with the given user.
   *
   * @param userId The ID of the user who is creating the new event. This ID may be used to
   *               ensure the new event is associated with the correct user in the system.
   * @param model  The planner system model that provides the functionality for event management,
   *               including adding the newly created event to the system.
   * @throws IllegalArgumentException if the userId is null, empty, or only whitespace, or if
   *                                  the model is null, indicating that valid and meaningful
   *                                  arguments are required for the operation.
   */
  public OpenNewEvent(String userId, PlannerSystem model) {
    if (userId == null || userId.isBlank() || model == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.userId = userId;
    this.model = model;
  }

  /**
   * Executes the operation to create and display a new event. This method initializes an
   * {@link EventView} with an empty {@link Event} and launches it via an
   * {@link EventViewController}. The view allows the user to enter details for the new event,
   * which can then be saved to the planner system.
   * If no user is selected (userId is "<none>"), an exception is thrown to indicate that
   * a user must be selected before creating a new event.
   *
   * @throws IllegalStateException if no user is selected for creating the new event, indicated
   *                               by the userId being "<none>".
   */
  @Override
  public void execute() {
    if (this.userId.equals("<none>")) {
      throw new IllegalStateException("No user is selected");
    }
    Event event = new Event();
    EventView newEvent = new EventViewImpl(event, userId);
    EventViewController controller = new EventViewControllerImpl(newEvent, model);
    controller.launch(event);
  }
}
