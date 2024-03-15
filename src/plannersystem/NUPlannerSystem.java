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

  /**
   * Reads a user's schedule from an XML file and updates the system's user schedule map.
   * The method parses the XML file to create and add events to the user's schedule.
   *
   * @param xmlFile The XML file containing the user's schedule to be read.
   * @throws IllegalStateException if there's an error creating the document builder, opening the
   *                               file, or parsing the XML.
   * @throws IllegalArgumentException if the XML file contains invalid data that doesn't conform to
   *                                  expected structure.
   */
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
    String fileName = userId + ".xml";
    try {
      ScheduleXMLWriter.writeScheduleToXML(users.get(userId), fileName);
    }
    catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public String displayUserSchedule(String userId) {
    this.validateUserExists(userId);
    ScheduleView scheduleView = new ScheduleViewModel();
    return scheduleView.render(this.users.get(userId));
  }

  /**
   * Creates an event and adds it to the schedule of the specified user and all invitees.
   * Validates event time to prevent schedule conflicts before adding the event.
   *
   * @param userId The user ID of the event host.
   * @param name The name of the event.
   * @param startDay The start day of the event.
   * @param startTime The start time of the event.
   * @param endDay The end day of the event.
   * @param endTime The end time of the event.
   * @param isOnline Indicates whether the event is online.
   * @param location The location of the event (if not online).
   * @param invitees A list of user IDs of the event invitees.
   * @throws IllegalArgumentException if event time conflicts with existing schedule.
   */
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

  /**
   * Modifies an existing event with new details in the schedule of the specified user and
   * all invitees.
   * Validates the modified event time to prevent schedule conflicts.
   *
   * @param userId The user ID of the event host.
   * @param event The event to be modified.
   * @param name The new name of the event.
   * @param startDay The new start day of the event.
   * @param startTime The new start time of the event.
   * @param endDay The new end day of the event.
   * @param endTime The new end time of the event.
   * @param isOnline Indicates whether the event is online.
   * @param location The new location of the event (if not online).
   * @param invitees A new list of user IDs of the event invitees.
   */
  @Override
  public void modifyEvent(String userId, Event event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<String> invitees) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    Event clone = new Event(event);
    clone.setName(name);
    clone.setEventTimes(startDay, startTime, endDay, endTime);
    clone.setLocation(isOnline, location);
    clone.setInvitees(invitees);
    this.validateEventTime(clone);

    event.setName(name);
    event.setEventTimes(startDay, startTime, endDay, endTime);
    event.setLocation(isOnline, location);
    event.setInvitees(invitees);
    this.addEventToSchedules(event);
  }

  /**
   * Modifies the name of a specified event in the user's schedule and updates it across
   * all invitees.
   * Ensures the event exists and is valid before applying the change.
   *
   * @param userId The user ID of the person making the modification. This is used to verify user
   *               existence.
   * @param event The event to be modified.
   * @param name The new name to assign to the event.
   * @throws IllegalArgumentException If the event does not exist in the user's schedule or if the
   *                                  user does not exist.
   */
  @Override
  public void modifyEvent(String userId, Event event, String name) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    Event clone = new Event(event);
    clone.setName(name);
    event.setName(name);

  }

  /**
   * Modifies the time of a specified event in the user's schedule and updates it across all
   * invitees.
   * Validates the new event time to prevent scheduling conflicts before applying the modification.
   *
   * @param userId The user ID of the person making the modification.
   * @param event The event to be modified.
   * @param startDay The new start day for the event.
   * @param startTime The new start time for the event.
   * @param endDay The new end day for the event.
   * @param endTime The new end time for the event.
   * @throws IllegalArgumentException If the event does not exist, the user does not exist, or the
   *                                 new time causes scheduling conflicts.
   */
  @Override
  public void modifyEvent(String userId, Event event, String startDay, String startTime,
                          String endDay, String endTime) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    Event clone = new Event(event);
    clone.setEventTimes(startDay, startTime, endDay, endTime);
    this.validateEventTime(clone);
    event.setEventTimes(startDay, startTime, endDay, endTime);
  }

  /**
   * Modifies the location and online status of a specified event in the user's schedule and
   * updates it across all invitees.
   *
   * @param userId The user ID of the person making the modification.
   * @param event The event to be modified.
   * @param isOnline Specifies whether the event is online.
   * @param location The new location of the event if it's not online.
   * @throws IllegalArgumentException If the event does not exist in the user's schedule or if
   *                                  the user does not exist.
   */
  @Override
  public void modifyEvent(String userId, Event event, boolean isOnline, String location) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    Event clone = new Event(event);
    clone.setLocation(isOnline, location);
    event.setLocation(isOnline, location);

  }

  /**
   * Modifies the list of invitees for a specified event in the user's schedule and updates
   * it across all affected schedules.
   * Validates the updated event to ensure there are no scheduling conflicts with the new list of
   * invitees.
   *
   * @param userId The user ID of the person making the modification.
   * @param event The event to be modified.
   * @param invitees The new list of invitees for the event.
   * @throws IllegalArgumentException If the event does not exist in the user's schedule, the user
   *                                  does not exist, or the modification causes scheduling
   *                                  conflicts.
   */
  @Override
  public void modifyEvent(String userId, Event event, List<String> invitees) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    Event clone = new Event(event);
    clone.setInvitees(invitees);
    this.validateEventTime(clone);
    event.setInvitees(invitees);
    this.addEventToSchedules(event);
  }

  /**
   * Removes an event from the user's schedule. If the user is the host of the event,
   * the event is removed from all invitees' schedules as well. If the user is not the host,
   * the event is only removed from their schedule.
   *
   * @param userId The user ID of the person attempting to remove the event.
   * @param event The event to be removed.
   * @throws IllegalArgumentException If the event does not exist in the user's schedule or
   *                                  if the user does not exist.
   */
  @Override
  public void removeEvent(String userId, Event event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    if (userId.equals(event.getHost())) {
      this.removeEventFromSchedules(event);
    }
    else {
      users.get(userId).removeEvent(event);
      event.removeInvitee(userId);
    }
  }

  @Override
  public void automaticScheduling(String time) {

  }

  @Override
  public String showEvent(String userId, String startDay, String startTime, String endDay,
                          String endTime) {
    return null;
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
      Schedule schedule = users.getOrDefault(user, new Schedule(user));
      if (!schedule.hasEvent(event)) {
        schedule.addEvent(event);
      }
      if (!users.containsKey(user)) {
        users.put(user, schedule);
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
      if (users.containsKey(user)) {
        if (users.get(user).overlap(event)) {
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
    if (users.get(userId) == null) {
      throw new IllegalArgumentException("User Schedule for " + userId
              + " does not exist in system");
    }
  }

  /**
   * Validates that the event exists in the specified user's schedule.
   *
   * @param userId The ID of the user whose schedule is being checked.
   * @param event The event to validate for existence.
   * @throws IllegalArgumentException If the event does not exist in the specified user's schedule.
   */
  private void validateEventExists(String userId, Event event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    if (!this.users.get(userId).hasEvent(event)) {
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
      if (users.containsKey(user)) {
        users.get(user).removeEvent(event);
        event.removeInvitee(user);
      }
    }
  }
}
