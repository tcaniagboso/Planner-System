package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Color;


import javax.swing.JPanel;

public class SchedulePanel extends JPanel {
  private static final int NUM_HOURS = 24;
  private static final int HOUR_INTERVAL = 4;
  private static final int NUM_DAYS = 7;
  private static final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
          "Thursday", "Friday", "Saturday"};

  public SchedulePanel() {
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


    // Draw the times on the leftmost column
    g.setFont(new Font("Aptos", Font.BOLD, 15));
    for (int i = 1; i <= NUM_HOURS; i++) {
      String time = String.format("%02d:00", i - 1);
      g.drawString(time, 5, cellHeight * i + g.getFontMetrics().getAscent() / 2 - 2);
    }

    // Draw the days of the week
    g.setFont(new Font("Aptos", Font.BOLD, 20));
    for (int i = 0; i < NUM_DAYS; i++) {
      g.drawString(daysOfWeek[i], cellWidth * (i + 1)
              + (columnWidth - g.getFontMetrics().stringWidth(daysOfWeek[i])) / 2,
              cellHeight - 10);
    }

    // Draw horizontal lines
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
}
