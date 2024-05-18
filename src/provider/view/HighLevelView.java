package provider.view;

/**
 * An interface abstracting out all of the commonly used methods a View component will need to
 * use.
 */
public interface HighLevelView {

  /**
   * Sets up the components of the frame.
   */
  void close();

  /**
   * Make the view visible. This is usually called
   * after the view is constructed.
   */
  void makeVisible();

  /**
   * Refreshes the component.
   */
  void refresh();


}
