package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Color;
import java.time.DayOfWeek;
import java.util.List;


import javax.swing.JPanel;

import controller.PlannerSystemController;
import schedule.ISchedule;
import schedule.ReadOnlyEvent;

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

  private ISchedule schedule;

  private PlannerSystemController controller;

  private ColorEvent colorEvent;

  private boolean colorToggled;

  private String firstDayOfWeek;

  /**
   * Constructs a SchedulePanel with the specified schedule.
   */
  public SchedulePanel() {
    this.addMouseListener(new EventClickListener(this));
    this.setBackground(Color.lightGray);
    this.colorEvent = new RedDecorator();
    this.colorToggled = false;
  }

  /**
   * Sets the action listener for the panel.
   *
   * @param listener The action listener to be set.
   */
  public void setActionListener(PlannerSystemController listener) {
    this.controller = listener;
  }

  /**
   * Retrieves the controller associated with this panel.
   *
   * @return The controller associated with this panel.
   */
  public PlannerSystemController getController() {
    return this.controller;
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
  public ISchedule getSchedule() {
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
   * Toggles the color event decorator for this panel, switching between two predefined colors.
   */
  public void setColorEvent() {
    if (colorToggled) {
      this.colorEvent = new RedDecorator();
    } else {
      this.colorEvent = new BlueHostDecorator();
    }
    this.colorToggled = !this.colorToggled;
  }

  /**
   * This method sets the first day of the week for the schedule panel.
   *
   * @param firstDayOfWeek The first day of the week.
   */
  public void setFirstDayOfWeek(String firstDayOfWeek) {
    this.firstDayOfWeek = firstDayOfWeek.toUpperCase();
  }

  /**
   * This method gets the first day of the week for the schedule panel.
   *
   * @return the first day of the week.
   */
  public String getFirstDayOfWeek() {
    return this.firstDayOfWeek;
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
    int columnWidth = (width - cellWidth) / NUM_DAYS;


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
   * @throws IllegalArgumentException if schedule is null.
   */
  public void setSchedule(ISchedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule is null.");
    }
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
    g.setFont(new Font("Aptos", Font.BOLD, cellHeight / 2));
    for (int i = 1; i <= NUM_HOURS; i++) {
      String time = String.format("%02d:00", i - 1);
      g.drawString(time, 5, cellHeight * i + g.getFontMetrics().getAscent() / 2 - 2);
    }
  }

  /**
   * Draws the days of the week at the top of each column.
   *
   * @param g           the Graphics object to draw on.
   * @param cellWidth   the width of the initial time column.
   * @param columnWidth the width of each subsequent day column.
   * @param cellHeight  the height of the top row.
   */
  private void drawDaysOfWeek(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    // Draw the days of the week
    g.setFont(new Font("Aptos", Font.BOLD, cellWidth / 9));
    int difference = DayOfWeek.valueOf(firstDayOfWeek).getValue() % 7;
    for (int i = 0; i < NUM_DAYS; i++) {
      int index = (i + difference) % 7;
      g.drawString(daysOfWeek[index], cellWidth * (i + 1)
                      + (columnWidth - g.getFontMetrics().stringWidth(daysOfWeek[i])) / 2,
              cellHeight - 10);
    }
  }

  /**
   * Draws the grid lines for the schedule panel, including horizontal hour lines and vertical day
   * lines.
   *
   * @param g          the Graphics object to draw on.
   * @param width      the full width of the panel.
   * @param height     the full height of the panel.
   * @param cellWidth  the width of a single vertical grid cell.
   * @param cellHeight the height of a single horizontal grid cell.
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
    for (int i = 0; i <= NUM_DAYS + 1; i++) {
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
    List<ReadOnlyEvent> events = schedule.getEvents();
    Color defaultColor = g.getColor(); // Store the default color

    for (ReadOnlyEvent event : events) {
      int startHour = event.getStartTime() / 100;
      int startMinute = event.getStartTime() % 100;
      int startDayIndex = (event.getStartDay().getValue() + this.difference()) % 7;
      int endHour = event.getEndTime() / 100;
      int endMinute = event.getEndTime() % 100;
      int endDayIndex = (event.getEndDay().getValue() + this.difference()) % 7;

      if (event.wrapsAround(this.firstDayOfWeek)) {
        endHour = 23;
        endMinute = 59;
        endDayIndex = 6;
      }

      // Calculate start and end positions in the grid
      int startX = cellWidth * (startDayIndex + 1); // +1 to account for time column
      int endX = cellWidth * (endDayIndex + 2); // +2 to go to the end of the day slot
      int startY = cellHeight * (startHour + 1) + (int) ((double) startMinute / 60 * cellHeight);
      int endY = cellHeight * (endHour + 1) + (int) ((double) endMinute / 60 * cellHeight);

      Color color;
      if (this.schedule.getUserName().equals(event.getHost())) {
        color = colorEvent.getHostColor();
      } else {
        color = colorEvent.getStandardColor();
      }
      // Set the event color
      g.setColor(color); // Using the first color for all events in this example

      // Fill cells with the event color
      for (int x = startX; x < endX; x += cellWidth) {
        int topY = x == startX ? startY : cellHeight;
        int bottomY = (x + cellWidth) >= endX ? endY : (cellHeight * (NUM_HOURS + 1));
        g.fillRect(x, topY, columnWidth, bottomY - topY);
      }
    }

    // Redraw grid lines over the filled cells
    g.setColor(defaultColor);
    drawGridLines(g, cellWidth, cellHeight);
  }

  /**
   * Draws the grid lines on the schedule panel.
   *
   * @param g          The Graphics object to draw on.
   * @param cellWidth  The width of a single cell.
   * @param cellHeight The height of a single cell.
   */
  private void drawGridLines(Graphics g, int cellWidth, int cellHeight) {
    for (int i = 0; i <= NUM_DAYS; i++) { // 7 days + 1 time column
      int x = i * cellWidth;
      g.drawLine(x, 0, x, this.getHeight());
    }
    for (int i = 0; i <= NUM_HOURS; i++) { // 24 hours
      int y = i * cellHeight;
      g.drawLine(0, y, this.getWidth(), y);
    }
  }

  /**
   * This method gets the difference between the start day index and 7.
   *
   * @return the difference between 7 and start day index.
   */
  private int difference() {
    return 7 - DayOfWeek.valueOf(firstDayOfWeek).getValue();
  }
}
