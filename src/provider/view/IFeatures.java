package provider.view;


import provider.dto.EventData;
import schedule.ReadOnlyEvent;

/**
 * A list of all the features in the program the User is capable of using and interacting with.
 * It is part of the Command Callback Design pattern and used to adequately separate the view from
 * the controller.
 */
public interface IFeatures {

  //Opens a blank event frame
  void createEventMain(String selectedHost);

  //Selects a person's schedule in the combobox
  void scheduleSelect(String selectedUser);

  //Opens an event frame with all of the information from the clicked event
  void openEventFrame(ReadOnlyEvent event, String selectedHost);

  //Loads in an XML file into the schedule frame
  String loadXML(String filePath);

  //Saves the current Schedule into ur desktop
  void saveXML(String userName, String filePath);

  // Creates a new event with the given details
  void createEvent(EventData eventDetails);

  // Modifies an existing event, identified by the original details, to have new details
  void modifyEvent(EventData originalEventDetails, EventData newEventDetails);

  // Removes an event with the specified details
  void removeEvent(EventData eventDetails);

  //Called when you click the button in the schedule frame to open the scheduling frame
  void scheduleEvent(EventData eventDetails);

  //Actually schedules the event using the strategy in the program arguments
  void scheduleEventMain(String selectedHost);

}
