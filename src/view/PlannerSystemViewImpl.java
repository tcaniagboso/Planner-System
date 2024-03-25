package view;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;


public class PlannerSystemViewImpl extends JFrame implements PlannerSystemView {
  private static final int FRAME_SIZE = 1050;
  public PlannerSystemViewImpl() {
    this.setTitle("Weekly Schedule");
    this.setSize(FRAME_SIZE, FRAME_SIZE + 50);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Custom panel for drawing the schedule
    add(new SchedulePanel(), BorderLayout.CENTER);

    // Panel for buttons
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setPreferredSize(new Dimension(FRAME_SIZE, 50));

    // Create buttons
    JButton createEventButton = new JButton("Create event");
    createEventButton.setFont(new Font("Aptos", Font.BOLD, 15));
    createEventButton.setBackground(Color.white);
    createEventButton.setPreferredSize(new Dimension(200, 40));

    JButton scheduleEventButton = new JButton("Schedule event");
    scheduleEventButton.setFont(new Font("Aptos", Font.BOLD, 15));
    scheduleEventButton.setBackground(Color.white);
    scheduleEventButton.setPreferredSize(new Dimension(200, 40));

    // Panel to hold the createEventButton and keep it at the center
    JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    centerButtonPanel.add(createEventButton);

    // Panel to hold the scheduleEventButton and keep it at the right
    JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightButtonPanel.add(scheduleEventButton);

    // Add the button panels to the main button panel
    buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
    buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

    // Add the main button panel to the frame
    add(buttonPanel, BorderLayout.SOUTH);

    setVisible(true);
  }

}
