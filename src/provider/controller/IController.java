package provider.controller;

/**
 * An interface for a controller. Luckily this is not being graded now, we just got a little ahead
 * of ourselves.
 */
public interface IController {

  /**
   * Saves the schedule of the given user to an XML file.
   *
   * @param userName the user's name
   * @throws Exception if the schedule is not found
   */
  void saveScheduleToXML(String userName) throws Exception;

  /**
   * Adds or updates a schedule from an XML string.
   *
   * @param xmlContent the XML string representing the schedule
   * @throws Exception if the schedule is not found
   */
  void addOrUpdateScheduleFromXML(String xmlContent) throws Exception;

  /**
   * Processes an XML file and updates the schedule accordingly.
   *
   * @param filePath the path to the XML file
   * @throws Exception if the file is not found
   */
  void processXMLFromFile(String filePath) throws Exception;

  /**
   * Starts the program by making the view visible, therefore allowing Users to start interacting
   * with the program.
   */
  void launch();

}
