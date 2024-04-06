package command;

import schedule.Event;
import view.EventView;
import view.EventViewImpl;

/**
 * A command for opening an event scheduling view for a specific user. This command initializes
 * an {@link EventView} with a new {@link Event} object and sets it up for the specified user,
 * allowing the user to schedule a new event.
 */
public class OpenScheduleEvent implements Command {
  private final String userId;

  /**
   * Constructs an {@code OpenScheduleEvent} command with the user ID of the current user.
   * This user ID is used to identify the user for whom the event scheduling view will be opened.
   *
   * @param userId The ID of the user who intends to schedule a new event. This ID must not be null.
   * @throws IllegalArgumentException if the {@code userId} is null, indicating that a valid user
   *                                  must be specified for the operation.
   */
  public OpenScheduleEvent(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Current user is null");
    }
    this.userId = userId;
  }

  /**
   * Executes the operation to open an event scheduling view for the specified user. This method
   * initializes an {@link EventView} with a new event and sets it up for the user identified by
   * {@code userId}.
   * If the {@code userId} indicates that no user is selected (e.g., {@code userId} is "<none>"),
   * an exception is thrown to signal that a valid user selection is required to proceed with
   * scheduling an event.
   *
   * @throws IllegalStateException if no user is selected (userId is "<none>"), enforcing the
   *                               requirement that a valid user must be selected before scheduling
   *                               an event.
   */
  @Override
  public void execute() {
    if (this.userId.equals("<none>")) {
      throw new IllegalStateException("No user is selected");
    }
    EventView newEvent = new EventViewImpl(new Event(), userId);
  }
}
