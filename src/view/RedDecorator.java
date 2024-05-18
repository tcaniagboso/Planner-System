package view;

import java.awt.Color;

/**
 * The RedDecorator class is a decorator for ColorEvent that ensures the color red is always
 * returned. It wraps an existing ColorEvent instance and overrides its behavior to return red for
 * both host-specific and standard color requests, regardless of the underlying ColorEvent's
 * implementation.
 */
public class RedDecorator implements ColorEvent {
  private final ColorEvent innerColorable;

  /**
   * Constructs a RedDecorator wrapping a basic color event instance that returns red.
   */
  public RedDecorator() {
    this.innerColorable = new BasicColorEvent();
  }

  /**
   * Returns the color red for host-specific elements.
   *
   * @return The color red.
   */
  @Override
  public Color getHostColor() {
    return innerColorable.getHostColor();
  }

  /**
   * Returns the color red for standard elements.
   *
   * @return The color red.
   */
  @Override
  public Color getStandardColor() {
    return innerColorable.getStandardColor();
  }
}
