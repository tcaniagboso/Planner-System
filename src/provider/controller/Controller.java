package provider.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import plannersystem.PlannerSystem;
import provider.dto.EventData;
import provider.model.ICentralSystem;
import provider.model.NUPlannerSystemAdapter;
import provider.view.EventFrame;
import provider.view.IEventFrame;
import provider.view.IFeatures;
import provider.view.IView;
import provider.view.ScheduleEventFrame;
import provider.view.ScheduleFrame;
import schedule.Event;
import schedule.IEvent;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;
import schedule.Schedule;
import schedule.TimeUtilities;
import schedulestrategy.ScheduleStrategy;

/**
 * The Controller class handles the interaction between the user interface, model, and scheduling
 * strategy
 * for managing events and schedules within the Planner System.
 */
public class Controller implements IController, IFeatures {

  private final ICentralSystem model;
  private final IView view;
  private final ScheduleStrategy strategy;

  /**
   * Constructs a Controller instance with the provided model and scheduling strategy.
   *
   * @param model    The central system model.
   * @param strategy The scheduling strategy to be used.
   * @throws IllegalArgumentException if either model or strategy is null.
   */
  public Controller(ICentralSystem model, ScheduleStrategy strategy) {
    if (model == null || strategy == null) {
      throw new IllegalArgumentException("Invalid arguments");
    }

    this.model = model;
    this.view = new ScheduleFrame(model);
    this.strategy = strategy;
    this.view.addFeatureListener(this);
  }

  @Override
  public void saveScheduleToXML(String userName) {
    // ignored, not used anywhere.
  }

  @Override
  public void addOrUpdateScheduleFromXML(String xmlContent) {
    // ignored, not used anywhere.
  }

  @Override
  public void processXMLFromFile(String filePath) {
    // ignored, not used anywhere.
  }

  @Override
  public void launch() {
    this.view.makeVisible();
  }

  @Override
  public void createEventMain(String selectedHost) {
    if (selectedHost == null) {
      this.displayErrorMessage("No user is selected");
    } else {
      IEventFrame eventFrame = new EventFrame(model, selectedHost, null);
      eventFrame.setFeatures(this);
      eventFrame.makeVisible();
    }
  }

  @Override
  public void scheduleSelect(String selectedUser) {
    if (selectedUser != null) {
      ISchedule schedule = model.getUserSchedule(selectedUser);
      view.updateGridPanel(schedule);
    }
    view.refresh();
  }

  @Override
  public void openEventFrame(ReadOnlyEvent event, String selectedHost) {
    EventFrame eventFrame = new EventFrame(model, selectedHost, event);
    eventFrame.setFeatures(this);
    eventFrame.makeVisible();
  }

  @Override
  public String loadXML(String filePath) {
    if (filePath != null) {
      try {
        File file = new File(filePath);
        PlannerSystem system = ((NUPlannerSystemAdapter) model).getPlannerSystem();
        system.readUserSchedule(file);
        view.refresh();
        return this.getUserNameFromXml(file);
      } catch (Exception e) {
        this.displayErrorMessage(e.getMessage());
        return null;
      }
    }
    return null;
  }

  @Override
  public void saveXML(String userName, String filePath) {
    if (userName != null && filePath != null) {
      try {
        PlannerSystem system = ((NUPlannerSystemAdapter) model).getPlannerSystem();
        system.saveUserSchedule(userName, filePath);
      } catch (Exception e) {
        this.displayErrorMessage("Unable to save " + userName + "'s schedule.");
      }
    }
  }

  @Override
  public void createEvent(EventData eventDetails) {
    try {
      ReadOnlyEvent event = this.convertToEvent(eventDetails);
      this.model.createNewEvent(event);
      this.view.refresh();
    } catch (Exception e) {
      this.displayErrorMessage("Unable to create event. " + e.getMessage());
    }
  }

  @Override
  public void modifyEvent(EventData originalEventDetails, EventData newEventDetails) {
    try {
      ReadOnlyEvent oldEvent = this.convertToEvent(originalEventDetails);
      ReadOnlyEvent newEvent = this.convertToEvent(newEventDetails);
      this.model.modifyEvent(oldEvent, newEvent);
      view.refresh();
    } catch (Exception e) {
      this.displayErrorMessage("Unable to modify event. " + e.getMessage());
    }
  }

  @Override
  public void removeEvent(EventData eventDetails) {
    try {
      ReadOnlyEvent event = this.convertToEvent(eventDetails);
      this.model.hostDeleteEvent(eventDetails.getHost(), event);
      view.refresh();
    } catch (Exception e) {
      this.displayErrorMessage("Unable to remove event. " + e.getMessage());
    }
  }

  @Override
  public void scheduleEvent(EventData eventDetails) {
    IEvent event = new Event();
    int duration = 0;
    try {
      event = (IEvent) this.convertToScheduleEvent(eventDetails);
      duration += eventDetails.getEndTime();
    } catch (Exception e) {
      this.displayErrorMessage(e.getMessage());
    }
    List<ISchedule> schedules = new ArrayList<>();
    for (String user : event.getInvitees()) {
      ISchedule schedule;
      try {
        schedule = this.model.getUserSchedule(user);
      } catch (Exception e) {
        schedule = new Schedule(user);
      }
      schedules.add(schedule);
    }
    try {
      ReadOnlyEvent newEvent = strategy.scheduleEvent(event, duration, schedules);
      if (newEvent != null) {
        this.model.createNewEvent(newEvent);
        view.refresh();
      } else {
        this.displayErrorMessage("Cannot schedule event");
      }
    } catch (Exception e) {
      this.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void scheduleEventMain(String selectedHost) {
    ScheduleEventFrame scheduleEventFrame = new ScheduleEventFrame(model, selectedHost);
    scheduleEventFrame.setFeatures(this);
    scheduleEventFrame.makeVisible();
  }

  /**
   * Displays an error message dialog box with the specified message.
   *
   * @param message The error message to be displayed.
   */
  private void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Retrieves the username from the XML file by parsing its contents.
   *
   * @param file The XML file from which to extract the username.
   * @return The username extracted from the XML file.
   * @throws Exception if there is an error parsing the XML file.
   */
  private String getUserNameFromXml(File file) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

    // Parse the XML file
    Document doc = dBuilder.parse(file);
    doc.getDocumentElement().normalize();

    // Assuming the root element is <schedule> and we need its id attribute
    Element rootElement = doc.getDocumentElement();

    return rootElement.getAttribute("id");
  }

  /**
   * Converts the given EventData object to a ReadOnlyEvent object.
   *
   * @param eventDetails The EventData object to be converted.
   * @return The corresponding ReadOnlyEvent object.
   */
  private ReadOnlyEvent convertToEvent(EventData eventDetails) {
    IEvent event = new Event();
    event.setName(eventDetails.getName());
    String startDay = TimeUtilities.formatDay(eventDetails.getStartDay());
    String startTime = String.format("%04d", eventDetails.getStartTime());
    String endDay = TimeUtilities.formatDay(eventDetails.getEndDay());
    String endTime = String.format("%04d", eventDetails.getEndTime());
    event.setEventTimes(startDay, startTime, endDay, endTime);
    event.setLocation(eventDetails.isOnline(), eventDetails.getLocation());
    event.setHost(eventDetails.getHost());
    List<String> invitees = eventDetails.getInvitees();
    invitees.add(eventDetails.getHost());
    event.setInvitees(invitees);
    return event;
  }

  /**
   * Converts the given EventData object to a ReadOnlyEvent object suitable for scheduling.
   *
   * @param eventData The EventData object to be converted.
   * @return The corresponding ReadOnlyEvent object suitable for scheduling.
   */
  private ReadOnlyEvent convertToScheduleEvent(EventData eventData) {
    IEvent event = new Event();
    event.setName(eventData.getName());
    event.setLocation(eventData.isOnline(), eventData.getLocation());
    event.setHost(eventData.getHost());
    List<String> invitees = eventData.getInvitees();
    invitees.add(eventData.getHost());
    event.setInvitees(invitees);
    return event;
  }
}
