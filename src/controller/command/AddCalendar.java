package controller.command;

import java.io.File;

import plannersystem.PlannerSystem;
import view.PlannerSystemView;

/**
 * A command class that encapsulates the action of adding a calendar to the planner system.
 * It integrates with both the PlannerSystem model and the PlannerSystemView to facilitate
 * the selection of a calendar file by the user and its subsequent loading into the model.
 */
public class AddCalendar implements Command {

  private final PlannerSystemView view;
  private final PlannerSystem model;

  /**
   * Constructs an AddCalendar command with the specified view and model.
   *
   * @param view  The {@link PlannerSystemView} instance that provides the user interface
   *              for selecting the calendar file to be added.
   * @param model The {@link PlannerSystem} instance where the loaded calendar will be added.
   * @throws IllegalArgumentException if either the view or model argument is null,
   *                                  indicating that valid instances are required for operation.
   */
  public AddCalendar(PlannerSystemView view, PlannerSystem model) {
    if (view == null || model == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    this.view = view;
    this.model = model;
  }

  /**
   * Executes the add calendar command by prompting the user to select a file through the view,
   * and then loading this file into the planner system model. If the file cannot be successfully
   * read or processed, an exception is thrown.
   * This method leverages the view to open a file dialog for the user to select the calendar file,
   * and upon selection, attempts to load the file's content into the model. Any errors encountered
   * during file reading or processing result in throwing an IllegalArgumentException.
   *
   * @throws IllegalArgumentException if the selected file cannot be read or processed by the model,
   *                                  encapsulating the original exception message for clarity.
   */
  @Override
  public void execute() {
    File file = this.view.loadFile();
    try {
      this.model.readUserSchedule(file);
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to read user schedule: " + e.getMessage());
    }
  }
}
