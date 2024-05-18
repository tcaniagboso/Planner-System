package plannersystem;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
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
import java.util.Set;

import schedule.IEvent;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedulestrategy.ScheduleStrategy;
import controller.Observer;
import schedule.Event;
import schedule.Schedule;
import scheduleview.ScheduleView;
import scheduleview.ScheduleViewModel;
import validationutilities.ValidationUtilities;

/**
 * Implements the PlannerSystem interface to manage users and their schedules.
 * This class provides functionality to read, create, modify, and display user schedules
 * and events from XML files.
 */
public class NUPlannerSystem implements PlannerSystem {
  private final Map<String, ISchedule> users;
  private final List<Observer> observers = new ArrayList<>();
  private ScheduleStrategy scheduleStrategy;

  private String firstDayOfWeek;

  /**
   * Constructs a new NUPlannerSystem instance with an empty map of users.
   */
  public NUPlannerSystem() {
    this.users = new HashMap<>();
  }

  /**
   * Initializes a new NUPlannerSystem with a list of schedules. It ensures all user schedules
   * are added to the system at instantiation. The constructor validates the input list to ensure
   * it neither is null nor contains null elements, maintaining the integrity of the planner system.
   *
   * @param schedules A list of {@link Schedule} objects, each representing a user's schedule.
   * @throws IllegalArgumentException if `schedules` is null or contains null, indicating an
   *                                  invalid input.
   */
  public NUPlannerSystem(List<ISchedule> schedules) {
    if (schedules == null || schedules.contains(null)) {
      throw new IllegalArgumentException("Invalid list of schedules");
    }
    this.users = new HashMap<>();
    this.addSchedules(schedules);
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
    this.notifyObservers();
  }

  @Override
  public void saveUserSchedule(String userId, String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("File path cannot be null or empty");
    }
    this.validateUserExists(userId);
    try {
      ScheduleXMLWriter.writeScheduleToXML(this.getSchedule(userId), filePath);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public String displayUserSchedule(String userId) {
    this.validateUserExists(userId);
    ScheduleView scheduleView = new ScheduleViewModel(this);
    return scheduleView.render(userId);
  }

  @Override
  public void createEvent(String userId, String name, String startDay, String startTime,
                          String endDay, String endTime, boolean isOnline, String location,
                          List<String> invitees) {
    IEvent newEvent = new Event();
    newEvent.setName(name);
    newEvent.setEventTimes(startDay, startTime, endDay, endTime);
    newEvent.setLocation(isOnline, location);
    newEvent.setHost(userId);
    newEvent.setInvitees(invitees);
    this.validateEventTime(newEvent);
    this.addEventToSchedules(newEvent);
    this.notifyObservers();
  }

  @Override
  public void modifyEvent(String userId, ReadOnlyEvent event, String name, String startDay,
                          String startTime, String endDay, String endTime, boolean isOnline,
                          String location, List<String> invitees) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);

    // Backup the original state
    EventBackup backup = this.backupEventDetails(event);

    // Remove the event from all schedules
    removeEventFromSchedules(event);
    IEvent curEvent = (IEvent) event;
    try {
      // Modify the event
      curEvent.setName(name);
      curEvent.setEventTimes(startDay, startTime, endDay, endTime);
      curEvent.setLocation(isOnline, location);
      curEvent.setHost(event.getHost());
      curEvent.setInvitees(invitees);
      // Re-validate and add the modified event to schedules
      this.validateEventTime(curEvent);
      this.addEventToSchedules(curEvent);
    } catch (Exception e) {
      this.restoreEventFromBackup(curEvent, backup);
      this.addEventToSchedules(curEvent);
      throw e;
    }
    this.notifyObservers();
  }

  @Override
  public void removeEvent(String userId, ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    this.validateEventExists(userId, event);
    ReadOnlyEvent originalEvent = this.users.get(userId).getEvent(event);
    if (userId.equals(originalEvent.getHost())) {
      this.removeEventFromSchedules(originalEvent);
    } else {
      this.getSchedule(userId).removeEvent(originalEvent);
      List<String> invitees = originalEvent.getInvitees();
      this.removeEventFromSchedules(originalEvent);
      invitees.remove(userId);
      ((IEvent) originalEvent).setInvitees(invitees);
      this.addEventToSchedules(originalEvent);
    }
    this.notifyObservers();
  }

  @Override
  public void scheduleEvent(String userId, String name, boolean isOnline, String location,
                            int duration, List<String> invitees) {
    if (scheduleStrategy == null) {
      throw new IllegalStateException("Schedule strategy is not set");
    }

    this.scheduleStrategy.setFirstDayOfWeek(this.firstDayOfWeek);
    IEvent newEvent = new Event();
    newEvent.setName(name);
    newEvent.setLocation(isOnline, location);
    newEvent.setHost(userId);
    newEvent.setInvitees(invitees);

    // Retrieve schedules for all invitees to check for availability
    List<ISchedule> scheduleList = this.getSchedules(newEvent);

    // Attempt to schedule the event using the current scheduling strategy
    ReadOnlyEvent scheduled = this.scheduleStrategy.scheduleEvent(newEvent, duration, scheduleList);

    // If a suitable time slot is found, add the event to schedules; otherwise, throw an exception
    if (scheduled != null) {
      this.addEventToSchedules(scheduled);
    } else {
      throw new IllegalArgumentException("No available time to schedule this event");
    }
    this.notifyObservers();
  }

  @Override
  public void addObserver(Observer observer) {
    ValidationUtilities.validateNull(observer);
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    ValidationUtilities.validateNull(observer);
    observers.remove(observer);
  }

  @Override
  public void setScheduleStrategy(ScheduleStrategy scheduleStrategy) {
    ValidationUtilities.validateNull(scheduleStrategy);
    this.scheduleStrategy = scheduleStrategy;
  }

  @Override
  public void addSchedule(ISchedule schedule) {
    ValidationUtilities.validateNull(schedule);
    String userId = schedule.getUserName();
    if (users.containsKey(userId)) {
      throw new IllegalArgumentException("A schedule for this user already exists");
    }

    List<ReadOnlyEvent> events = schedule.getEvents();
    for (ReadOnlyEvent event : events) {
      this.validateEventTime(event);
    }

    for (ReadOnlyEvent event: events) {
      this.addEventToSchedules(event);
    }
  }

  @Override
  public void addUser(String userId) {
    ValidationUtilities.validateNull(userId);
    if (this.users.containsKey(userId)) {
      throw new IllegalArgumentException("User already exists.");
    }

    this.users.put(userId, new Schedule(userId));
  }

  @Override
  public boolean removeUser(String userId) {
    ValidationUtilities.validateNull(userId);
    if (!this.users.containsKey(userId)) {
      return false;
    }
    List<ReadOnlyEvent> events = this.users.get(userId).getEvents();
    for (ReadOnlyEvent event : events) {
      this.removeEvent(userId, event);
    }
    this.users.remove(userId);
    return true;
  }

  @Override
  public void setFirstDayOfWeek(String firstDayOfWeek) {
    ValidationUtilities.validateNull(firstDayOfWeek);
    try {
      DayOfWeek day = DayOfWeek.valueOf(firstDayOfWeek.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Invalid day");
    }
    this.firstDayOfWeek = firstDayOfWeek.toUpperCase();
  }

  @Override
  public String showEvent(String userId, String day, String time) {
    this.validateUserExists(userId);
    ValidationUtilities.validateNull(day);
    ValidationUtilities.validateNull(time);
    ReadOnlyEvent event = this.getSchedule(userId).findEvent(day, time, firstDayOfWeek);
    StringBuilder eventString = new StringBuilder();
    if (event == null) {
      eventString.append("No event exists at this time");
    } else {
      eventString.append(event.getName()).append(" happens at this time");
    }
    return eventString.toString();
  }

  @Override
  public ISchedule getSchedule(String userId) {
    this.validateUserExists(userId);
    return users.get(userId);
  }

  @Override
  public Set<String> getUsers() {
    return users.keySet();
  }

  @Override
  public boolean checkEventConflict(ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    try {
      this.validateEventTime(event);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public String getFirstDayOfWeek() {
    ValidationUtilities.validateGetNull(this.firstDayOfWeek);
    return this.firstDayOfWeek;
  }

  /**
   * Notifies all the model observers when the model has been mutated.
   */
  private void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
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
    List<ReadOnlyEvent> tempEvents = new ArrayList<>();
    NodeList eventNodes = document.getElementsByTagName("event");
    boolean validationFailed = false;

    for (int i = 0; i < eventNodes.getLength() && !validationFailed; i++) {
      Node node = eventNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element eventElement = (Element) node;
        ReadOnlyEvent event = parseEventFromElement(eventElement);

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
      for (ReadOnlyEvent event : tempEvents) {
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
  private ReadOnlyEvent parseEventFromElement(Element eventElement) {
    IEvent event = new Event();

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
  private void addEventToSchedules(ReadOnlyEvent event) {
    List<String> invitees = event.getInvitees();
    for (String user : invitees) {
      ISchedule schedule = users.getOrDefault(user, new Schedule(user));
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
  private void validateEventTime(ReadOnlyEvent event) {
    for (String user : event.getInvitees()) {
      if (users.containsKey(user)) {
        if (this.getSchedule(user).overlap(event, firstDayOfWeek)) {
          throw new IllegalArgumentException("There is a time conflict in " + user + "'s "
                  + "schedule.");
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
   * @param event  The event to validate for existence.
   * @throws IllegalArgumentException If the event does not exist in the specified user's schedule.
   */
  private void validateEventExists(String userId, ReadOnlyEvent event) {
    ValidationUtilities.validateNull(event);
    this.validateUserExists(userId);
    if (!this.getSchedule(userId).hasEvent(event)) {
      throw new IllegalArgumentException("Event doesn't exist in user " + userId + "'s schedule.");
    }
  }


  /**
   * Removes a specified event from the schedules of all its invitees, effectively canceling the
   * event for those users. After removal, the event's list of invitees is cleared to reflect that
   * it no longer has any attendees.
   *
   * @param event The event to be removed from all associated schedules.
   */
  private void removeEventFromSchedules(ReadOnlyEvent event) {
    List<String> invitees = new ArrayList<>(event.getInvitees());
    for (String user : invitees) {
      if (users.containsKey(user)) {
        users.get(user).removeEvent(event);
      }
    }
    ((IEvent) event).clearInvitees();
  }

  /**
   * Adds a collection of schedules to the system. Each schedule is associated with a unique user.
   * This method is intended for initializing the system with a predefined set of user schedules.
   *
   * @param schedules A list of {@link ISchedule} objects to be added to the system.
   */
  private void addSchedules(List<ISchedule> schedules) {
    for (ISchedule schedule : schedules) {
      users.put(schedule.getUserName(), schedule);
    }
  }

  /**
   * Creates a backup of the current state of an event. This backup includes all relevant details
   * of the event such as its name, timing, location, host, and invitees. The backup is used to
   * restore the event's state in case of a failure during modification.
   *
   * @param event The {@link Event} to be backed up.
   * @return An {@link EventBackup} object containing the backed-up event details.
   */
  private EventBackup backupEventDetails(ReadOnlyEvent event) {
    return new EventBackup(event);
  }

  /**
   * Restores an event's details from a backup. This method is typically called to revert changes
   * to an event after a failed modification attempt, ensuring the event's state is consistent with
   * its state prior to the modification.
   *
   * @param event  The {@link Event} whose details are to be restored.
   * @param backup The {@link EventBackup} from which to restore the event's details.
   */
  private void restoreEventFromBackup(IEvent event, EventBackup backup) {
    event.setName(backup.getName());
    event.setEventTimes(backup.getStartDay(), backup.getStartTime(), backup.getEndDay(),
            backup.getEndTime());
    event.setHost(backup.getHost());
    event.setLocation(backup.isOnline(), backup.getPlace());
    event.setInvitees(backup.getInvitees());
  }

  /**
   * Retrieves a list of schedules for all invitees of a given event. If an invitee does not have an
   * existing schedule, a new schedule is created and returned for that user. This method ensures
   * that every invitee of the event is accounted for, with their respective schedule
   * (existing or new) included in the returned list.
   *
   * @param event The event for which to retrieve the invitees' schedules. Assumes the event is not
   *              null and contains a valid list of invitees.
   * @return A list of {@link Schedule} objects corresponding to each invitee of the event. The list
   *         contains either existing schedules from the 'users' map or newly created schedules for
   *         invitees without an existing schedule.
   */
  private List<ISchedule> getSchedules(ReadOnlyEvent event) {
    List<ISchedule> result = new ArrayList<>();
    for (String user : event.getInvitees()) {
      result.add(users.getOrDefault(user, new Schedule(user)));
    }
    return result;
  }
}
