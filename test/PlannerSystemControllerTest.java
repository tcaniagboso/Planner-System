import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import controller.PlannerSystemController;
import controller.ScheduleViewController;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.IEvent;
import view.EventView;
import view.EventViewImpl;
import view.PlannerSystemView;

/**
 * Test suite for the {@link ScheduleViewController} class within the planner system.
 * These tests verify the controller's response to various actions and mouse events,
 * ensuring the controller interacts correctly with both the model ({@link PlannerSystem})
 * and the view ({@link PlannerSystemView}). It specifically tests the controller's ability
 * to handle null inputs, launch operations, process action events, and handle mouse clicks.
 */
public class PlannerSystemControllerTest {

  private PlannerSystemController scheduleController;
  private StringBuilder log;
  private PlannerSystem model;

  /**
   * Initializes common objects and state for tests.
   * This setup method instantiates a mock model and view, and a {@link ScheduleViewController},
   * logging interactions for verification.
   */
  @Before
  public void init() {
    log = new StringBuilder();
    model = new MockPlannerSystem(log);
    PlannerSystemView view = new MockPlannerSystemView(log);
    scheduleController = new ScheduleViewController(view);
  }

  /**
   * Tests that the {@link ScheduleViewController} constructor throws an IllegalArgumentException
   * when passed a null view, ensuring the controller requires a valid view reference.
   */
  @Test
  public void testValidation() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new ScheduleViewController(null));
  }

  /**
   * Verifies the launch operation of the {@link ScheduleViewController}.
   * This test checks for proper handling of null input and verifies the sequence of method calls
   * made during a successful launch, using a StringBuilder log to record actions.
   */
  @Test
  public void testLaunch() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> scheduleController.launch(null));

    scheduleController.launch(model);

    Assert.assertEquals("This method sets the given planner system controller as a "
            + "listener for the view." + System.lineSeparator()
            + "This method adds an observer to the system, to monitor system modifications."
            + System.lineSeparator()
            + "This method makes the view visible." + System.lineSeparator(), log.toString());
  }

  /**
   * Tests the controller's response to action events triggered in the UI.
   * This includes simulating user actions like adding a calendar, saving calendars,
   * and selecting a user, and verifying the controller's subsequent interactions with the model
   * and view through the logged sequence of operations.
   */
  @Test
  public void testProcessButtonPress() {
    scheduleController.launch(model);

    scheduleController.processButtonPress("Add calendar");
    String result = result(3);

    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator() + "This method loads a file into the view."
            + System.lineSeparator() + "This method reads a user's schedule into the system from "
            + "the xml file argument if the file or schedule is valid, otherwise throws an "
            + "Exception." + System.lineSeparator() + "This method refreshes the view.", result);

    scheduleController.processButtonPress("Save calendars");
    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator() + "This method returns a desired file path for saving a file."
            + System.lineSeparator() + "This method saves the given user's schedule to an xml file "
            + "with the given file path, if no Exception is thrown." + System.lineSeparator()
            + "This method refreshes the view.", result(7));

    scheduleController.processButtonPress("Select user");
    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator()
            + "This method updates the schedule panel to that of the current user."
            + System.lineSeparator() + "This method refreshes the view.", result(11));

    IEvent event = new Event();
    event.setName("Something");
    event.setEventTimes("Tuesday", "1000", "Tuesday", "1300");
    event.setLocation(true, "Somewhere");
    event.setHost("Tobe");
    event.setInvitees(new ArrayList<>(List.of("Tobe", "Karina", "John")));

    EventView eventView = new EventViewImpl(event, "Tobe");

    scheduleController.setEventView(eventView);

    scheduleController.processButtonPress("Create event");
    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator()
            + "This method creates an event in the system, if possible, otherwise throws an "
            + "Exception." + System.lineSeparator() + "This method refreshes the view.",
            result(14));

    scheduleController.processButtonPress("Remove event");
    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator() + "This method removes the event from all the invitees' "
            + "schedules, if userId is the host of the event, otherwise it removes the event from "
            + "only the user's schedule." + System.lineSeparator()
            + "This method refreshes the view.", result(17));

    // can check more than that because
    scheduleController.processButtonPress("Schedule Event");
    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator()
            + "This method refreshes the view.", result(20));

  }

  /**
   * Helper method to extract and return a portion of the log starting from the specified index.
   * This is used to verify specific sequences of operations within the larger logged interaction
   * history.
   *
   * @param start The starting index from which to extract log entries.
   * @return A string containing the concatenated log entries from the start index onwards.
   */
  public String result(int start) {
    String[] resultList = log.toString().split(System.lineSeparator());
    return String.join(System.lineSeparator(),
            Arrays.copyOfRange(resultList, start, resultList.length));
  }
}
