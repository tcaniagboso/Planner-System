package controller;

import controller.command.AddCalendar;
import controller.command.Command;
import controller.command.CreateEvent;
import controller.command.ModifyEvent;
import controller.command.RemoveEvent;
import controller.command.SaveCalendars;
import controller.command.ScheduleEvent;
import controller.command.SelectUser;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.ReadOnlyEvent;
import view.EventView;
import view.EventViewImpl;
import view.PlannerSystemView;
import view.ScheduleEventView;

/**
 * Controller class for managing user actions and mouse events in the Planner System application.
 * This class listens for actions performed on the GUI and responds to mouse clicks on the schedule
 * panel.
 * It bridges the interaction between the view ({@link PlannerSystemView}) and the model
 * ({@link PlannerSystem}).
 */
public class ScheduleViewController implements PlannerSystemController {

  private final PlannerSystemView view;
  private PlannerSystem model;

  private EventView eventView;

  /**
   * Constructs a ScheduleViewController with a specified view.
   * Initializes the controller, sets itself as the listener for action and mouse events.
   *
   * @param view The {@link PlannerSystemView} for this controller to manage.
   * @throws IllegalArgumentException if the provided view is null.
   */
  public ScheduleViewController(PlannerSystemView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    this.view.setActionListener(this);
  }

  @Override
  public void launch(PlannerSystem model) {
    this.setModel(model);
    this.view.makeVisible();
  }

  @Override
  public void processButtonPress(String action) {
    String currentUser = this.view.getCurrentUser();
    assert currentUser != null;
    Command command = this.handleCommands(action, currentUser);
    if (command != null) {
      try {
        command.execute();
      } catch (Exception ise) {
        this.view.displayErrorMessage(ise.getMessage());
      }
    }
    this.view.refresh();
  }

  /**
   * Handles various command actions based on the input action string and current user context.
   * This method routes the action to specific command implementations or UI actions,
   * creating and executing appropriate Command objects or triggering UI changes directly.
   *
   * @param action      The string representing the action command triggered by the user. This
   *                    should match one of the predefined actions such as "Open Event Frame",
   *                    "Save calendars", etc.
   * @param currentUser The username of the currently active user, used to customize command
   *                    behavior based on user context.
   * @return A Command object appropriate to the action, or null if the action does not directly
   *         correlate to a command pattern (e.g., actions that directly manipulate the UI or
   *         initiate other procedures).
   */
  private Command handleCommands(String action, String currentUser) {
    Command command;
    switch (action) {
      case "Open Event Frame":
        command = null;
        this.launchEventView(currentUser, new Event(), "Standard");
        break;
      case "Open Schedule Event Frame":
        command = null;
        this.launchEventView(currentUser, new Event(), "Schedule");
        break;
      case "Add calendar":
        command = new AddCalendar(view, model);
        break;
      case "Save calendars":
        command = new SaveCalendars(currentUser, view, model);
        break;
      case "Select user":
        command = new SelectUser(currentUser, view);
        break;
      case "Create event":
        command = new CreateEvent(currentUser, model, eventView, eventView.getEvent());
        break;
      case "Modify event":
        command = new ModifyEvent(currentUser, model, eventView, eventView.getEvent());
        break;
      case "Remove event":
        command = new RemoveEvent(currentUser, model, eventView, eventView.getEvent());
        break;
      case "Schedule event":
        command = new ScheduleEvent(currentUser, model, eventView, eventView.getEvent());
        break;
      case "Toggle Color":
        command = null;
        this.handleToggleColor(currentUser);
        break;
      default:
        command = null;
    }
    return command;
  }

  @Override
  public void processMouseClick(ReadOnlyEvent event) {
    String userId = this.view.getCurrentUser();
    this.launchEventView(userId, event, "Standard");
  }

  @Override
  public void setEventView(EventView view) {
    this.eventView = view;
  }

  @Override
  public void update() {
    this.view.updateUsers();
    this.view.refresh();
  }

  /**
   * Sets the model for this controller.
   *
   * @param model The {@link PlannerSystem} model to be set.
   * @throws IllegalArgumentException if the provided model is null.
   */
  private void setModel(PlannerSystem model) {
    if (model == null) {
      throw new IllegalArgumentException("Planner System Model is null");
    }
    this.model = model;
    this.model.addObserver(this);
  }

  /**
   * Launches an event view based on the specified user ID, event, and frame type. Displays an error
   * message if an event frame is already open or if the user selected is "none".
   *
   * @param userId    The ID of the user associated with the event view.
   * @param event     The event to be displayed in the event view.
   * @param frameType The type of frame to be launched ("Schedule" or any other value).
   */
  private void launchEventView(String userId, ReadOnlyEvent event, String frameType) {
    if (eventView != null && eventView.isViewVisible()) {
      view.displayErrorMessage("An event frame is already open.");
      return;
    }
    if (userId.equals("<none>")) {
      view.displayErrorMessage("No user is selected.");
      return;
    }
    if (frameType.equals("Schedule")) {
      this.eventView = new ScheduleEventView(event, userId);
    } else {
      this.eventView = new EventViewImpl(event, userId);
    }
    this.eventView.setActionListener(this);
    this.eventView.makeVisible();
  }

  /**
   * Toggles the color theme or color settings in the user interface based on the specified user's
   * action. This method checks if a valid user (not "none") is provided before toggling the color
   * settings to ensure that the operation is meaningful in a user-specific context.
   *
   * @param userId The identifier for the current user, used to determine if the toggle operation
   *               should proceed. The method performs the toggle only if the userId is not
   *               "none", which typically represents a lack of user selection or a default non-user
   *               state.
   */
  private void handleToggleColor(String userId) {
    if (!userId.equals("<none>")) {
      this.view.toggleColor();
    }
  }

}
