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


//TODO make plannerSystem more user friendly by changing list of users to list of strings for methods
public class NUPlannerSystem implements PlannerSystem {
  private final Map<String, Schedule> users;

  public NUPlannerSystem() {
    this.users = new HashMap<>();
  }

  @Override
  public void readUserSchedule(File xmlFile) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(xmlFile);
      document.getDocumentElement().normalize();
      Element scheduleElement = document.getDocumentElement();
      String id = scheduleElement.getAttribute("id");
      Schedule schedule = this.users.getOrDefault(id, new Schedule(id));
      processXmlDocument(document, schedule);
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Error in creating the builder");
    } catch (IOException ioEx) {
      throw new IllegalStateException("Error in opening the file");
    } catch (SAXException saxEx) {
      throw new IllegalStateException("Error in parsing the file");
    }
  }

  @Override
  public void saveUserSchedule(String userID) {

  }

  @Override
  public String displayUserSchedule(String userId) {
    return null;
  }

  @Override
  public void createEvent(String userId, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<String> invitees) {

  }

  @Override
  public void modifyEvent(String userId, Event event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<String> invitees) {

  }

  @Override
  public void modifyEvent(String userId, Event event, String name) {

  }

  @Override
  public void modifyEvent(String userId, Event event, String startDay, String startTime,
                          String endDay, String endTime) {

  }

  @Override
  public void modifyEvent(String userId, Event event, boolean isOnline, String location) {

  }

  @Override
  public void modifyEvent(String userId, Event event, List<String> invitees) {

  }

  @Override
  public void removeEvent(String userId, Event event) {

  }

  @Override
  public void automaticScheduling(String time) {

  }

  @Override
  public String showEvent(String userId, String startDay, String startTime, String endDay,
                          String endTime) {
    return null;
  }

  private void processXmlDocument(Document document, Schedule userSchedule) {
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
   * Parses an event from an XML element and constructs an Event object.
   * Sets the host of the event to the first invitee read from the XML.
   *
   * @param eventElement The XML Element representing an event.
   * @return An Event object populated with the details from the XML Element.
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
    event.setLocation(online, place); // Assuming setLocation method exists

    // Parse and add invitees
    NodeList userIds = ((Element) eventElement.getElementsByTagName("users").item(0))
            .getElementsByTagName("uid");
    for (int j = 0; j < userIds.getLength(); j++) {
      String uid = userIds.item(j).getTextContent();
      // Add the first invitee as the host
      if (j == 0) {
        event.setHost(uid); // Assuming setHost method exists
      }
      event.addInvitee(uid);
    }

    return event;
  }

  private void addEventToSchedules(Event event) {
    for (String user: event.getInvitees()) {
      Schedule schedule = users.getOrDefault(user, new Schedule(user));
      schedule.addEvent(event);
      if (!users.containsKey(user)) {
        users.put(user, schedule);
      }
    }

  }

  private void validateEventTime(Event event) {
    for (String user: event.getInvitees()) {
      if (users.containsKey(user)) {
        if (users.get(user).overlap(event)) {
          throw new IllegalArgumentException("There is a time conflict in " + user + " schedule.");
        }
      }
    }
  }

}
