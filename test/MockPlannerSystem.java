import java.io.File;
import java.util.List;
import java.util.Set;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedulestrategy.ScheduleStrategy;
import controller.Observer;
import plannersystem.PlannerSystem;
import schedule.Schedule;

/**
 * A mock implementation of the {@link PlannerSystem} interface for testing purposes.
 * This class simulates the behavior of a planner system without requiring a real
 * database or file system interactions. It uses a {@link StringBuilder} as a log
 * to record method calls, allowing tests to verify that system interactions occur as expected.
 * This approach is particularly useful for testing components that interact with the
 * planner system, enabling verification of system behavior in response to various actions
 * without the complexity of integrating with the actual system implementation.
 */
public class MockPlannerSystem implements PlannerSystem {

  private final StringBuilder log;

  /**
   * Constructs a {@code MockPlannerSystem} with a provided log.
   *
   * @param log A {@link StringBuilder} instance where method call logs will be recorded.
   *            This log is essential for verifying that the mock planner system is
   *            interacting as expected during tests.
   * @throws IllegalArgumentException if {@code log} is {@code null}, ensuring that the mock
   *                                  system is always initialized with a valid log for
   *                                  recording interactions.
   */
  public MockPlannerSystem(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log is null");
    }
    this.log = log;
  }

  @Override
  public void readUserSchedule(File xmlFile) {
    this.log.append("This method reads a user's schedule into the system from the xml file ")
            .append("argument if the file or schedule is valid, otherwise throws an Exception.")
            .append(System.lineSeparator());
  }

  @Override
  public void saveUserSchedule(String userId, String filePath) {
    this.log.append("This method saves the given user's schedule to an xml file with the given ")
            .append("file path, if no Exception is thrown.").append(System.lineSeparator());

  }

  @Override
  public void createEvent(String userId, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<String> invitees) {
    this.log.append("This method creates an event in the system, if possible, otherwise throws an ")
            .append("Exception.").append(System.lineSeparator());
  }

  @Override
  public void modifyEvent(String userId, ReadOnlyEvent event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<String> invitees) {
    this.log.append("This method modifies an event in the system, if possible, otherwise throws ")
            .append("an Exception.").append(System.lineSeparator());

  }

  @Override
  public void removeEvent(String userId, ReadOnlyEvent event) {
    this.log.append("This method removes the event from all the invitees' schedules, if userId is ")
            .append("the host of the event, otherwise it removes the event from only the user's ")
            .append("schedule.").append(System.lineSeparator());
  }

  @Override
  public void scheduleEvent(String userId, String name, boolean isOnline, String location,
                            int duration, List<String> invitees) {
    this.log.append("This method attempts to schedule an event with the given duration and ")
            .append("details in the system, using the chosen scheduling strategy.")
            .append(System.lineSeparator());
  }

  @Override
  public void addObserver(Observer observer) {
    this.log.append("This method adds an observer to the system, to monitor system modifications.")
            .append(System.lineSeparator());

  }

  @Override
  public void removeObserver(Observer observer) {
    this.log.append("This method removes an observer from the system.")
            .append(System.lineSeparator());
  }

  @Override
  public void setScheduleStrategy(ScheduleStrategy scheduleStrategy) {
    this.log.append("This method sets the scheduling strategy of the system.")
            .append(System.lineSeparator());
  }

  @Override
  public void addSchedule(ISchedule schedule) {
    this.log.append("This method adds a schedule to the planner system, if the schedule owner ")
            .append("doesn't exist in the system.").append(System.lineSeparator());
  }

  @Override
  public void addUser(String userId) {
    this.log.append("This method adds a user to the system, if the user does not exists.")
            .append(System.lineSeparator());
  }

  @Override
  public boolean removeUser(String userId) {
    this.log.append("This method returns true if a user was successfully removed from the system, ")
            .append("otherwise false.").append(System.lineSeparator());
    return false;
  }

  @Override
  public void setFirstDayOfWeek(String firstDayOfWeek) {
    this.log.append("This method sets the first day of the week for the planner.")
            .append(System.lineSeparator());
  }

  @Override
  public String displayUserSchedule(String userId) {
    this.log.append("This method displays the chosen userId's schedule, if userId exists in ")
            .append("the system.").append(System.lineSeparator());
    return null;
  }

  @Override
  public String showEvent(String userId, String day, String time) {
    this.log.append("This method shows the event occurring at the given day and time.")
            .append(System.lineSeparator());
    return null;
  }

  @Override
  public Schedule getSchedule(String userId) {
    this.log.append("This method gets the given userId's schedule, if userId exists in the system.")
            .append(System.lineSeparator());
    return null;
  }

  @Override
  public Set<String> getUsers() {
    this.log.append("This method gets all the users currently on the system.")
            .append(System.lineSeparator());
    return null;
  }

  @Override
  public boolean checkEventConflict(ReadOnlyEvent event) {
    this.log.append("This method checks if the given event conflicts with the schedule of ")
            .append("any of its invitees").append(System.lineSeparator());
    return false;
  }

  @Override
  public String getFirstDayOfWeek() {
    this.log.append("This method gets the first day of the week of the planner.")
            .append(System.lineSeparator());
    return "SUNDAY";
  }
}
