package controller.command;

import controller.EventViewController;
import controller.EventViewControllerImpl;
import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;
import view.EventViewImpl;

/**
 * A command for opening and displaying the details of an existing event. This command
 * initializes the view for an event and launches it through an event controller, allowing
 * the user to view or modify the event's details.
 */
public class OpenExistingEvent implements Command {

  private final String userId;
  private final Event event;

  private final PlannerSystem model;

  /**
   * Constructs an {@code OpenExistingEvent} command with the specified user ID, event, and
   * planner system model. This setup enables the command to open an existing event for viewing
   * or modification.
   *
   * @param userId The ID of the user who is opening the event. This ID is used for any
   *               permissions checks or user-specific logic within the event view.
   * @param event  The event to be opened. This is the existing event whose details are to
   *               be displayed in the event view.
   * @param model  The planner system model, providing data and functionality related to
   *               event management. This may be used for further event operations such as
   *               saving changes.
   * @throws IllegalArgumentException if any of the parameters are null or if the userId is
   *                                  an empty string, indicating that valid arguments are
   *                                  required for operation.
   */
  public OpenExistingEvent(String userId, Event event, PlannerSystem model) {
    if (event == null || userId == null || model == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid Arguments");
    }
    this.userId = userId;
    this.event = event;
    this.model = model;
  }

  /**
   * Executes the operation to open and display the existing event. This involves initializing
   * an {@link EventView} with the event's details and launching it via an
   * {@link EventViewController}.
   * This setup allows the user to interact with the event's details, potentially modifying them
   * through the provided view and controller.
   */
  @Override
  public void execute() {
    EventView existingEvent = new EventViewImpl(this.event, userId);
    EventViewController controller = new EventViewControllerImpl(existingEvent, model);
    controller.launch(this.event);
  }
}
