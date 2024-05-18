package view;

import java.awt.Color;

/**
 * The BlueHostDecorator class is a decorator for ColorEvent that modifies the host-specific color
 * to blue, while maintaining the standard color behavior of the wrapped ColorEvent instance.
 * It is particularly useful when a distinction is needed between host-specific and general UI
 * elements.
 */
public class BlueHostDecorator implements ColorEvent {
  private final ColorEvent innerColorable;

  /**
   * Constructs a BlueHostDecorator wrapping a basic color event instance.
   */
  public BlueHostDecorator() {
    this.innerColorable = new BasicColorEvent();
  }

  /**
   * Returns the color blue for host-specific elements, overriding the default behavior
   * of the wrapped ColorEvent instance.
   *
   * @return The color blue, representing host-specific events.
   */
  @Override
  public Color getHostColor() {
    return Color.BLUE;
  }

  /**
   * Returns the standard color as determined by the wrapped ColorEvent instance.
   * This allows for consistent standard color behavior across different decorators.
   *
   * @return The standard color as defined by the underlying ColorEvent.
   */
  @Override
  public Color getStandardColor() {
    return innerColorable.getStandardColor();
  }
}
