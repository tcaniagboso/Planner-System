package view;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;

/**
 * Defines the operations that a planner system view should support. This includes
 * the ability to set action and mouse listeners, display error messages, load and
 * save files, and refresh the view among others.
 */
public interface PlannerSystemView {

  /**
   * Sets the action listener for the view. This listener will be notified of action events
   * generated within the view.
   *
   * @param listener The ActionListener to be set.
   */
  void setActionListener(ActionListener listener);

  /**
   * Sets the mouse listener for the view. This listener will be notified of mouse events
   * generated within the view.
   *
   * @param listener The MouseAdapter to be set.
   */
  void setMouseListener(MouseAdapter listener);

  /**
   * Makes the view visible to the user.
   */
  void makeVisible();

  /**
   * Displays an error message dialog to the user.
   *
   * @param message The message to be displayed in the dialog.
   */
  void displayErrorMessage(String message);

  /**
   * Prompts the user to load a calendar file from the filesystem.
   *
   * @return The File selected by the user, or null if the operation was canceled.
   */
  File loadFile();

  /**
   * Prompts the user to save the current calendar to a file.
   *
   * @return The file path where the calendar was saved, or null if the operation was canceled.
   */
  String saveFile();

  /**
   * Refreshes the view to reflect any updates to the underlying model or state.
   */
  void refresh();

  /**
   * Retrieves the currently selected user.
   *
   * @return The ID of the currently selected user as a String.
   */
  String getCurrentUser();

  /**
   * Updates the schedule panel to display information for the specified user.
   *
   * @param currentUser The ID of the user whose schedule information should be displayed.
   */
  void updateSchedulePanel(String currentUser);

  /**
   * Updates the list of users in the view, typically in response to changes in the underlying
   * model.
   */
  void updateUsers();

  /**
   * Retrieves the SchedulePanel component of the view. This component is responsible for
   * displaying the schedule information.
   *
   * @return The SchedulePanel component.
   */
  SchedulePanel getSchedulePanel();
}
