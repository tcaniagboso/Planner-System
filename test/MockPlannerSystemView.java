import java.io.File;

import controller.PlannerSystemController;
import view.PlannerSystemView;

/**
 * A mock implementation of the {@link PlannerSystemView} interface for testing purposes.
 * This class simulates the interactions with the planner system view, allowing tests
 * to verify the view's behavior without actual GUI manipulation. It logs method calls
 * and parameters to a {@link StringBuilder}, facilitating verification of interactions
 * with the view in a controlled test environment.
 */
public class MockPlannerSystemView implements PlannerSystemView {
  private final StringBuilder log;

  /**
   * Constructs a {@code MockPlannerSystemView} with a specified log.
   *
   * @param log A {@link StringBuilder} instance where interactions with the mock view
   *            are logged. This allows for later inspection and verification of the
   *            mock view's behavior during tests.
   * @throws IllegalArgumentException if {@code log} is {@code null}, ensuring that
   *                                  every instance of this mock view is properly
   *                                  initialized with a logging mechanism.
   */
  public MockPlannerSystemView(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null");
    }
    this.log = log;
  }

  @Override
  public void setActionListener(PlannerSystemController listener) {
    this.log.append("This method sets the given planner system controller as a listener for the ")
            .append("view.").append(System.lineSeparator());
  }

  @Override
  public void makeVisible() {
    this.log.append("This method makes the view visible.").append(System.lineSeparator());
  }

  @Override
  public void displayErrorMessage(String message) {
    this.log.append("This method displays error messages for the view.")
            .append(System.lineSeparator());
  }

  @Override
  public File loadFile() {
    this.log.append("This method loads a file into the view.").append(System.lineSeparator());
    return new File("File");
  }

  @Override
  public String saveFile() {
    this.log.append("This method returns a desired file path for saving a file.")
            .append(System.lineSeparator());
    return "filepath";
  }

  @Override
  public void refresh() {
    this.log.append("This method refreshes the view.").append(System.lineSeparator());
  }

  @Override
  public String getCurrentUser() {
    this.log.append("This method gets the current selected user of the view.")
            .append(System.lineSeparator());
    return "Tobe";
  }

  @Override
  public void updateSchedulePanel(String currentUser) {
    this.log.append("This method updates the schedule panel to that of the current user.")
            .append(System.lineSeparator());
  }

  @Override
  public void updateUsers() {
    this.log.append("This method updates the users currently shown by the view.")
            .append(System.lineSeparator());
  }

  @Override
  public void toggleColor() {
    this.log.append("This tells the schedule panel to toggle its color.")
            .append(System.lineSeparator());
  }
}
