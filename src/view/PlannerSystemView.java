package view;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import plannersystem.ReadonlyPlannerSystem;
import schedule.Event;
import schedule.Schedule;

/**
 * The PlannerSystemView class represents the main graphical user interface of the Planner System
 * application.
 * It provides functionalities for creating events, scheduling events, loading and saving calendars,
 * and displaying user schedules.
 */
public class PlannerSystemView extends JFrame implements ActionListener {
  private static final int FRAME_SIZE = 1200;
  private JButton createEventButton;
  private JButton scheduleEventButton;
  private JComboBox<String> userOptions;
  private final SchedulePanel schedulePanel;
  private JMenuItem addCalendar;
  private JMenuItem saveCalendar;
  private final JFileChooser fileChooser;

  private final ReadonlyPlannerSystem system;

  /**
   * Constructs a PlannerSystemView with the specified ReadonlyPlannerSystem.
   *
   * @param system The ReadonlyPlannerSystem instance to be used.
   * @throws IllegalArgumentException if the provided system is null.
   */
  public PlannerSystemView(ReadonlyPlannerSystem system) {
    if (system == null) {
      throw new IllegalArgumentException("System is null");
    }

    this.system = system;
    this.setTitle("Weekly Schedule");
    this.setSize(FRAME_SIZE, FRAME_SIZE);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    schedulePanel = new SchedulePanel(new Schedule("none"));
    // Custom panel for drawing the schedule
    this.add(schedulePanel, BorderLayout.CENTER);
    this.displayButtons();
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = this.createFileMenu();
    menuBar.add(fileMenu);
    this.setJMenuBar(menuBar);
    this.fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("."));
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String currentUser = (String) userOptions.getSelectedItem();
    if (e.getSource() == scheduleEventButton || e.getSource() == createEventButton) {
      assert currentUser != null;
      if (currentUser.equals("<none>")) {
        this.displayErrorMessage("No user is selected");
      } else {
        new EventView(new Event(), currentUser);
      }
    }
    if (e.getSource() == addCalendar) {
      this.loadFile();
    }
    if (e.getSource() == saveCalendar) {
      this.saveFile();
    }

    updateSchedulePanel();
  }

  /**
   * Loads a calendar file.
   */
  private void loadFile() {
    int response = fileChooser.showOpenDialog(null);
    if (response == JFileChooser.APPROVE_OPTION) {
      File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
      System.out.println(file);
    }
  }

  /**
   * Saves the current calendar to a file.
   */
  private void saveFile() {
    int response = fileChooser.showSaveDialog(null);
    if (response == JFileChooser.APPROVE_OPTION) {
      File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
      System.out.println(file);
    }
  }

  /**
   * Displays the buttons on the user interface.
   */
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

  /**
   * Updates the schedule panel based on the selected user.
   */
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

  /**
   * Creates a JButton with the specified text.
   *
   * @param text The text to be displayed on the button.
   * @return The created JButton.
   */
  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Aptos", Font.BOLD, 15));
    button.setBackground(Color.white);
    button.setPreferredSize(new Dimension(200, 40));

    return button;
  }

  /**
   * Creates a JComboBox with the specified options.
   *
   * @param options The options to be displayed in the combo box.
   * @return The created JComboBox.
   */
  private JComboBox<String> createComboBox(String[] options) {
    JComboBox<String> comboBox = new JComboBox<>(options);
    comboBox.setFont(new Font("Aptos", Font.BOLD, 15));
    comboBox.setBackground(Color.white);
    comboBox.setPreferredSize(new Dimension(200, 40));
    return comboBox;
  }

  /**
   * Generates the user options for the combo box.
   *
   * @return An array of user options.
   */
  private String[] userOptions() {
    String[] result = new String[system.getUsers().size() + 1];
    result[0] = "<none>";
    int i = 1;
    for (String user : system.getUsers()) {
      result[i++] = user;
    }

    return result;
  }

  /**
   * Creates the File menu for the menu bar.
   *
   * @return The created File menu.
   */
  private JMenu createFileMenu() {
    JMenu menu = new JMenu("File");
    addCalendar = new JMenuItem("Add calendar");
    saveCalendar = new JMenuItem("Save calendars");
    addCalendar.addActionListener(this);
    saveCalendar.addActionListener(this);
    menu.add(addCalendar);
    menu.add(saveCalendar);
    return menu;
  }

  /**
   * Shows a JOptionPane error message to the user.
   *
   * @param message the message to be displayed.
   */
  private void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

}
