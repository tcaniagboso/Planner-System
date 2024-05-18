package view;

import java.awt.Color;

/**
 * The ColorEvent interface provides a contract for implementing color behaviors
 * for various UI components. Implementations of this interface should specify
 * how colors are determined for different contexts, such as distinguishing between
 * host-specific and standard settings.
 */
public interface ColorEvent {

  /**
   * Retrieves the color used for events associated with a host.
   * Implementations should return the specific color that represents the host context.
   *
   * @return The color used for host-related elements.
   */
  Color getHostColor();

  /**
   * Retrieves the standard color used for general events not specifically associated with a host.
   * Implementations should return the default color used across various components where no special
   * context is applied.
   *
   * @return The standard color used for non-host-specific elements.
   */
  Color getStandardColor();

}
