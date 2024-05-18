package provider.view;


/**
 * The interface for the event frame. This interface is used to ensure that the frame is closed
 * properly.
 */
public interface IEventFrame extends HighLevelView {


  /**
   * Sets the features for the event frame.
   *
   * @param features The features to be set.
   */
  void setFeatures(IFeatures features);

}
