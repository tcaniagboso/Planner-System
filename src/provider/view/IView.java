package provider.view;

import schedule.ISchedule;

/**
 * The view of a planner program. It is also the main view interface, parent of the ScheduleFrame
 * implementation (which actually implements the GUI).
 */
public interface IView extends HighLevelView {

  /**
   * Adds the features listeners to the view, ready to listen to events.
   * @param features the features listener class
   */
  void addFeatureListener(IFeatures features);

  /**
   * Updates only the grid panel. Essentially repaints the grid panel (schedule inside).
   * @param schedule the schedule in the grid panel to be repainted
   */
  void updateGridPanel(ISchedule schedule);

  /**
   * Updates the selections in the combo box. Used after a new schedule is added into the system.
   * @param newUserName the new user name that should appear in the combo box.
   */
  void updateScheduleComboBox(String newUserName);
}


