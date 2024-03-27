package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;


import javax.swing.JPanel;

import schedule.Event;
import schedule.Schedule;

/**
 * The SchedulePanel class represents a graphical panel to display schedules.
 * It extends JPanel and provides methods to draw a schedule grid and events on the panel.
 */
public class SchedulePanel extends JPanel {
  private static final int NUM_HOURS = 24;
  private static final int HOUR_INTERVAL = 4;
  private static final int NUM_DAYS = 7;
  private static final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
      "Thursday", "Friday", "Saturday"};

  private Schedule schedule;

  /**
   * Constructs a SchedulePanel with the specified schedule.
   *
   * @param schedule The schedule to be displayed on the panel.
   * @throws IllegalArgumentException if the provided schedule is null.
   */
  public SchedulePanel(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule is null");
    }
    this.schedule = schedule;
    this.setBackground(Color.lightGray);
    addMouseListener(new EventClickListener(this));
  }

  /**
   * Retrieves the width of a single cell in the panel.
   *
   * @return The width of a single cell.
   */
  public int getCellWidth() {
    return this.getWidth() / (NUM_DAYS + 1);
  }

  /**
   * Retrieves the schedule associated with this panel.
   *
   * @return The schedule associated with this panel.
   */
  public Schedule getSchedule() {
    return this.schedule;
  }

  /**
   * Retrieves the height of a single cell in the panel.
   *
   * @return The height of a single cell.
   */
  public int getCellHeight() {
    return this.getHeight() / (NUM_HOURS + 1);
  }

  /**
   * Overrides the paintComponent method to draw the schedule grid and events.
   *
   * @param g The Graphics object to draw on.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int width = this.getWidth();
    int height = this.getHeight();
    int cellHeight = height / (NUM_HOURS + 1); // +1 for the days of the week
    int cellWidth = width / (NUM_DAYS + 1); // +1 for the time column
    int columnWidth = (getWidth() - cellWidth) / NUM_DAYS;


    this.drawTimes(g, cellHeight);

    // Draw the days of the week
    this.drawDaysOfWeek(g, cellWidth, columnWidth, cellHeight);

    this.drawGrid(g, width, height, cellWidth, cellHeight);

    this.drawEvents(g, cellWidth, columnWidth, cellHeight);

  }

  /**
   * Sets the schedule to be displayed on this panel.
   *
   * @param schedule The schedule to be displayed.
   */
  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Draws the times on the leftmost column of the schedule panel.
   *
   * @param g          The Graphics object to draw on.
   * @param cellHeight The height of a single cell.
   */
  private void drawTimes(Graphics g, int cellHeight) {
    // Draw the times on the leftmost column
    g.setFont(new Font("Aptos", Font.BOLD, 15));
    for (int i = 1; i <= NUM_HOURS; i++) {
      String time = String.format("%02d:00", i - 1);
      g.drawString(time, 5, cellHeight * i + g.getFontMetrics().getAscent() / 2 - 2);
    }
  }

  /**
   * Draws the days of the week at the top of the schedule panel.
   *
   * @param g           The Graphics object to draw on.
   * @param cellWidth   The width of a single cell.
   * @param columnWidth The width of a column.
   * @param cellHeight  The height of a single cell.
   */
  private void drawDaysOfWeek(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    // Draw the days of the week
    g.setFont(new Font("Aptos", Font.BOLD, 20));
    for (int i = 0; i < NUM_DAYS; i++) {
      g.drawString(daysOfWeek[i], cellWidth * (i + 1)
                      + (columnWidth - g.getFontMetrics().stringWidth(daysOfWeek[i])) / 2,
              cellHeight - 10);
    }
  }

  /**
   * Draws the grid lines of the schedule panel.
   *
   * @param g          The Graphics object to draw on.
   * @param width      The width of the schedule panel.
   * @param height     The height of the schedule panel.
   * @param cellWidth  The width of a single cell.
   * @param cellHeight The height of a single cell.
   */
  private void drawGrid(Graphics g, int width, int height, int cellWidth, int cellHeight) {
    for (int i = 1; i <= NUM_HOURS + 1; i++) {
      // Every 4 hours, draw a thick line
      if ((i - 1) % HOUR_INTERVAL == 0) {
        ((Graphics2D) g).setStroke(new BasicStroke(2));
      } else {
        ((Graphics2D) g).setStroke(new BasicStroke(1));
      }
      g.drawLine(0, cellHeight * i, width, cellHeight * i);
    }

    // Reset to default stroke
    ((Graphics2D) g).setStroke(new BasicStroke(1));

    // Draw vertical lines
    for (int i = 0; i <= NUM_DAYS; i++) {
      g.drawLine(cellWidth * i, 0, cellWidth * i, height);
    }
  }

  /**
   * Draws the events on the schedule panel.
   *
   * @param g           The Graphics object to draw on.
   * @param cellWidth   The width of a single cell.
   * @param columnWidth The width of a column.
   * @param cellHeight  The height of a single cell.
   */
  private void drawEvents(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    List<Event> events = schedule.getEvents();
    Color defaultColor = g.getColor(); // Store the default color

    // Define an array of colors to be used for events
    Color[] eventColors = {Color.red}; // For simplicity, only using red color for all events

    for (Event event : events) {
      int startHour = event.getTime().getStartTime().getHour();
      int startMinute = event.getTime().getStartTime().getMinute();
      int startDayIndex = event.getTime().getStartDay().getValue() % 7;
      int endHour = event.getTime().getEndTime().getHour();
      int endMinute = event.getTime().getEndTime().getMinute();
      int endDayIndex = event.getTime().getEndDay().getValue() % 7;

      if (event.wrapsAround()) {
        endHour = 23;
        endMinute = 59;
        endDayIndex = 6;
      }

      // Calculate start and end positions in the grid
      int startX = cellWidth * (startDayIndex + 1); // +1 to account for time column
      int endX = cellWidth * (endDayIndex + 2); // +2 to go to the end of the day slot
      int startY = cellHeight * (startHour + 1) + (int) ((double) startMinute / 60 * cellHeight);
      int endY = cellHeight * (endHour + 1) + (int) ((double) endMinute / 60 * cellHeight);

      // Set the event color
      g.setColor(eventColors[0]); // Using the first color for all events in this example

      // Fill cells with the event color
      for (int x = startX; x < endX; x += cellWidth) {
        int topY = x == startX ? startY : cellHeight;
        int bottomY = (x + cellWidth) >= endX ? endY : (cellHeight * (NUM_HOURS + 1));
        g.fillRect(x, topY, columnWidth, bottomY - topY);
      }
    }

    // Redraw grid lines over the filled cells
    g.setColor(defaultColor);
    drawGridLines(g, cellWidth, columnWidth, cellHeight);
  }

  /**
   * Draws the grid lines on the schedule panel.
   *
   * @param g           The Graphics object to draw on.
   * @param cellWidth   The width of a single cell.
   * @param columnWidth The width of a column.
   * @param cellHeight  The height of a single cell.
   */
  private void drawGridLines(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    for (int i = 0; i <= NUM_DAYS; i++) { // 7 days + 1 time column
      int x = i * cellWidth;
      g.drawLine(x, 0, x, this.getHeight());
    }
    for (int i = 0; i <= NUM_HOURS; i++) { // 24 hours
      int y = i * cellHeight;
      g.drawLine(0, y, this.getWidth(), y);
    }
  }

}
