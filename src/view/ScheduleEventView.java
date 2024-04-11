package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import schedule.Event;

/**
 * ScheduleEventView class extends EventViewImpl to provide a user interface
 * for scheduling events within the Planner System. This specialized view includes
 * functionality for specifying the duration of events and selecting invitees,
 * in addition to the standard event details.
 */
public class ScheduleEventView extends EventViewImpl {

  private static final int WIDTH = 600;
  private static final int HEIGHT = 650;
  private JButton scheduleEventButton;

  /**
   * Constructs a ScheduleEventView for a given event and user ID.
   * Initializes the view with specified dimensions and sets up UI components
   * for event scheduling.
   *
   * @param event  The event to be scheduled or viewed.
   * @param userId The user ID associated with this event scheduling session.
   */
  public ScheduleEventView(Event event, String userId) {
    super(event, userId);
    this.setSize(WIDTH, HEIGHT);
  }

  @Override
  public void setActionListener(ActionListener listener) {
    this.scheduleEventButton.addActionListener(listener);
  }

  @Override
  public String getStartDay() {
    return null;
  }

  @Override
  public String getStartTime() {
    return null;
  }

  @Override
  public String getEndDay() {
    return null;
  }

  @Override
  public String getEndTime() {
    return null;
  }

  @Override
  public int getDuration() {
    JTextField durationTextArea = (JTextField) getContentPane().getComponent(7);
    try {
      String entry = durationTextArea.getText();
      return Integer.parseInt(entry);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid Duration! Duration must be a number");
    }
  }

  @Override
  public List<String> getInvitees() {
    List<String> invitees = new ArrayList<>();
    JTextArea inviteesTextArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(9))
            .getViewport().getView();
    String inviteesText = inviteesTextArea.getText();
    String[] unfiltered = inviteesText.split("\n");
    for (String names : unfiltered) {
      if (!names.trim().isEmpty()) {
        invitees.add(names);
      }
    }
    return invitees;
  }

  @Override
  protected void displayLabels() {
    this.createLabel("Event name:", 0, 5, 200, 30);
    this.createTextField("", 5, 45, WIDTH - 50, 50);

    this.createLabel("Location:", 0, 110, 200, 30);
    this.createLabel("Online:", 50, 150, 100, 20);
    this.createComboBox(new String[]{"Yes", "No"}, 120, 150);
    this.createTextField("", 5, 190, WIDTH - 50, 50);

    this.createLabel("Duration (in minutes):", 0, 250, 200, 30);
    this.createTextField("", 5, 300, WIDTH - 50, 50);
    this.createLabel("Available Users", 0, 360, 200, 30);
    this.createScrollableTextArea(400);
  }

  @Override
  protected void displayButtons() {
    // Creating a new JPanel for the button
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setPreferredSize(new Dimension(WIDTH, 50)); // Adjust the height as needed
    buttonPanel.setBounds(5, 555, WIDTH - 30, 50);
    buttonPanel.setBackground(Color.lightGray);

    // Initialize the scheduleEventButton if not already initialized
    scheduleEventButton = new JButton("Schedule Event");
    scheduleEventButton.setFont(new Font("Aptos", Font.BOLD, 15));
    scheduleEventButton.setBackground(Color.white);

    // Make the button take up the entire panel
    scheduleEventButton.setPreferredSize(new Dimension(WIDTH - 30, 40));
    scheduleEventButton.setActionCommand("Schedule event");

    // Add the scheduleEventButton to the buttonPanel
    buttonPanel.add(scheduleEventButton, BorderLayout.CENTER);

    // Replace the existing buttonPanel in the JFrame with the new one
    this.add(buttonPanel, BorderLayout.SOUTH);

  }

  @Override
  protected void populateTextFields() {
    // ignored
  }
}
