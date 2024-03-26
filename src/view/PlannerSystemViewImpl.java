package view;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plannersystem.ReadonlyPlannerSystem;
import schedule.Event;
import schedule.Schedule;


public class PlannerSystemViewImpl extends JFrame implements PlannerSystemView, ActionListener {
  private static final int FRAME_SIZE = 1050;
  private JButton createEventButton;
  private JButton scheduleEventButton;
  private JComboBox<String> userOptions;
  private SchedulePanel schedulePanel;

  private final ReadonlyPlannerSystem system;

  public PlannerSystemViewImpl(ReadonlyPlannerSystem system) {
    if (system == null) {
      throw new IllegalArgumentException("System is null");
    }

    this.system = system;
    this.setTitle("Weekly Schedule");
    this.setSize(FRAME_SIZE, FRAME_SIZE + 50);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    schedulePanel = new SchedulePanel(new Schedule("none"));
    // Custom panel for drawing the schedule
    this.add(schedulePanel, BorderLayout.CENTER);
    this.displayButtons();
    JMenuBar menuBar= new JMenuBar();
    JMenu fileMenu = this.createFileMenu();
    menuBar.add(fileMenu);
    this.setJMenuBar(menuBar);

    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String currentUser = (String) userOptions.getSelectedItem();
    if (e.getSource() == createEventButton) {
      assert currentUser != null;
      if (currentUser.equals("<none>")) {
        this.displayErrorMessage("No user is selected");
      }
      else {
        EventView eventView = new EventViewImpl(new Event());
      }
    }
    if (e.getSource() == scheduleEventButton) {
      assert currentUser != null;
      if (currentUser.equals("<none>")) {
        this.displayErrorMessage("No user is selected");
      }
      else {
        EventView eventView = new EventViewImpl(new Event());
      }
    }

    updateSchedulePanel();
  }

  private void displayButtons() {
    // Panel for buttons
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setPreferredSize(new Dimension(FRAME_SIZE, 50));

    createEventButton = this.createButton("Create event");
    scheduleEventButton = this.createButton("Schedule Event");
    userOptions = this.createComboBox(this.userOptions());
    createEventButton.addActionListener(this);
    scheduleEventButton.addActionListener(this);
    userOptions.addActionListener(this);

    // Panel to hold the createEventButton and keep it at the center
    JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    centerButtonPanel.add(createEventButton);

    // Panel to hold the scheduleEventButton and keep it at the right
    JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightButtonPanel.add(scheduleEventButton);

    JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftButtonPanel.add(userOptions);

    // Add the button panels to the main button panel
    buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
    buttonPanel.add(rightButtonPanel, BorderLayout.EAST);
    buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

    // Add the main button panel to the frame
    this.add(buttonPanel, BorderLayout.SOUTH);
  }

  private void updateSchedulePanel() {
    String currentUser = (String) userOptions.getSelectedItem();
    if (currentUser != null && !currentUser.equals("<none>")) {
      // Retrieve the schedule for the selected user
      Schedule userSchedule = system.getSchedule(currentUser);
      // Update the schedule panel with the user's schedule
      schedulePanel.setSchedule(userSchedule);
    } else {
      // Handle case where no user is selected
      schedulePanel.setSchedule(new Schedule("none"));
    }

    schedulePanel.repaint();
  }

  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Aptos", Font.BOLD, 15));
    button.setBackground(Color.white);
    button.setPreferredSize(new Dimension(200, 40));

    return button;
  }

  private JComboBox<String> createComboBox(String[] options) {
    JComboBox<String> comboBox = new JComboBox<>(options);
    comboBox.setFont(new Font("Aptos", Font.BOLD, 15));
    comboBox.setBackground(Color.white);
    comboBox.setPreferredSize(new Dimension(200, 40));
    return comboBox;
  }

  private String[] userOptions() {
    String[] result = new String[system.getUsers().size() + 1];
    result[0] = "<none>";
    int i = 1;
    for (String user : system.getUsers()) {
      result[i++] = user;
    }

    return result;
  }

  private JMenu createFileMenu() {
    JMenu menu = new JMenu("File");
    JMenuItem addCalendar = new JMenuItem("Add calendar");
    JMenuItem saveCalendar = new JMenuItem("Save calendars");
    menu.add(addCalendar);
    menu.add(saveCalendar);
    return menu;
  }

  private void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

}
