package view;

import java.util.List;

import controller.PlannerSystemController;
import schedule.ReadOnlyEvent;

/**
 * The EventView interface defines the operations that an event view should support
 * in the context of displaying and interacting with event details within a user interface.
 */
public interface EventView {

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
   * Sets a planner system controller as a listener for user-initiated actions within this view.
   *
   * @param listener The EventViewController to be set.
   */
  void setActionListener(PlannerSystemController listener);

  /**
   * Retrieves the name of the event from the view.
   *
   * @return The event name as a String.
   */
  String getEventName();

  /**
   * Retrieves the start day of the event from the view. Returns the entered start day for standard
   * event views and returns null for schedule event views.
   *
   * @return The start day as a String.
   */
  String getStartDay();

  /**
   * Retrieves the start time of the event from the view. Returns the entered start time for
   * standard event views and returns null for schedule event views.
   *
   * @return The start time as a String.
   */
  String getStartTime();

  /**
   * Retrieves the end day of the event from the view. Returns the entered end day for standard
   * event views and returns null for schedule event views.
   *
   * @return The end day as a String.
   */
  String getEndDay();

  /**
   * Retrieves the end time of the event from the view. Returns the entered end time for standard
   * event views and returns null for schedule event views.
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
   * Retrieves the duration entered by the user in the designated text field.
   * The method attempts to parse the input string into an integer representing
   * the duration. If the parsing fails or the input is not a valid number,
   * it throws an IllegalArgumentException. Returns 0 for standard event views and returns the
   * entered duration for schedule event views.
   *
   * @return The duration as an integer, extracted from the text field.
   * @throws IllegalArgumentException If the text entered is not a valid integer
   *                                  or if the text field does not contain a numeric value
   *                                  representing the duration.
   */
  int getDuration();

  /**
   * Checks if all text fields and text areas within the view are not empty.
   *
   * @throws IllegalStateException if any field or text area is empty.
   */
  void checkFieldsNotEmpty();

  /**
   * Retrieves the event associated with the event view.
   *
   * @return The event associated with the event view.
   */
  ReadOnlyEvent getEvent();

  /**
   * Checks if the event view is currently visible on the screen.
   *
   * @return true if the event view is visible, false otherwise.
   */
  boolean isViewVisible();
}
