import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JButton;

import controller.PlannerSystemController;
import controller.ScheduleViewController;
import plannersystem.PlannerSystem;
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

    Assert.assertEquals("This method gets the schedule panel of the view."
            + System.lineSeparator() + "This method sets the action listener to the "
            + "given listener." + System.lineSeparator() + "This method sets the mouse "
            + "listener to the given listener." + System.lineSeparator()
            + "This method adds an observer to the system, to monitor system modifications."
            + System.lineSeparator() + "This method makes the view visible."
            + System.lineSeparator(), log.toString());
  }

  /**
   * Tests the controller's response to action events triggered in the UI.
   * This includes simulating user actions like adding a calendar, saving calendars,
   * and selecting a user, and verifying the controller's subsequent interactions with the model
   * and view through the logged sequence of operations.
   */
  @Test
  public void testActionEvent() {
    scheduleController.launch(model);
    Object dummySource = new Object();
    MockActionEvent addCalendar = new MockActionEvent(dummySource, 1, "Add calendar");
    MockActionEvent saveCalendar = new MockActionEvent(dummySource, 2,
            "Save calendars");
    MockActionEvent selectUser = new MockActionEvent(dummySource, 3, "Select user");

    ((ActionListener) scheduleController).actionPerformed(addCalendar);
    String result = result(5);

    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator() + "This method loads a file into the view."
            + System.lineSeparator() + "This method reads a user's schedule into the system from "
            + "the xml file argument if the file or schedule is valid, otherwise throws an "
            + "Exception." + System.lineSeparator() + "This method refreshes the view.", result);

    ((ActionListener) scheduleController).actionPerformed(saveCalendar);

    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator() + "This method returns a desired file path for saving a file."
            + System.lineSeparator() + "This method saves the given user's schedule to an xml file "
            + "with the given file path, if no Exception is thrown." + System.lineSeparator()
            + "This method refreshes the view.", result(9));

    ((ActionListener) scheduleController).actionPerformed(selectUser);

    Assert.assertEquals("This method gets the current selected user of the view."
            + System.lineSeparator()
            + "This method updates the schedule panel to that of the current user."
            + System.lineSeparator() + "This method refreshes the view.", result(13));
  }

  /**
   * Simulates a mouse click event within the UI and verifies the controller's response.
   * This test checks the controller's process for handling mouse clicks, ensuring it
   * appropriately queries and manipulates the view and model based on the click location.
   */
  @Test
  public void testMouseClicked() {
    scheduleController.launch(model);

    MouseEvent event = new MockMouseEvent(new JButton(), MouseEvent.MOUSE_CLICKED,
            System.currentTimeMillis(), 0, 50, 50, 1, false);

    ((MouseAdapter) scheduleController).mouseClicked(event);

    Assert.assertEquals("This method gets the width of the panel." + System.lineSeparator()
            + "This method gets the height of the panel." + System.lineSeparator()
            + "This method gets the schedule of the panel.", result(5));
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
