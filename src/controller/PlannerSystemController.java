package controller;

import plannersystem.PlannerSystem;

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

}
