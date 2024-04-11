import java.awt.event.ActionEvent;

/**
 * A mock {@link ActionEvent} class for testing purposes.
 * This class allows the simulation of action events within unit tests, providing control over
 * the parameters of an action event such as the source component, event ID, and command string.
 * It extends {@link ActionEvent}, enabling the customization of the action command returned by the
 * event.
 */
public class MockActionEvent extends ActionEvent {

  private final String command;

  /**
   * Constructs a new {@link MockActionEvent} instance.
   * Initializes an {@link ActionEvent} with specified details including the event source, event ID,
   * and command string. The command string can be used to simulate different commands being
   * triggered by the event.
   *
   * @param source  The {@link Object} on which the Event initially occurred.
   * @param id      The integer that identifies the event type.
   *                For example, {@link ActionEvent#ACTION_PERFORMED}.
   * @param command A {@link String} command which could represent an action command string.
   *                This string is used to simulate command actions within tests.
   */
  public MockActionEvent(Object source, int id, String command) {
    super(source, id, command);
    this.command = command;
  }

  /**
   * Overrides the {@link ActionEvent#getActionCommand()} method to return the command string
   * passed during the construction of this mock event. This allows for predictable testing of
   * action command handling logic.
   *
   * @return The command string associated with this event.
   */
  @Override
  public String getActionCommand() {
    return this.command;
  }
}
