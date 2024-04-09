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
public class OpenEventFrame implements Command {

  protected final String userId;
  protected final Event event;
  protected final PlannerSystem model;
  protected EventView eventView;
  protected EventViewController controller;

  /**
   * Constructs an {@code OpenNewEvent} command with the specified user ID and planner system model.
   * This setup is intended for creating a new event associated with the given user.
   *
   * @param userId The ID of the user who is creating the new event. This ID may be used to
   *               ensure the new event is associated with the correct user in the system.
   * @param event  The event object to be managed. Can be a new or existing event.
   * @param model  The planner system model that provides the functionality for event management,
   *               including adding the newly created event to the system.
   * @throws IllegalArgumentException if the userId is null, empty, or only whitespace, or if
   *                                  the model is null, indicating that valid and meaningful
   *                                  arguments are required for the operation.
   */
  public OpenEventFrame(String userId, Event event, PlannerSystem model) {
    if (userId == null || userId.isBlank() || event == null || model == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.userId = userId;
    this.event = event;
    this.model = model;
    this.eventView = new EventViewImpl(this.event, this.userId);
    controller = new EventViewControllerImpl(eventView, model);
  }


  /**
   * Executes the operation to create and display an event frame. This method initializes an
   * {@link EventView} with an empty or existing {@link Event} and launches it via an
   * {@link EventViewController}. The view allows the user to enter details for the event,
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
    controller.launch(event);
  }

  /**
   * Sets the {@link EventViewController} for this command. This method is primarily intended
   * for testing purposes, allowing the injection of a mock or stub {@link EventViewController}
   * to simulate different behaviors of the controller without requiring the actual implementation.
   *
   * <p></p>This method can be used to inject dependencies after the command has been constructed,
   * providing a way to modify the command's behavior, especially during unit testing.
   * It allows tests to verify the interaction between the command and its controller without the
   * need to instantiate the actual controller implementations.
   *
   * @param controller The {@link EventViewController} to be used by this command. This can be a
   *                   mock implementation for testing purposes. Must not be {@code null}.
   * @throws IllegalArgumentException if the controller is {@code null}, as the command requires a
   *                                  non-null controller to function properly.
   */
  public void setController(EventViewController controller) {
    this.controller = controller;
  }

  /**
   * Sets the {@link EventView} for this command. This setter method is primarily used for testing
   * purposes, allowing the injection of a mock or stub {@link EventView} to simulate user
   * interactions and view updates without the need for the actual view implementation.
   * This approach is particularly useful for testing the command's behavior in response to view
   * events or to verify that the command triggers the correct updates on the view.
   * By using this method, tests can more easily mock user inputs and view behaviors, facilitating
   * the isolation of the command logic from the UI for more focused and reliable testing. It allows
   * for the verification of the interaction between the command and its view without relying on the
   * concrete implementation of the view, which can often involve complex UI behavior and state.
   *
   * @param eventView The {@link EventView} to be used by this command, typically a mock or stub
   *                  for testing purposes. This parameter allows the test to control the view's
   *                  behavior and state, ensuring that tests are not affected by the real
   *                  implementation details of the view.
   * @throws IllegalArgumentException if {@code eventView} is {@code null}, as the command relies
   *                                  on a valid, non-null view to operate correctly and to ensure
   *                                  that view operations can be simulated accurately in tests.
   */
  public void setEventView(EventView eventView) {
    this.eventView = eventView;
  }
}
