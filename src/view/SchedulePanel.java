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

public class SchedulePanel extends JPanel {
  private static final int NUM_HOURS = 24;
  private static final int HOUR_INTERVAL = 4;
  private static final int NUM_DAYS = 7;
  private static final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
          "Thursday", "Friday", "Saturday"};

  private Schedule schedule;

  public SchedulePanel(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule is null");
    }
    this.schedule = schedule;
    this.setBackground(Color.lightGray);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int width = this.getWidth();
    int height = this.getHeight();
    int cellHeight = height / (NUM_HOURS + 1); // +1 for the days of the week
    int cellWidth = getWidth() / (NUM_DAYS + 1); // +1 for the time column
    int columnWidth = (getWidth() - cellWidth) / NUM_DAYS;


    this.drawTimes(g, cellHeight);

    // Draw the days of the week
    this.drawDaysOfWeek(g, cellWidth, columnWidth, cellHeight);

    this.drawGrid(g, width, height, cellWidth, cellHeight);

    this.drawEvents(g, cellWidth, columnWidth, cellHeight);

  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  private void drawTimes(Graphics g, int cellHeight) {
    // Draw the times on the leftmost column
    g.setFont(new Font("Aptos", Font.BOLD, 15));
    for (int i = 1; i <= NUM_HOURS; i++) {
      String time = String.format("%02d:00", i - 1);
      g.drawString(time, 5, cellHeight * i + g.getFontMetrics().getAscent() / 2 - 2);
    }
  }

  private void drawDaysOfWeek(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    // Draw the days of the week
    g.setFont(new Font("Aptos", Font.BOLD, 20));
    for (int i = 0; i < NUM_DAYS; i++) {
      g.drawString(daysOfWeek[i], cellWidth * (i + 1)
                      + (columnWidth - g.getFontMetrics().stringWidth(daysOfWeek[i])) / 2,
              cellHeight - 10);
    }
  }

  private void drawGrid(Graphics g, int width, int height, int cellWidth, int cellHeight) {
    for (int i = 1; i <= NUM_HOURS; i++) {
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

  private void drawEvents(Graphics g, int cellWidth, int columnWidth, int cellHeight) {
    List<Event> events = schedule.getEvents();
    Color defaultColor = g.getColor(); // Store the default color

    // Define an array of colors to be used for events
    Color[] eventColors = {Color.blue, Color.red, Color.green, Color.orange,
            Color.magenta, Color.cyan, Color.yellow};

    for (int i = 0; i < events.size(); i++) {
      Event event = events.get(i);

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

      int colorIndex = i % eventColors.length; // Use event index to select color from eventColors array

      for (int j = startDayIndex; j <= endDayIndex; j++) {
        int x = cellWidth * (j + 1) + 2;
        int y = cellHeight * (startHour + 1) + (int) ((double) startMinute / 60 * cellHeight);
        int eventWidth = columnWidth - 2; // Adjust for padding

        // Calculate event height for multi-day events
        int eventHeight;
        if (j == startDayIndex && j == endDayIndex) {
          eventHeight = (endHour - startHour) * cellHeight +
                  (int) (((double) endMinute - startMinute) / 60 * cellHeight);
        } else if (j == startDayIndex) {
          eventHeight = (NUM_HOURS - startHour) * cellHeight;
        } else if (j == endDayIndex) {
          y = cellHeight;
          eventHeight = (endHour) * cellHeight +
                  (int) (((double) endMinute) / 60 * cellHeight);
        } else {
          y = cellHeight;
          eventHeight = NUM_HOURS * cellHeight;
        }

        // Set event color
        g.setColor(eventColors[colorIndex]);

        // Paint the event rectangle
        g.fillRect(x, y, eventWidth, eventHeight);
      }
    }

    // Set the color back to the default color for drawing grid lines
    g.setColor(defaultColor);
  }


}
