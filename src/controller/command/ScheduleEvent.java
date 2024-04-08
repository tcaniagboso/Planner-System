package controller.command;

import java.util.List;

import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * Represents a command to schedule an event automatically within the planner system.
 * This command extends the {@link CreateEvent} class, utilizing its structure to
 * schedule an event based on provided user inputs. It makes use of the planner system's
 * automatic scheduling feature, which calculates an optimal time for the event,
 * considering the availability of all invitees and the event's host.
 *
 * @see CreateEvent
 */
public class ScheduleEvent extends CreateEvent {

  /**
   * Constructs a ScheduleEvent command with necessary information for scheduling.
   *
   * @param userId    The ID of the user attempting to schedule the event.
   * @param model     The planner system model where the event will be scheduled.
   * @param eventView The view from which event details are gathered.
   * @param event     The event to be scheduled.
   */
  public ScheduleEvent(String userId, PlannerSystem model, EventView eventView, Event event) {
    super(userId, model, eventView, event);
  }

  /**
   * Executes the command to schedule an event. It first checks if all fields in the view are
   * filled, then retrieves event details from the view, and finally requests the model to schedule
   * the event automatically. If the automatic scheduling is successful, the event is added to the
   * system; otherwise, an exception is thrown.
   *
   * @throws IllegalStateException If any field is not filled, or if the automatic scheduling fails.
   */
  @Override
  public void execute() {
    try {
      this.eventView.checkFieldsNotEmpty();
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
    }

    try {
      String name = this.eventView.getEventName();
      String location = this.eventView.getEventLocation();
      boolean isOnline = this.eventView.getOnline();
      List<String> invitees = this.eventView.getInvitees();
      int duration = this.eventView.getDuration();
      this.model.automaticScheduling(userId, name, isOnline, location, duration, invitees);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot Schedule event: " + e.getMessage());
    }
  }
}
