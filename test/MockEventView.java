import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import view.EventView;

/**
 * A mock implementation of the {@link EventView} interface used for testing purposes.
 * This class simulates the behavior of an actual event view without creating a real
 * user interface. It's primarily used to verify interactions between the view and other
 * components in the system during tests, especially to check if certain methods are
 * called as expected.
 * The mock view uses a {@link StringBuilder} as a log to record calls to its methods,
 * allowing tests to assert that the correct methods were invoked. This approach simplifies
 * the testing of view actions and interactions without the need to deal with the complexity
 * of actual UI components or event handling.
 */
public class MockEventView implements EventView {

  private final StringBuilder log;

  /**
   * Constructs a {@code MockEventView} with a provided log.
   *
   * @param log A {@link StringBuilder} instance to which log entries will be appended during
   *            method calls. This log is used in tests to verify that the correct actions
   *            have been performed by this view.
   * @throws IllegalArgumentException if {@code log} is {@code null}, ensuring that the mock
   *                                  view is always initialized with a valid log for
   *                                  recording its actions.
   */
  public MockEventView(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null.");
    }
    this.log = log;
  }

  @Override
  public void refresh() {
    this.log.append("This method refreshes the event view.").append(System.lineSeparator());
  }

  @Override
  public void displayError(String message) {
    this.log.append("This method displays an error message.").append(System.lineSeparator());
  }

  @Override
  public void makeVisible() {
    this.log.append("This method makes the event view visible.").append(System.lineSeparator());
  }

  @Override
  public String getUserId() {
    this.log.append("This method gets the userId of the event view.")
            .append(System.lineSeparator());
    return null;
  }

  @Override
  public void setActionListener(ActionListener listener) {
    this.log.append("This method sets the action listener of the event view to the given listener.")
            .append(System.lineSeparator());
  }

  @Override
  public String getEventName() {
    this.log.append("This method gets the name of the event from the event view.")
            .append(System.lineSeparator());
    return "Sunday Service";
  }

  @Override
  public String getStartDay() {
    this.log.append("This method gets the start day of the event from the event view.")
            .append(System.lineSeparator());
    return "Sunday";
  }

  @Override
  public String getStartTime() {
    this.log.append("This method gets the start time of the event from the event view.")
            .append(System.lineSeparator());
    return "1000";
  }

  @Override
  public String getEndDay() {
    this.log.append("This method gets the end day of the event from the event view.")
            .append(System.lineSeparator());
    return "Sunday";
  }

  @Override
  public String getEndTime() {
    this.log.append("This method gets the end time of the event from the event view.")
            .append(System.lineSeparator());
    return "1300";
  }

  @Override
  public String getEventLocation() {
    this.log.append("This method gets the location (place) of the event from the event view.")
            .append(System.lineSeparator());
    return "Church";
  }

  @Override
  public boolean getOnline() {
    this.log.append("This method gets the location (online?) of the event from the event view.")
            .append(System.lineSeparator());
    return true;
  }

  @Override
  public List<String> getInvitees() {
    this.log.append("This method gets the invitees of the event from the event view.")
            .append(System.lineSeparator());
    return new ArrayList<>(List.of("Tobe", "Karina", "Joe"));
  }

  @Override
  public int getDuration() {
    this.log.append("This method gets the duration of the event from the event view.")
            .append(System.lineSeparator());
    return 90;
  }

  @Override
  public void checkFieldsNotEmpty() {
    this.log.append("This method checks if all the event view fields have been filled.")
            .append(System.lineSeparator());
  }
}
