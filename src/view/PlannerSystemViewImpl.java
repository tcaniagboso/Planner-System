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

import controller.PlannerSystemController;
import plannersystem.ReadonlyPlannerSystem;
import schedule.ISchedule;
import schedule.Schedule;

/**
 * The PlannerSystemView class represents the main graphical user interface of the Planner System
 * application.
 * It provides functionalities for creating events, scheduling events, loading and saving calendars,
 * and displaying user schedules.
 */
public class PlannerSystemViewImpl extends JFrame implements PlannerSystemView, ActionListener {
  private static final int FRAME_SIZE = 700;
  private JComboBox<String> userOptions;
  private final SchedulePanel schedulePanel;
  private final JFileChooser fileChooser;

  private final ReadonlyPlannerSystem system;

  private PlannerSystemController controller;

  /**
   * Constructs a PlannerSystemView with the specified ReadonlyPlannerSystem.
   *
   * @param system The ReadonlyPlannerSystem instance to be used.
   * @throws IllegalArgumentException if the provided system is null.
   */
  public PlannerSystemViewImpl(ReadonlyPlannerSystem system) {
    if (system == null) {
      throw new IllegalArgumentException("System is null");
    }

    this.system = system;
    this.setTitle("Weekly Schedule");
    this.setSize(FRAME_SIZE, FRAME_SIZE + 50);
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    schedulePanel = new SchedulePanel();
    schedulePanel.setSchedule(new Schedule("none"));
    schedulePanel.setFirstDayOfWeek(system.getFirstDayOfWeek());
    // Custom panel for drawing the schedule
    this.add(schedulePanel, BorderLayout.CENTER);
    this.displayButtons();
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = this.createFileMenu();
    menuBar.add(fileMenu);
    this.setJMenuBar(menuBar);
    this.fileChooser = new JFileChooser();
  }

  @Override
  public void setActionListener(PlannerSystemController listener) {
    this.controller = listener;
    this.schedulePanel.setActionListener(listener);
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public File loadFile() {
    int response = fileChooser.showOpenDialog(null);
    if (response == JFileChooser.APPROVE_OPTION) {
      return new File(fileChooser.getSelectedFile().getAbsolutePath());
    }
    return null;
  }

  @Override
  public String saveFile() {
    int response = fileChooser.showSaveDialog(null);
    if (response == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getAbsolutePath();
    }
    return null;
  }

  @Override
  public void refresh() {
    this.schedulePanel.repaint();
  }

  @Override
  public String getCurrentUser() {
    return (String) userOptions.getSelectedItem();
  }

  @Override
  public void updateSchedulePanel(String currentUser) {
    if (currentUser != null && !currentUser.equals("<none>")) {
      // Retrieve the schedule for the selected user
      ISchedule userSchedule = system.getSchedule(currentUser);
      // Update the schedule panel with the user's schedule
      schedulePanel.setSchedule(userSchedule);
    } else {
      // Handle case where no user is selected
      schedulePanel.setSchedule(new Schedule("none"));
    }
  }

  @Override
  public void updateUsers() {
    String currentUser = this.getCurrentUser();
    // to avoid duplication
    userOptions.removeAllItems();

    userOptions.addItem("<none>");
    for (String user : this.system.getUsers()) {
      userOptions.addItem(user);
    }

    userOptions.setSelectedItem(currentUser);

    userOptions.repaint();
  }

  @Override
  public void toggleColor() {
    this.schedulePanel.setColorEvent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    this.controller.processButtonPress(command);
  }

  /**
   * Displays the buttons on the user interface.
   */
  private void displayButtons() {
    // Panel for buttons
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setPreferredSize(new Dimension(FRAME_SIZE, this.getHeight() / 16));

    JButton createEventButton = this.createButton("Create event");
    JButton scheduleEventButton = this.createButton("Schedule event");
    JButton toggleColorButton = this.createButton("Toggle color");
    userOptions = this.createComboBox(this.userOptions());

    createEventButton.setActionCommand("Open Event Frame");
    scheduleEventButton.setActionCommand("Open Schedule Event Frame");
    toggleColorButton.setActionCommand("Toggle Color");

    // Panel to hold the createEventButton and keep it at the center
    JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    centerButtonPanel.add(createEventButton);
    centerButtonPanel.add(scheduleEventButton);

    // Panel to hold the scheduleEventButton and keep it at the right
    JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightButtonPanel.add(toggleColorButton);

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
   * Creates a JButton with the specified text.
   *
   * @param text The text to be displayed on the button.
   * @return The created JButton.
   */
  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Aptos", Font.BOLD, this.getWidth() / 55));
    button.setBackground(Color.white);
    button.setPreferredSize(new Dimension(this.getWidth() / 5, this.getHeight() / 20));
    button.addActionListener(this);
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
    comboBox.setFont(new Font("Aptos", Font.BOLD, this.getWidth() / 55));
    comboBox.setBackground(Color.white);
    comboBox.setPreferredSize(new Dimension(this.getWidth() / 5,
            this.getHeight() / 20));
    comboBox.addActionListener(this);
    comboBox.setActionCommand("Select user");
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
    JMenuItem addCalendar = new JMenuItem("Add calendar");
    JMenuItem saveCalendar = new JMenuItem("Save calendars");
    addCalendar.addActionListener(this);
    saveCalendar.addActionListener(this);
    addCalendar.setActionCommand("Add calendar");
    saveCalendar.setActionCommand("Save calendars");
    menu.add(addCalendar);
    menu.add(saveCalendar);
    return menu;
  }
}
