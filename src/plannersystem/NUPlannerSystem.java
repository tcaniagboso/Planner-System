package plannersystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.Map;

import schedule.Event;
import schedule.Schedule;
import schedule.ScheduleXMLWriter;
import schedule.TimeUtilities;
import scheduleview.ScheduleView;
import scheduleview.ScheduleViewModel;
import validationutilities.ValidationUtilities;

/**
 * Implements the PlannerSystem interface to manage users and their schedules.
 * This class provides functionality to read, create, modify, and display user schedules
 * and events from XML files.
 */
public class NUPlannerSystem implements PlannerSystem {
  private final Map<String, Schedule> users;

  /**
   * Constructs a new NUPlannerSystem instance with an empty map of users.
   */
  public NUPlannerSystem() {
    this.users = new HashMap<>();
  }


  @Override
  public void readUserSchedule(File xmlFile) {
    ValidationUtilities.validateNull(xmlFile);
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(xmlFile);
      document.getDocumentElement().normalize();
      processXmlDocument(document);
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Error in creating the builder");
    } catch (IOException ioEx) {
      throw new IllegalStateException("Error in opening the file");
    } catch (SAXException saxEx) {
      throw new IllegalStateException("Error in parsing the file");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void saveUserSchedule(String userId) {
    this.validateUserExists(userId);
    String fileName = userId.toLowerCase() + ".xml";
    try {
      ScheduleXMLWriter.writeScheduleToXML(this.getSchedule(userId), fileName);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public String displayUserSchedule(String userId) {
    this.validateUserExists(userId);
    ScheduleView scheduleView = new ScheduleViewModel();
    return scheduleView.render(this.getSchedule(userId));
  }

  @Override
  public void createEvent(String userId, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<String> invitees) {
    Event newEvent = new Event();
    newEvent.setName(name);
    newEvent.setEventTimes(startDay, startTime, endDay, endTime);
    newEvent.setLocation(isOnline, location);
    newEvent.setHost(userId);
    newEvent.setInvitees(invitees);
    this.validateEventTime(newEvent);
    this.addEventToSchedules(newEvent);
  }

  @Override
  public void modifyEvent(String userId, Event event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<String> invitees) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    // Backup the original state
    String oldStartDay = TimeUtilities.formatDay(event.getTime().getStartDay());
    String oldEndDay = TimeUtilities.formatDay(event.getTime().getEndDay());
    String oldStartTime = TimeUtilities.formatTime(event.getTime().getStartTime());
    String oldEndTime = TimeUtilities.formatTime(event.getTime().getEndTime());
    String oldName = event.getName();
    String oldHost = event.getHost();
    boolean oldOnline = event.getLocation().isOnline();
    String oldPlace = event.getLocation().getLocation();
    List<String> oldInvitees = event.getInvitees();

    // Remove the event from all schedules
    removeEventFromSchedules(event);

    try {
      // Modify the event
      event.setName(name);
      event.setEventTimes(startDay, startTime, endDay, endTime);
      event.setLocation(isOnline, location);
      event.setInvitees(new ArrayList<>(invitees));
      // Re-validate and add the modified event to schedules
      this.validateEventTime(event);
      this.addEventToSchedules(event);
    } catch (IllegalArgumentException e) {
      event.setName(oldName);
      event.setEventTimes(oldStartDay, oldStartTime, oldEndDay, oldEndTime);
      event.setHost(oldHost);
      event.setLocation(oldOnline, oldPlace);
      event.setInvitees(oldInvitees);
      this.addEventToSchedules(event);
      throw e;
    }
  }

  @Override
  public void modifyEvent(String userId, Event event, String name) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    String oldName = event.getName();

    try {
      event.setName(name);
    } catch (IllegalArgumentException e) {
      event.setName(oldName);
      throw e;
    }
  }

  @Override
  public void modifyEvent(String userId, Event event, String startDay, String startTime,
                          String endDay, String endTime) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    // Backup the original state
    String oldStartDay = TimeUtilities.formatDay(event.getTime().getStartDay());
    String oldEndDay = TimeUtilities.formatDay(event.getTime().getEndDay());
    String oldStartTime = TimeUtilities.formatTime(event.getTime().getStartTime());
    String oldEndTime = TimeUtilities.formatTime(event.getTime().getEndTime());

    removeEventFromSchedules(event); // Remove the event

    try {
      event.setEventTimes(startDay, startTime, endDay, endTime); // Modify the event

      this.validateEventTime(event); // Re-validate
      this.addEventToSchedules(event); // Re-add the modified event
    } catch (IllegalArgumentException e) {
      event.setEventTimes(oldStartDay, oldStartTime, oldEndDay, oldEndTime);
      this.addEventToSchedules(event); // Re-add the original event
      throw e;
    }
  }

  @Override
  public void modifyEvent(String userId, Event event, boolean isOnline, String location) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    boolean oldOnline = event.getLocation().isOnline();
    String oldPlace = event.getLocation().getLocation();

    try {
      event.setLocation(isOnline, location);
    } catch (IllegalArgumentException e) {
      event.setLocation(oldOnline, oldPlace);
      throw e;
    }
  }

  @Override
  public void modifyEvent(String userId, Event event, List<String> invitees) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    List<String> oldInvitees = event.getInvitees();
    removeEventFromSchedules(event); // Remove the event

    try {
      event.setInvitees(invitees); // Modify the event

      this.validateEventTime(event); // Re-validate
      this.addEventToSchedules(event); // Re-add the modified event
    } catch (IllegalArgumentException e) {
      event.setInvitees(oldInvitees);
      this.addEventToSchedules(event); // Re-add the original event
      throw e;
    }
  }

  @Override
  public void removeEvent(String userId, Event event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    if (userId.equals(event.getHost())) {
      this.removeEventFromSchedules(event);
    } else {
      this.getSchedule(userId).removeEvent(event);
      event.removeInvitee(userId);
    }
  }

  @Override
  public void automaticScheduling(String userId, String name, boolean isOnline,
                                  String location, List<String> invitees) {

  }

  @Override
  public String showEvent(String userId, String day, String time) {
    this.validateUserExists(userId);
    ValidationUtilities.validateNull(day);
    ValidationUtilities.validateNull(time);
    Event event = this.getSchedule(userId).findEvent(day, time);
    StringBuilder eventString = new StringBuilder();
    if (event == null) {
      eventString.append("No event exists at this time");
    } else {
      eventString.append(event.getName()).append(" happens at this time");
    }
    return eventString.toString();
  }

  public Schedule getSchedule(String userId) {
    this.validateUserExists(userId);
    return users.get(userId.toLowerCase());
  }

  /**
   * Processes the XML document to extract and add events to the system.
   * It iterates through all "event" nodes, parses each to create Event objects,
   * and validates them before adding to the system. If any event fails validation,
   * the process is aborted, and no events are added.
   *
   * @param document The XML Document representing the user's schedule.
   * @throws IllegalArgumentException If event validation fails for any event.
   */
  private void processXmlDocument(Document document) {
    List<Event> tempEvents = new ArrayList<>();
    NodeList eventNodes = document.getElementsByTagName("event");
    boolean validationFailed = false;

    for (int i = 0; i < eventNodes.getLength() && !validationFailed; i++) {
      Node node = eventNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element eventElement = (Element) node;
        Event event = parseEventFromElement(eventElement);

        try {
          this.validateEventTime(event); // Validate the event against all invitees' schedules
          tempEvents.add(event); // Temporarily store the event if it passes validation
        } catch (IllegalArgumentException e) {
          validationFailed = true; // Abort adding events if any validation fails
        }
      }
    }

    // If all events passed validation, add them to the schedules
    if (!validationFailed) {
      for (Event event : tempEvents) {
        addEventToSchedules(event);
      }
    } else {
      throw new IllegalArgumentException("Event validation failed. No events were added.");
    }
  }

  /**
   * Parses an individual event element from the XML document and constructs an Event object.
   * The method extracts event details such as name, time, location, and invitees,
   * and sets the host of the event to the first invitee read from the XML.
   *
   * @param eventElement The XML Element representing an event.
   * @return The constructed Event object populated with details from the XML element.
   */
  private Event parseEventFromElement(Element eventElement) {
    Event event = new Event();

    // Parse and set the event name
    String eventName = eventElement.getElementsByTagName("name").item(0).getTextContent();
    event.setName(eventName);

    // Parse and set event times
    Element timeElement = (Element) eventElement.getElementsByTagName("time").item(0);
    String startDay = timeElement.getElementsByTagName("start-day").item(0).getTextContent();
    String startTime = timeElement.getElementsByTagName("start").item(0).getTextContent();
    String endDay = timeElement.getElementsByTagName("end-day").item(0).getTextContent();
    String endTime = timeElement.getElementsByTagName("end").item(0).getTextContent();
    event.setEventTimes(startDay, startTime, endDay, endTime);

    // Parse and set the event location
    Element locationElement = (Element) eventElement.getElementsByTagName("location").item(0);
    boolean online = Boolean.parseBoolean(locationElement.getElementsByTagName("online")
            .item(0).getTextContent());
    String place = locationElement.getElementsByTagName("place").item(0).getTextContent();
    event.setLocation(online, place);

    // Parse and add invitees
    NodeList userIds = ((Element) eventElement.getElementsByTagName("users").item(0))
            .getElementsByTagName("uid");
    for (int j = 0; j < userIds.getLength(); j++) {
      String uid = userIds.item(j).getTextContent();
      // Add the first invitee as the host
      if (j == 0) {
        event.setHost(uid);
      }
      event.addInvitee(uid);
    }

    return event;
  }

  /**
   * Adds a validated event to the schedules of all its invitees.
   * If the invitee does not have an existing schedule in the system, a new schedule is created.
   *
   * @param event The event to be added to the invitees' schedules.
   */
  private void addEventToSchedules(Event event) {
    for (String user : event.getInvitees()) {
      String userId = user.toLowerCase();
      Schedule schedule = users.getOrDefault(userId, new Schedule(userId));
      if (!schedule.hasEvent(event)) {
        schedule.addEvent(event);
      }
      if (!users.containsKey(userId)) {
        users.put(userId, schedule);
      }
    }

  }

  /**
   * Validates the timing of the event against all invitees' schedules to ensure there are no
   * conflicts.
   * If a time conflict is detected for any invitee, an exception is thrown.
   *
   * @param event The event whose timing is to be validated.
   * @throws IllegalArgumentException If a scheduling conflict is detected.
   */
  private void validateEventTime(Event event) {
    for (String user : event.getInvitees()) {
      if (users.containsKey(user.toLowerCase())) {
        if (this.getSchedule(user).overlap(event)) {
          throw new IllegalArgumentException("There is a time conflict in " + user + " schedule.");
        }
      }
    }
  }

  /**
   * Validates the existence of a user in the system. Throws an exception if the user does not
   * exist.
   *
   * @param userId The ID of the user to validate.
   * @throws IllegalArgumentException If the user does not exist in the system.
   */
  private void validateUserExists(String userId) {
    ValidationUtilities.validateNull(userId);
    if (users.get(userId.toLowerCase()) == null) {
      throw new IllegalArgumentException("User Schedule for " + userId
              + " does not exist in system");
    }
  }

  /**
   * Validates that the event exists in the specified user's schedule.
   *
   * @param userId The ID of the user whose schedule is being checked.
   * @param event  The event to validate for existence.
   * @throws IllegalArgumentException If the event does not exist in the specified user's schedule.
   */
  private void validateEventExists(String userId, Event event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    if (!this.getSchedule(userId).hasEvent(event)) {
      throw new IllegalArgumentException("Event doesn't exist in user " + userId + " schedule.");
    }
  }


  /**
   * Removes the specified event from the schedules of all its invitees.
   * If the event is removed from the host's schedule, it is also removed from all invitees'
   * schedules.
   *
   * @param event The event to be removed.
   */
  private void removeEventFromSchedules(Event event) {
    for (String user : event.getInvitees()) {
      String format = user.toLowerCase();
      if (users.containsKey(format)) {
        users.get(format).removeEvent(event);
        event.removeInvitee(format);
      }
    }
  }
}
