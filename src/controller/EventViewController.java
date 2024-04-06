package controller;

import schedule.Event;

/**
 * Defines the operations that an event view controller should support.
 * This interface is responsible for initializing and displaying the event view
 * with details from a specified event. It typically manages user interactions
 * related to a single event, such as editing event details or responding to user
 * commands within the event view.
 */
public interface EventViewController {

  /**
   * Initializes the view with the details of the specified event and makes the view visible.
   * This method is called to either display an existing event for editing or to prepare
   * the view for creating a new event.
   *
   * @param event The event to display in the view; if null, an IllegalArgumentException is thrown.
   */
  void launch(Event event);
}
