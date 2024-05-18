package provider.view;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;

/**
 * This is the class responsible for drawing the entire GridPanel to be used in the main schedule
 * frame. It handles drawing the gridlines accurately and the events inside those gridlines.
 */
public class GridPanel extends JPanel {
  private ISchedule schedule;
  private int colWidth;
  private int rowHeight;
  private final ScheduleFrame scheduleFrame;

  /**
   * This is the constructor of an instance of this grid panel. It ensures that both a schedule and
   * a frame are added to construct this panel.
   *
   * @param schedule the schedule to be drawn
   * @param frame    the actual frame this gridPanel is going into
   */
  public GridPanel(ISchedule schedule, ScheduleFrame frame) {
    setPreferredSize(new Dimension(700, 500));
    this.schedule = Objects.requireNonNull(schedule);
    this.scheduleFrame = Objects.requireNonNull(frame);
    this.addClickListener();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    int width = getWidth();
    int height = getHeight();
    int rows = 24;
    rowHeight = height / rows;
    int cols = 7;
    colWidth = width / cols;
    drawGridlines(g2d, width, height, rows, cols);
    drawEvents(g2d);
  }

  //Draws the gridlines into this panel.
  private void drawGridlines(Graphics2D g2d, int width, int height, int rows, int cols) {
    g2d.setStroke(new BasicStroke(1));
    for (int lineNum = 0; lineNum <= rows; lineNum++) {
      if (lineNum % 4 == 0) {
        g2d.setStroke(new BasicStroke(2));
      } else {
        g2d.setStroke(new BasicStroke(1));
      }
      g2d.drawLine(0, lineNum * rowHeight, width, lineNum * rowHeight);
    }
    g2d.setStroke(new BasicStroke(1));
    for (int lineNum = 0; lineNum <= cols; lineNum++) {
      g2d.drawLine(lineNum * colWidth, 0, lineNum * colWidth, height);
    }
  }

  //This draws the red boxes over the correct days
  private void drawEvents(Graphics2D g2d) {
    Color translucentRed = new Color(255, 0, 0, 128);
    g2d.setColor(translucentRed);
    //loops through the events and draws each of them
    for (ReadOnlyEvent event : schedule.getEvents()) {
      int startDayIdx = event.getStartDay().getValue() % 7;
      int endDayIdx = event.getEndDay().getValue() % 7;
      //if the event is multi-week, it sets the last day to saturday auto
      if (endDayIdx < startDayIdx) {
        endDayIdx = 6;
      }
      //draws the boxes
      for (int currentDay = startDayIdx; currentDay <= endDayIdx; currentDay++) {
        int x = currentDay * colWidth;
        int startY;
        int endY;
        if (currentDay == startDayIdx) {
          int startHr = event.getStartTime() / 100;
          int startMins = event.getStartTime() % 100;
          startY = (startHr * rowHeight) + (startMins * rowHeight / 60);
        } else {
          startY = 0;
        }
        if (currentDay == endDayIdx && endDayIdx == event.getEndDay().getValue() % 7) {
          int endHour = event.getEndTime() / 100;
          int endMinutes = event.getEndTime() % 100;
          endY = (endHour * rowHeight) + (endMinutes * rowHeight / 60);
        } else {
          endY = 24 * rowHeight;
        }
        int eventHeight = endY - startY;
        g2d.fillRect(x, startY, colWidth, eventHeight);
      }
    }
  }


  private void addClickListener() {
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        handleEventClick(event.getX(), event.getY());
      }
    });
  }

  private void handleEventClick(int x, int y) {
    int dayIdx = x / colWidth;
    int clickedMinutes = (y * 60) / rowHeight;
    for (ReadOnlyEvent event : schedule.getEvents()) {
      int currentStartDay = event.getStartDay().getValue() % 7;
      int currentEndDay = event.getEndDay().getValue() % 7;
      if (currentEndDay < currentStartDay) {
        currentEndDay += 7;
      }
      if (dayIdx >= currentStartDay && dayIdx <= currentEndDay) {
        int eventStartMins = (event.getStartTime() / 100) * 60 + (event.getStartTime() % 100);
        int eventEndMins = (event.getEndTime() / 100) * 60 + (event.getEndTime() % 100);
        if (dayIdx > currentStartDay) {
          eventStartMins = 0;
        }
        if (dayIdx < currentEndDay) {
          eventEndMins = 23 * 60 + 59;
        }
        //This uses the previous logic to assert the click was in the correct spot and opens the
        //event frame
        if (clickedMinutes >= eventStartMins && clickedMinutes <= eventEndMins) {
          scheduleFrame.openEventFrame(event);
          return;
        }
      }
    }
  }
}
