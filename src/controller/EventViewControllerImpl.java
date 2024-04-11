package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.command.Command;
import controller.command.CreateEvent;
import controller.command.ModifyEvent;
import controller.command.RemoveEvent;
import controller.command.ScheduleEvent;
import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * Implementation of the EventViewController interface that responds to user actions
 * within an EventView and interacts with the PlannerSystem model. This controller
 * handles creating, modifying, and removing events based on user input.
 */
public class EventViewControllerImpl implements EventViewController, ActionListener {

  private final EventView eventView;
  private final PlannerSystem model;
  private Event event;

  /**
   * Constructs an EventViewControllerImpl with a given event view and model.
   *
   * @param eventView The view associated with this controller, providing the user interface.
   * @param model The model with which the controller interacts to manipulate event data.
   * @throws IllegalArgumentException if either argument is null, indicating a failure to properly
   *                                  initialize the controller.
   */
  public EventViewControllerImpl(EventView eventView, PlannerSystem model) {
    if (eventView == null || model == null) {
      throw new IllegalArgumentException("Arguments are null");
    }
    this.eventView = eventView;
    this.model = model;
    this.eventView.setActionListener(this);
  }

  @Override
  public void launch(Event event) {
    this.setEvent(event);
    this.eventView.makeVisible();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String currentUser = this.eventView.getUserId();
    String action = e.getActionCommand();
    Command command;
    switch (action) {
      case "Create event":
        command = new CreateEvent(currentUser, model, eventView, event);
        break;
      case "Modify event":
        command = new ModifyEvent(currentUser, model, eventView, event);
        break;
      case "Remove event":
        command = new RemoveEvent(currentUser, model, eventView, event);
        break;
      case "Schedule event":
        command = new ScheduleEvent(currentUser, model, eventView, event);
        break;
      default:
        command = null;
    }

    if (command != null) {
      try {
        command.execute();
      } catch (Exception ee) {
        this.eventView.displayError(ee.getMessage());
      }
    }
    this.eventView.refresh();
  }

  /**
   * Sets the current event to the specified event. This is used to initialize or update
   * the event that this controller is managing.
   *
   * @param event The new event to be managed by this controller.
   * @throws IllegalArgumentException if the provided event is null.
   */
  private void setEvent(Event event) {
    if (event == null) {
      throw new IllegalArgumentException("Event is null");
    }
    this.event = event;
  }
}
