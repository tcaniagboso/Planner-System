import schedule.Schedule;
import view.SchedulePanel;

/**
 * A mock {@link SchedulePanel} class for testing purposes.
 * This class extends {@link SchedulePanel} to override its methods for interaction with a graphical
 * schedule panel, logging each method call to a provided {@link StringBuilder}. This allows for
 * verification of method calls during tests without needing a graphical environment.
 */
public class MockSchedulePanel extends SchedulePanel {

  private final StringBuilder log;

  /**
   * Constructs a MockSchedulePanel with a specific logger.
   * Initializes a new {@link MockSchedulePanel} instance that logs interactions to the provided
   * {@link StringBuilder}. Each method overridden in this mock class appends a log entry describing
   * the interaction.
   *
   * @param log The {@link StringBuilder} used for logging interactions with the mock panel.
   *            Must not be null.
   * @throws IllegalArgumentException if the provided log is null, ensuring that all interactions
   *                                  can be logged.
   */
  public MockSchedulePanel(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null");
    }

    this.log = log;
  }

  @Override
  public int getCellWidth() {
    this.log.append("This method gets the width of the panel.").append(System.lineSeparator());
    return 0;
  }

  @Override
  public Schedule getSchedule() {
    this.log.append("This method gets the schedule of the panel.").append(System.lineSeparator());
    return new Schedule("Tobe");
  }

  @Override
  public int getCellHeight() {
    this.log.append("This method gets the height of the panel.").append(System.lineSeparator());
    return 0;
  }

  @Override
  public void setSchedule(Schedule schedule) {
    this.log.append("This method sets the schedule of the panel.").append(System.lineSeparator());
  }

}
