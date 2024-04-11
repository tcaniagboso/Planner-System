import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import controller.EventViewController;
import controller.EventViewControllerImpl;
import plannersystem.PlannerSystem;
import schedule.Event;
import view.EventView;

/**
 * Test class for {@link EventViewController}. It tests the functionality of the controller
 * in managing events within the Planner System application. The class primarily verifies that
 * {@link EventViewControllerImpl} correctly handles action events triggered by the user interface
 * and interacts with both the model ({@link PlannerSystem}) and the view ({@link EventView}) as
 * expected.
 */
public class EventViewControllerTest {

  private EventView eventView;
  private PlannerSystem model;
  private Event event;
  private StringBuilder log;
  private EventViewController controller;

  /**
   * Initializes common objects for testing before each test case. This method sets up a mock model,
   * a mock view, an event instance, and a {@link EventViewController} with logging capabilities to
   * track interactions.
   */
  @Before
  public void init() {
    log = new StringBuilder();
    model = new MockPlannerSystem(log);
    event = new Event();
    eventView = new MockEventView(log);
    controller = new EventViewControllerImpl(eventView, model);
  }

  /**
   * Tests the constructor of {@link EventViewControllerImpl} for handling null arguments.
   * Verifies that passing null for either the view or the model argument throws an
   * {@link IllegalArgumentException}.
   */
  @Test
  public void testConstructor() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new EventViewControllerImpl(null, model));
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new EventViewControllerImpl(eventView, null));
  }

  /**
   * Tests the launch method of the {@link EventViewController}. Verifies that the controller
   * prepares the view for interaction, including setting action listeners and making the view
   * visible, and handles null event arguments appropriately.
   */
  @Test
  public void testLaunch() {
    Assert.assertThrows(IllegalArgumentException.class, () -> controller.launch(null));

    controller.launch(event);

    Assert.assertEquals("This method sets the action listener of the event view to the "
            + "given listener." + System.lineSeparator()
            + "This method makes the event view visible." + System.lineSeparator(), log.toString());
  }

  /**
   * Tests the actionPerformed method of the {@link EventViewController}. Simulates various action
   * events to verify that the controller responds by executing appropriate operations, such as
   * creating, modifying, removing, and scheduling events. The test verifies that the controller
   * interacts correctly with the model and view, and updates the interface accordingly.
   */
  @Test
  public void testActionPerformed() {
    controller.launch(event);

    Object dummySource = new Object();
    ActionEvent createEvent = new MockActionEvent(dummySource, 1, "Create event");
    ActionEvent modifyEvent = new MockActionEvent(dummySource, 2, "Modify event");
    ActionEvent removeEvent = new MockActionEvent(dummySource, 3, "Remove event");
    ActionEvent scheduleEvent = new MockActionEvent(dummySource, 4, "Schedule event");

    ((ActionListener) controller).actionPerformed(createEvent);

    Assert.assertEquals("This method gets the userId of the event view."
            + System.lineSeparator()
            + "This method checks if all the event view fields have been filled."
            + System.lineSeparator()
            + "This method gets the name of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (online?) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the invitees of the event from the event view."
            + System.lineSeparator()
            + "This method creates an event in the system, if possible, otherwise throws an "
            + "Exception." + System.lineSeparator() + "This method refreshes the event view.",
            result(2));

    ((ActionListener) controller).actionPerformed(modifyEvent);
    Assert.assertEquals("This method gets the userId of the event view."
            + System.lineSeparator()
            + "This method checks if all the event view fields have been filled."
            + System.lineSeparator()
            + "This method gets the name of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (online?) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the invitees of the event from the event view."
            + System.lineSeparator()
            + "This method modifies an event in the system, if possible, otherwise throws an "
            + "Exception." + System.lineSeparator()
            + "This method refreshes the event view.", result(14));

    ((ActionListener) controller).actionPerformed(removeEvent);
    Assert.assertEquals("This method gets the userId of the event view."
            + System.lineSeparator()
            + "This method checks if all the event view fields have been filled."
            + System.lineSeparator()
            + "This method gets the name of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the start time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end day of the event from the event view."
            + System.lineSeparator()
            + "This method gets the end time of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (online?) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the invitees of the event from the event view."
            + System.lineSeparator() + "This method removes the event from all the invitees' "
            + "schedules, if userId is the host of the event, otherwise it removes the event from "
            + "only the user's schedule." + System.lineSeparator()
            + "This method refreshes the event view.", result(26));

    ((ActionListener) controller).actionPerformed(scheduleEvent);
    Assert.assertEquals("This method gets the userId of the event view."
            + System.lineSeparator()
            + "This method checks if all the event view fields have been filled."
            + System.lineSeparator()
            + "This method gets the name of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (place) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the location (online?) of the event from the event view."
            + System.lineSeparator()
            + "This method gets the invitees of the event from the event view."
            + System.lineSeparator()
            + "This method gets the duration of the event from the event view."
            + System.lineSeparator()
            + "This method attempts to schedule an event with the given duration and details in "
            + "the system, using the chosen scheduling strategy." + System.lineSeparator()
            + "This method refreshes the event view.", result(38));
  }

  /**
   * A helper method to extract a portion of the interaction log for validation. This method is used
   * to simplify the verification of specific sequences of operations that were logged during the
   * test execution, starting from a specified index within the log.
   *
   * @param start The index from which to begin extraction.
   * @return A string representation of the extracted portion of the log.
   */
  public String result(int start) {
    String[] resultList = log.toString().split(System.lineSeparator());
    return String.join(System.lineSeparator(),
            Arrays.copyOfRange(resultList, start, resultList.length));
  }
}
