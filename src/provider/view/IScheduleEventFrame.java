package provider.view;

/**
 * An interface representing any implementation of a frame meant to be used for scheduling events.
 */
public interface IScheduleEventFrame extends HighLevelView {

  /**
   * Sets the feature listeners to listen for action events.
   * @param features the features listener class
   */
  void setFeatures(IFeatures features);

}

