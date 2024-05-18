import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import controller.command.AddCalendar;
import controller.command.Command;
import controller.command.CreateEvent;
import controller.command.ModifyEvent;
import controller.command.RemoveEvent;
import controller.command.SaveCalendars;
import controller.command.ScheduleEvent;
import controller.command.SelectUser;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.IEvent;
import view.EventView;
import view.PlannerSystemView;

/**
 * Tests for command classes in the planner system. Each test ensures that commands correctly
 * interact with the model and view, handling their responsibilities and any exceptions as expected.
 */
public class CommandTest {

  private Command command;
  private IEvent filled;
  private IEvent empty;
  private PlannerSystem model;
  private PlannerSystemView view;
  private StringBuilder log;
  private EventView eventView;
  private String userId;

  /**
   * Initializes common objects used across test cases, including mock versions of the model,
   * view, and events to simulate various scenarios.
   */
  @Before
  public void init() {
    filled = new Event();
    filled.setName("CS3500 Lecture Morning");
    filled.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    filled.setLocation(true, "Churchill Hall Room 109");
    filled.setHost("Tobe");
    filled.setInvitees(new ArrayList<>(List.of("Tobe", "Karina")));

    empty = new Event();

    log = new StringBuilder();
    model = new MockPlannerSystem(log);
    view = new MockPlannerSystemView(log);
    eventView = new MockEventView(log);
    userId = "Tobe";
  }

  /**
   * Tests the AddCalendar command for correct exception handling and execution.
   * Verifies that the command interacts as expected with the view and model,
   * loading a file into the view and reading a user's schedule into the system.
   */
  @Test
  public void testAddCalendar() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new AddCalendar(null, model));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new AddCalendar(view, null));

    command = new AddCalendar(view, model);
    String result = "This method loads a file into the view." + System.lineSeparator()
            + "This method reads a user's schedule into the system from the xml file argument "
            + "if the file or schedule is valid, otherwise throws an Exception."
            + System.lineSeparator();
    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Tests the CreateEvent command for correct exception handling, field validation,
   * and event creation. Validates that all necessary fields are checked and that
   * the event is correctly created in the system.
   */
  @Test
  public void testCreateEvent() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CreateEvent(userId, null, eventView, empty));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CreateEvent(userId, model, null, empty));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CreateEvent(userId, model, eventView, null));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CreateEvent(null, model, eventView, empty));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CreateEvent(" ", null, eventView, empty));
    command = new CreateEvent(userId, model, eventView, empty);
    String result = "This method checks if all the event view fields have been filled."
            + System.lineSeparator() + "This method gets the name of the event from the event view."
            + System.lineSeparator() + "This method gets the start day of the event from the event "
            + "view." + System.lineSeparator() + "This method gets the start time of the event "
            + "from the event view." + System.lineSeparator() + "This method gets the end day of "
            + "the event from the event view." + System.lineSeparator() + "This method gets the "
            + "end time of the event from the event view." + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator() + "This method gets the location (online?) of the event from "
            + "the event view." + System.lineSeparator() + "This method gets the invitees of the "
            + "event from the event view." + System.lineSeparator() + "This method creates an "
            + "event in the system, if possible, otherwise throws an Exception."
            + System.lineSeparator();
    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Tests the ModifyEvent command to ensure it correctly modifies an existing event.
   * This command extends CreateEvent, inheriting its validation and execution logic
   * but applies modifications to the event rather than creating a new one.
   */
  @Test
  public void testModifyEvent() {
    // ModifyEvent command extends create event, so we don't have to test it.
    command = new ModifyEvent(userId, model, eventView, filled);
    String result = "This method checks if all the event view fields have been filled."
            + System.lineSeparator() + "This method gets the name of the event from the event view."
            + System.lineSeparator() + "This method gets the start day of the event from the event "
            + "view." + System.lineSeparator() + "This method gets the start time of the event "
            + "from the event view." + System.lineSeparator() + "This method gets the end day of "
            + "the event from the event view." + System.lineSeparator() + "This method gets the "
            + "end time of the event from the event view." + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator() + "This method gets the location (online?) of the event from "
            + "the event view." + System.lineSeparator() + "This method gets the invitees of the "
            + "event from the event view." + System.lineSeparator() + "This method modifies an "
            + "event in the system, if possible, otherwise throws an Exception."
            + System.lineSeparator();

    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Tests the SaveCalendars command to ensure calendars are correctly saved.
   * Validates the command's interaction with the view for file selection and
   * the model for saving the user's schedule to the selected file.
   */
  @Test
  public void testSaveCalendars() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new SaveCalendars(null, view, model));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new SaveCalendars(" ", view, model));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new SaveCalendars(userId, null, model));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new SaveCalendars(userId, view, null));

    command = new SaveCalendars(userId, view, model);
    String result = "This method returns a desired file path for saving a file."
            + System.lineSeparator() + "This method saves the given user's schedule to an xml "
            + "file with the given file path, if no Exception is thrown." + System.lineSeparator();
    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);

    // test when <none> is selected.
    command = new SaveCalendars("<none>", view, model);
    Assert.assertThrows(IllegalStateException.class, () -> command.execute());
  }


  /**
   * Tests the ScheduleEvent command for its ability to schedule events based on
   * various constraints. Verifies that it extends the CreateEvent command's logic
   * for event creation and applies scheduling strategies.
   */
  @Test
  public void testScheduleEvent() {
    // extends CreateEvent, so constructor has already been tested.
    command = new ScheduleEvent(userId, model, eventView, empty);
    String result = "This method checks if all the event view fields have been filled."
            + System.lineSeparator() + "This method gets the name of the event from the event view."
            + System.lineSeparator() + "This method gets the location (place) of the event "
            + "from the event view." + System.lineSeparator() + "This method gets the location "
            + "(online?) of the event from the event view." + System.lineSeparator()
            + "This method gets the invitees of the event from the event view."
            + System.lineSeparator() + "This method gets the duration of the event from "
            + "the event view." + System.lineSeparator() + "This method attempts to schedule an "
            + "event with the given duration and details in the system, using the chosen "
            + "scheduling strategy." + System.lineSeparator();
    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Tests the SelectUser command for updating the current user and their schedule
   * in the view. Verifies that the schedule panel is updated to display the selected
   * user's schedule.
   */
  @Test
  public void testSelectUser() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new SelectUser(userId, null));

    command = new SelectUser(userId, view);
    String result = "This method updates the schedule panel to that of the current user."
            + System.lineSeparator();
    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Tests the RemoveEvent command to ensure it correctly removes events from the
   * system. Validates that the event is removed from all invitees' schedules if the
   * user is the host, or only from the user's schedule otherwise.
   */
  @Test
  public void testRemoveEvent() {
    // RemoveEvent command extends create event, so we don't have to test it.
    command = new RemoveEvent(userId, model, eventView, filled);
    String result = "This method checks if all the event view fields have been filled."
            + System.lineSeparator() + "This method gets the name of the event from the event view."
            + System.lineSeparator() + "This method gets the start day of the event from the event "
            + "view." + System.lineSeparator() + "This method gets the start time of the event "
            + "from the event view." + System.lineSeparator() + "This method gets the end day of "
            + "the event from the event view." + System.lineSeparator() + "This method gets the "
            + "end time of the event from the event view." + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator() + "This method gets the location (online?) of the event from "
            + "the event view." + System.lineSeparator() + "This method gets the invitees of the "
            + "event from the event view." + System.lineSeparator() + "This method removes the "
            + "event from all the invitees' schedules, if userId is the host of the event, "
            + "otherwise it removes the event from only the user's schedule."
            + System.lineSeparator();

    boolean passed = testCommand(command, result, log);
    Assert.assertTrue(passed);
  }

  /**
   * Executes a given command and compares the actual outcomes against the expected results.
   * This method serves as a utility for running command tests, simplifying the process
   * of asserting the expected behavior of command execution. It utilizes a StringBuilder
   * to log the interactions with the mock objects, enabling verification that the command
   * interacted with the model and view as anticipated.
   *
   * @param command The command to be tested.
   * @param result The expected string that should match the content of the log after the
   *               command execution.
   * @param log The StringBuilder instance that contains the log of interactions between the
   *            command and the mock objects.
   * @return true if the actual log matches the expected log, indicating that the command behaved
   *         as expected; otherwise, false.
   */
  private boolean testCommand(Command command, String result, StringBuilder log) {
    command.execute();
    return result.contentEquals(log);
  }

}
