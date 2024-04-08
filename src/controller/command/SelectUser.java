package controller.command;

import view.PlannerSystemView;

/**
 * A command that selects a user in the planner system and updates the view to display
 * the schedule of the selected user. This command is part of the application's command
 * pattern implementation, allowing for dynamic user selection and view updates.
 */
public class SelectUser implements Command {

  private final String userId;
  private final PlannerSystemView view;

  /**
   * Constructs a {@code SelectUser} command with the specified user ID and the system view.
   * This setup facilitates the updating of the schedule panel in the view based on the
   * selected user's data.
   *
   * @param userId The ID of the user to be selected. This ID is used to retrieve and display
   *               the user's schedule. The ID can be any string value that uniquely identifies
   *               a user within the system.
   * @param view   The {@link PlannerSystemView} instance that contains the schedule panel
   *               to be updated. Must not be null.
   * @throws IllegalArgumentException if the provided view is null, indicating that a valid
   *                                  view instance is required for the command to operate.
   */
  public SelectUser(String userId, PlannerSystemView view) {
    if (view == null) {
      throw new IllegalArgumentException("View is null");
    }

    this.userId = userId;
    this.view = view;
  }

  /**
   * Executes the command to select a user and update the schedule panel within the system view.
   * This method calls {@code updateSchedulePanel} on the provided {@link PlannerSystemView}
   * instance, passing the selected user's ID. The view is then responsible for fetching the
   * appropriate schedule data and updating the display accordingly.
   */
  @Override
  public void execute() {
    this.view.updateSchedulePanel(userId);
  }
}
