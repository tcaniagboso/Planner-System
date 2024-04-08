package controller.command;

import plannersystem.PlannerSystem;
import view.PlannerSystemView;

/**
 * A command that saves the current state of a user's calendar to a file. This command is
 * responsible for invoking the save operation within the planner system, leveraging user
 * interaction through the system view to obtain a file path for saving.
 */
public class SaveCalendars implements Command {

  private final String userId;
  private final PlannerSystemView view;
  private final PlannerSystem model;

  /**
   * Constructs a {@code SaveCalendars} command with the specified user ID, view, and model.
   * This setup allows for saving the calendar of the specified user to a persistent storage.
   *
   * @param userId The ID of the user whose calendar is to be saved. Must not be null or empty.
   * @param view   The {@link PlannerSystemView} that provides the interface for selecting a file
   *               path.
   * @param model  The {@link PlannerSystem} that contains the calendar data to be saved.
   * @throws IllegalArgumentException if the userId is null or empty, or if either the view or model
   *                                  are null, indicating that valid and complete parameters are
   *                                  required for the operation.
   */
  public SaveCalendars(String userId, PlannerSystemView view, PlannerSystem model) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("User ID is null or empty");
    }
    if (view == null || model == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    this.userId = userId;
    this.view = view;
    this.model = model;
  }

  /**
   * Executes the save operation. This method prompts the user, through the system view,
   * to select a file path where the calendar will be saved. It then invokes the model's
   * saving file functionality to persist the calendar data to the selected location.
   * <p>
   * If no user is selected or if the saving process encounters any issues, appropriate
   * exceptions are thrown to indicate failure.
   *
   * @throws IllegalStateException if no user is selected or if any problem occurs during
   *                               the file saving process, including issues with file path
   *                               selection or writing to the file.
   */
  @Override
  public void execute() {
    String filePath = this.view.saveFile();

    if (userId.equals("<none>")) {
      throw new IllegalStateException("No user selected");
    }
    try {
      this.model.saveUserSchedule(userId, filePath);
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to save user schedule: " + e.getMessage());
    }
  }
}
