package controller;

import plannersystem.PlannerSystem;
import schedule.ReadOnlyEvent;
import view.EventView;

/**
 * Defines the operations that a planner system controller should support.
 * The controller acts as an intermediary between the view and the model,
 * handling user actions and updating the model or view accordingly.
 */
public interface PlannerSystemController extends Observer {

  /**
   * Initializes and launches the application with the provided model. This method
   * is responsible for setting up the initial state of the application and making
   * the view visible to the user.
   *
   * @param model The PlannerSystem model that contains the data and logic of the application.
   */
  void launch(PlannerSystem model);

  /**
   * Processes button press events by executing corresponding commands based on the action command
   * received.
   *
   * @param action the action command string indicating which button was pressed.
   */
  void processButtonPress(String action);

  /**
   * Processes mouse click events on schedule events by creating and executing an OpenEventFrame
   * command.
   *
   * @param event the {@link ReadOnlyEvent} that was clicked.
   */
  void processMouseClick(ReadOnlyEvent event);

  /**
   * This is method is sets the event view of the planner system controller to the given view. This
   * method is solely for making testing of the event controller easy.
   *
   * @param view the event view to be assigned.
   */
  void setEventView(EventView view);

}
