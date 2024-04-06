package view;

import java.awt.event.ActionListener;
import java.util.List;


/**
 * The EventView interface defines the operations that an event view should support
 * in the context of displaying and interacting with event details within a user interface.
 */
public interface EventView {

  /**
   * Refreshes the view to reflect any changes to the underlying event data.
   */
  void refresh();

  /**
   * Displays an error message to the user using a JOptionPane.
   *
   * @param message The message to be displayed to the user.
   */
  void displayError(String message);

  /**
   * Makes the event view visible to the user.
   */
  void makeVisible();

  /**
   * Retrieves the user ID associated with this event view.
   *
   * @return The user ID as a String.
   */
  String getUserId();

  /**
   * Sets the action listener for user-initiated actions within this view.
   *
   * @param listener The ActionListener to be set.
   */
  void setActionListener(ActionListener listener);

  /**
   * Retrieves the name of the event from the view.
   *
   * @return The event name as a String.
   */
  String getEventName();

  /**
   * Retrieves the start day of the event from the view.
   *
   * @return The start day as a String.
   */
  String getStartDay();

  /**
   * Retrieves the start time of the event from the view.
   *
   * @return The start time as a String.
   */
  String getStartTime();

  /**
   * Retrieves the end day of the event from the view.
   *
   * @return The end day as a String.
   */
  String getEndDay();

  /**
   * Retrieves the end time of the event from the view.
   *
   * @return The end time as a String.
   */
  String getEndTime();

  /**
   * Retrieves the location of the event from the view.
   *
   * @return The event location as a String.
   */
  String getEventLocation();

  /**
   * Determines whether the event is marked as online from the view.
   *
   * @return true if the event is online; false otherwise.
   */
  boolean getOnline();

  /**
   * Retrieves a list of invitees to the event from the view.
   *
   * @return A list of invitees as Strings.
   */
  List<String> getInvitees();

  /**
   * Checks if all text fields and text areas within the view are not empty.
   *
   * @throws IllegalStateException if any field or text area is empty.
   */
  void checkFieldsNotEmpty();


}
