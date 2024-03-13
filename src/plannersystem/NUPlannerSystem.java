package plannersystem;

import java.io.File;
import java.io.IOException;
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

import event.Event;
import user.User;

public class NUPlannerSystem implements PlannerSystem {
  private final Map<String, User> users;

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
      User user = assignUser(id);
      processXmlDocument(document, user);
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Error in creating the builder");
    } catch (IOException ioEx) {
      throw new IllegalStateException("Error in opening the file");
    } catch (SAXException saxEx) {
      throw new IllegalStateException("Error in parsing the file");
    }
  }

  private void processXmlDocument(Document document, User user) {
    //confirm if structure has event tags and inside schedule
    NodeList eventNodes = document.getElementsByTagName("event");
    for (int i = 0; i < eventNodes.getLength(); i++) {
      Node node = eventNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element eventElement = (Element) node;
        Event event = new Event();
        event.setName(eventElement.getElementsByTagName("name").item(0).getTextContent());
        Element timeElement = (Element) eventElement.getElementsByTagName("time").item(0);
        String startDay = timeElement.getElementsByTagName("start-day").item(0)
                .getTextContent();
        String start = timeElement.getElementsByTagName("start").item(0).getTextContent();
        String endDay = timeElement.getElementsByTagName("end-day").item(0).getTextContent();
        String end = timeElement.getElementsByTagName("end").item(0).getTextContent();
        event.setEventTimes(startDay, start, endDay, end);

        // Process location
        Element locationElement = (Element) eventElement.getElementsByTagName("location")
                .item(0);
        boolean online = Boolean.parseBoolean(locationElement.getElementsByTagName("online")
                .item(0).getTextContent());
        String place = locationElement.getElementsByTagName("place").item(0).getTextContent();
        event.setLocation(online, place);

        // Process invitees
        NodeList userIds = ((Element) eventElement.getElementsByTagName("users").item(0))
                .getElementsByTagName("uid");
        for (int j = 0; j < userIds.getLength(); j++) {
          String uid = userIds.item(j).getTextContent();
          User invitee = assignUser(uid);
          try {
            invitee.addEvent(event);
            event.addInvitee(invitee);
          } catch (IllegalArgumentException ignored) {
            // TODO: Maybe throw an exception?
          }
        }
        user.addEvent(event);
      }
    }
  }

  @Override
  public void saveUserSchedule() {
  }

  @Override
  public String displayUserSchedule(String userId) {
    this.validateUserID(userId);
    return null;
  }

  @Override
  public void createEvent(String userId, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<User> invitees) {
    this.validateUserID(userId);
    User currentUser = this.users.get(userId);
    Event event = new Event();
    event.setName(name);
    event.setEventTimes(startDay, startTime, endDay, endTime);
    event.setLocation(isOnline, location);
    event.setHost(currentUser);
    invitees.add(currentUser);
    event.setInvitees(invitees);
  }

  @Override
  public void modifyEvent(String userId, Event event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<User> invitees) {
    this.validateUserID(userId);
    this.modifyEvent(userId, event, name);
    this.modifyEvent(userId, event, startDay, startTime, endDay, endTime);
    this.modifyEvent(userId, event, isOnline, location);
    this.modifyEvent(userId, event, invitees);
  }

  @Override
  public void modifyEvent(String userId, Event event, String name) {
    this.validateUserID(userId);
    this.users.get(userId).modifyEvent(event, name);

  }

  @Override
  public void modifyEvent(String userId, Event event, String startDay, String startTime,
                          String endDay, String endTime) {
    this.validateUserID(userId);
    this.users.get(userId).modifyEvent(event, startDay, startTime, endDay, endTime);

  }

  @Override
  public void modifyEvent(String userId, Event event, boolean isOnline, String location) {
    this.validateUserID(userId);
    this.users.get(userId).modifyEvent(event, isOnline, location);

  }

  @Override
  public void modifyEvent(String userId, Event event, List<User> invitees) {
    this.validateUserID(userId);
    this.users.get(userId).modifyEvent(event, invitees);
  }

  @Override
  public void removeEvent(String userId, Event event) {
    this.validateUserID(userId);
    this.users.get(userId).removeEvent(event);
  }

  @Override
  public void automaticScheduling(String time) {

  }

  @Override
  public void showEvents(String userId, String startDay, String startTime, String endDay,
                         String endTime) {
    this.validateUserID(userId);


  }

  private User assignUser(String id) {
    if (id == null) {
      throw new IllegalArgumentException("User Id cannot be null");
    }
    if (users.containsKey(id)) {
      return users.get(id);
    }
    User newUser = new User(id);
    users.put(id, newUser);
    return newUser;
  }

  private void validateUserID(String userID) {
    if (userID == null || userID.isEmpty()) {
      throw new IllegalArgumentException("UserID cannot be null || empty");
    }
    if (!this.users.containsKey(userID)) {
      throw new IllegalArgumentException("User doesn't exist in the system");
    }
  }

  private void addNewUsers(List<User> userList) {
    if (userList == null) {

    }
  }

  private void validateUserList(List<User> userList) {
    if (userList == null || userList.contains(null)) {
      throw new IllegalArgumentException("User List is invalid");
    }
  }
}
