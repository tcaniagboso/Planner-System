import controller.EventViewController;
import schedule.Event;

/**
 * A mock implementation of {@link EventViewController} used for testing purposes. This class
 * simulates the behavior of a real event view controller without performing any actual view
 * rendering or event handling. It's primarily used to verify interactions between the controller
 * and other components in the system during tests, especially to check if certain methods are
 * called as expected.
 * The mock controller uses a {@link StringBuilder} as a log to record calls to its methods,
 * allowing tests to assert that the correct methods were invoked. This approach simplifies
 * the testing of controller actions and interactions without the need to deal with the complexity
 * of actual UI components or event handling.
 */
public class MockEventViewController implements EventViewController {

  private final StringBuilder log;

  /**
   * Constructs a {@code MockEventViewController} with a provided log.
   *
   * @param log A {@link StringBuilder} instance to which log entries will be appended during
   *            method calls. This log is used in tests to verify that the correct actions
   *            have been performed by this controller.
   * @throws IllegalArgumentException if {@code log} is {@code null}, ensuring that the mock
   *                                  controller is always initialized with a valid log for
   *                                  recording its actions.
   */
  public MockEventViewController(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null.");
    }
    this.log = log;
  }

  @Override
  public void launch(Event event) {
    this.log.append("This method launches the controller by setting the event of the controller ")
            .append("and making the eventView visible.").append(System.lineSeparator());
  }
}
