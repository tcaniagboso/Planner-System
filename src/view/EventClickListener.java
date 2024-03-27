package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

import schedule.Event;
import schedule.Schedule;

/**
 * Listens for mouse click events on the SchedulePanel to handle event selection.
 */
public class EventClickListener extends MouseAdapter {

  private final SchedulePanel schedulePanel;

  /**
   * Constructs an EventClickListener with the specified SchedulePanel.
   *
   * @param schedulePanel The SchedulePanel to listen for mouse click events.
   */
  public EventClickListener(SchedulePanel schedulePanel) {
    this.schedulePanel = schedulePanel;
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
    for (int i = startDayIndex; i <= endDayIndex; i++) {
      int startX = cellWidth * (i + 1);
      int endX = cellWidth * (i + 2);
      if (i == startDayIndex && i == endDayIndex) {
        if (isInRange(startX, endX, startY, endY, mouseX, mouseY)) {
          new EventView(event, userId);
          return;
        }
      } else if (i == startDayIndex) {
        if (isInRange(startX, endX, startY, cellHeight * 25, mouseX, mouseY)) {
          new EventView(event, userId);
          return;
        }
      } else if (i == endDayIndex) {
        if (isInRange(startX, endX, cellHeight, endY, mouseX, mouseY)) {
          new EventView(event, userId);
          return;
        }
      } else {
        if (isInRange(startX, endX, cellHeight, cellHeight * 25, mouseX, mouseY)) {
          new EventView(event, userId);
          return;
        }
      }
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
}
