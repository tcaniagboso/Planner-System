import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * A mock {@link MouseEvent} class for testing purposes.
 * This class allows the simulation of mouse events within unit tests, providing control over
 * the parameters of a mouse event, such as the source component, event type, coordinates,
 * and click count.
 * It extends {@link MouseEvent}, replicating its behavior in a controlled testing environment.
 */
public class MockMouseEvent extends MouseEvent {

  /**
   * Constructs a new {@link MockMouseEvent} instance.
   * Initializes a {@link MouseEvent} with specified details including source component, event type,
   * event time, modifiers, x and y coordinates, click count, and popup trigger flag.
   *
   * @param source       The {@link Component} on which the MouseEvent initially occurred.
   * @param id           The integer that identifies the event type. For example,
   *                     {@link MouseEvent#MOUSE_CLICKED}.
   * @param when         A long that gives the time the event occurred.
   * @param modifiers    The modifier keys down during event (shift, ctrl, alt, meta).
   *                     Passing negative value is not recommended.
   * @param x            The horizontal x coordinate for the mouse location.
   * @param y            The vertical y coordinate for the mouse location.
   * @param clickCount   The number of mouse clicks associated with event.
   * @param popupTrigger A boolean that says whether the event is a trigger for a popup menu.
   */
  public MockMouseEvent(Component source, int id, long when, int modifiers,
                        int x, int y, int clickCount, boolean popupTrigger) {
    super(source, id, when, modifiers, x, y, clickCount, popupTrigger);
  }
}
