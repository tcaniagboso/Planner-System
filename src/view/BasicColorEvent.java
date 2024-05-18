package view;

import java.awt.Color;

/**
 * The BasicColorEvent class provides a simple implementation of the ColorEvent interface,
 * where both host-specific and standard color settings return the same color.
 * This class is useful for situations where differentiation between host and standard colors
 * is not required, and a uniform color theme is to be maintained.
 */
public class BasicColorEvent implements ColorEvent {

  /**
   * Returns a color for host-specific events.
   * In this implementation, the color red is used for both host-specific and standard elements,
   * simplifying the color scheme.
   *
   * @return The color red, representing both host and standard colors.
   */
  @Override
  public Color getHostColor() {
    return Color.RED;
  }

  /**
   * Returns a standard color for general elements.
   * As with getHostColor(), this method returns red, indicating no distinction in this
   * implementation.
   *
   * @return The color red, used universally for all elements.
   */
  @Override
  public Color getStandardColor() {
    return Color.RED;
  }
}
