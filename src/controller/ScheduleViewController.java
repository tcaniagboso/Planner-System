package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

import command.AddCalendar;
import command.Command;
import command.OpenExistingEvent;
import command.OpenNewEvent;
import command.OpenScheduleEvent;
import command.SaveCalendars;
import command.SelectUser;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;
import view.PlannerSystemView;
import view.SchedulePanel;

/**
 * Controller class for managing user actions and mouse events in the Planner System application.
 * This class listens for actions performed on the GUI and responds to mouse clicks on the schedule
 * panel.
 * It bridges the interaction between the view ({@link PlannerSystemView}) and the model
 * ({@link PlannerSystem}).
 */
public class ScheduleViewController extends MouseAdapter implements PlannerSystemController,
        ActionListener, Observer {

  private final PlannerSystemView view;
  private PlannerSystem model;
  private final SchedulePanel schedulePanel;

  /**
   * Constructs a ScheduleViewController with a specified view.
   * Initializes the controller, sets itself as the listener for action and mouse events.
   *
   * @param view The {@link PlannerSystemView} for this controller to manage.
   * @throws IllegalArgumentException if the provided view is null.
   */
  public ScheduleViewController(PlannerSystemView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    this.schedulePanel = this.view.getSchedulePanel();
    this.view.setActionListener(this);
    this.view.setMouseListener(this);
  }

  @Override
  public void launch(PlannerSystem model) {
    this.setModel(model);
    this.view.makeVisible();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String currentUser = this.view.getCurrentUser();
    assert currentUser != null;
    String action = e.getActionCommand();
    Command command = null;
    switch (action) {
      case "Create event":
        command = new OpenNewEvent(currentUser, model);
        break;
      case "Schedule event":
        command = new OpenScheduleEvent(currentUser);
        break;
      case "Add calendar":
        command = new AddCalendar(view, model);
        break;
      case "Save calendars":
        command = new SaveCalendars(currentUser, view, model);
        break;
      case "Select user":
        command = new SelectUser(currentUser, view);
        break;
    }

    if (command != null) {
      try {
        command.execute();
      } catch (Exception ise) {
        this.view.displayErrorMessage(ise.getMessage());
      }
    }
    this.view.refresh();
  }

  @Override
  public void update() {
    this.view.updateUsers();
    this.view.refresh();
  }

  /**
   * Handles mouseClicked event by processing the mouse event.
   *
   * @param e The MouseEvent that triggered the event.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    processMouseEvent(e);
  }

  /**
   * Processes the mouse event to determine if an event is clicked.
   *
   * @param e The MouseEvent to process.
   */
  private void processMouseEvent(MouseEvent e) {
    int cellWidth = schedulePanel.getCellWidth();
    int cellHeight = schedulePanel.getCellHeight();
    Schedule schedule = schedulePanel.getSchedule();
    double mouseX = e.getX();
    double mouseY = e.getY();

    for (Event event : schedule.getEvents()) {
      int startDayIndex = (event.getTime().getStartDay().getValue() % 7);
      int endDayIndex = (event.getTime().getEndDay().getValue() % 7);
      LocalTime startTime = event.getTime().getStartTime();
      LocalTime endTime = event.getTime().getEndTime();
      double startY = calculateYPosition(startTime, cellHeight);
      double endY = calculateYPosition(endTime, cellHeight);

      if (event.wrapsAround()) {
        endDayIndex = 6;
        endY = calculateYPosition(LocalTime.of(23, 59), cellHeight);
      }
      checkAndHandleEventIntersection(event, startDayIndex, endDayIndex, cellWidth, cellHeight,
              mouseX, mouseY, startY, endY);
    }
  }

  /**
   * Calculates the Y position of an event based on its start time and cell height.
   *
   * @param time       The start time of the event.
   * @param cellHeight The height of a single cell.
   * @return The Y position of the event.
   */
  private double calculateYPosition(LocalTime time, int cellHeight) {
    return (cellHeight * (time.getHour() + 1)) + (cellHeight * ((double) time.getMinute() / 60));
  }

  /**
   * Checks if the mouse click intersects with an event and handles it accordingly.
   *
   * @param event         The event to check intersection with.
   * @param startDayIndex The start day index of the event.
   * @param endDayIndex   The end day index of the event.
   * @param cellWidth     The width of a single cell.
   * @param cellHeight    The height of a single cell.
   * @param mouseX        The X coordinate of the mouse click.
   * @param mouseY        The Y coordinate of the mouse click.
   * @param startY        The Y position of the event start.
   * @param endY          The Y position of the event end.
   */
  private void checkAndHandleEventIntersection(Event event, int startDayIndex, int endDayIndex,
                                               int cellWidth, int cellHeight, double mouseX,
                                               double mouseY, double startY, double endY) {
    String userId = schedulePanel.getSchedule().getUserId();
    boolean found = false;
    for (int i = startDayIndex; i <= endDayIndex; i++) {
      int startX = cellWidth * (i + 1);
      int endX = cellWidth * (i + 2);
      if (i == startDayIndex && i == endDayIndex) {
        if (isInRange(startX, endX, startY, endY, mouseX, mouseY)) {
          found = true;
          break;
        }
      } else if (i == startDayIndex) {
        if (isInRange(startX, endX, startY, cellHeight * 25, mouseX, mouseY)) {
          found = true;
          break;
        }
      } else if (i == endDayIndex) {
        if (isInRange(startX, endX, cellHeight, endY, mouseX, mouseY)) {
          found = true;
          break;
        }
      } else {
        if (isInRange(startX, endX, cellHeight, cellHeight * 25, mouseX, mouseY)) {
          found = true;
          break;
        }
      }
    }
    if (found) {
      Command command = new OpenExistingEvent(userId, event, model);
      command.execute();
    }
  }

  /**
   * Checks if the mouse click is within the range of an event.
   *
   * @param startX The starting X coordinate of the event.
   * @param endX   The ending X coordinate of the event.
   * @param startY The starting Y coordinate of the event.
   * @param endY   The ending Y coordinate of the event.
   * @param clickX The X coordinate of the mouse click.
   * @param clickY The Y coordinate of the mouse click.
   * @return True if the mouse click is within the range of the event, false otherwise.
   */
  private boolean isInRange(double startX, double endX, double startY, double endY, double clickX,
                            double clickY) {
    return (clickX >= startX && clickX < endX) && (clickY >= startY && clickY < endY);
  }

  private void setModel(PlannerSystem model) {
    if (model == null) {
      throw new IllegalArgumentException("Planner System Model is null");
    }
    this.model = model;
    this.model.addObserver(this);
  }
}
