package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;


import schedule.Event;
import schedule.TimeUtilities;

/**
 * EventView class represents a graphical user interface for viewing and interacting with events.
 * It extends the JFrame class and implements the ActionListener interface.
 */
public class EventViewImpl extends JFrame implements EventView {

  private static final int WIDTH = 600;
  private static final int HEIGHT = 850;

  private static final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
          "Thursday", "Friday", "Saturday"};

  private JButton createEventButton;
  private JButton modifyEventButton;
  private JButton removeEventButton;

  private final Event event;
  private final String user;

  /**
   * Constructs an EventView object with the specified event and user ID.
   *
   * @param event  The event to be displayed and interacted with.
   * @param userId The ID of the user associated with the event.
   * @throws IllegalArgumentException if the event or userId is null.
   */
  public EventViewImpl(Event event, String userId) {
    if (event == null || userId == null) {
      throw new IllegalArgumentException("Event is null.");
    }
    this.event = event;
    this.user = userId;
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setResizable(false);
    this.setSize(WIDTH, HEIGHT);
    this.setTitle("Event");
    this.setLayout(null);
    this.getContentPane().setBackground(Color.lightGray);
    this.displayLabels();
    this.displayButtons();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public String getUserId() {
    return this.user;
  }

  @Override
  public void setActionListener(ActionListener listener) {
    this.createEventButton.addActionListener(listener);
    this.removeEventButton.addActionListener(listener);
    this.modifyEventButton.addActionListener(listener);
  }

  @Override
  public String getEventName() {
    JTextField eventNameTextField = (JTextField) getContentPane().getComponent(1);
    return eventNameTextField.getText();
  }

  @Override
  public String getStartDay() {
    JComboBox<String> startingDayComboBox = (JComboBox<String>) getContentPane().getComponent(7);
    return (String) startingDayComboBox.getSelectedItem();
  }

  @Override
  public String getStartTime() {
    JTextField startingTimeTextField = (JTextField) getContentPane().getComponent(9);
    return TimeUtilities.formatTime(LocalTime.parse(startingTimeTextField.getText()));
  }

  @Override
  public String getEndDay() {
    JComboBox<String> endingDayComboBox = (JComboBox<String>) getContentPane().getComponent(11);
    return (String) endingDayComboBox.getSelectedItem();
  }

  @Override
  public String getEndTime() {
    JTextField endingTimeTextField = (JTextField) getContentPane().getComponent(13);
    return TimeUtilities.formatTime(LocalTime.parse(endingTimeTextField.getText()));
  }

  @Override
  public String getEventLocation() {
    JTextField locationTextField = (JTextField) getContentPane().getComponent(5);
    return locationTextField.getText();
  }

  @Override
  public boolean getOnline() {
    JComboBox<String> onlineComboBox = (JComboBox<String>) getContentPane().getComponent(4);
    String choice = (String) onlineComboBox.getSelectedItem();
    return choice.equals("Yes");
  }

  @Override
  public List<String> getInvitees() {
    List<String> invitees = new ArrayList<>();
    JTextArea inviteesTextArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(15))
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
  public void refresh() {
    this.repaint();
  }

  @Override
  public void displayError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void checkFieldsNotEmpty() {
    Component[] components = getContentPane().getComponents();
    for (Component component : components) {
      if (component instanceof JTextField) {
        JTextField textField = (JTextField) component;
        String text = textField.getText().trim();
        if (text.isEmpty()) {
          throw new IllegalStateException("All event fields must be filled.");
        }
      } else if (component instanceof JTextArea) {
        JTextArea textArea = (JTextArea) ((JScrollPane) component).getViewport().getView();
        String text = textArea.getText().trim();
        if (text.isEmpty()) {
          throw new IllegalStateException("All event fields must be filled.");
        }
      }
    }
  }

  /**
   * Displays the labels for various event details.
   */
  private void displayLabels() {
    this.createLabel("Event name:", 0, 5, 200, 30);
    this.createTextField("", 5, 45, WIDTH - 50, 50);

    this.createLabel("Location:", 0, 120, 200, 30);
    this.createLabel("Online:", 50, 150, 100, 20);
    this.createComboBox(new String[]{"Yes", "No"}, 120, 150);
    this.createTextField("", 5, 190, WIDTH - 50, 50);

    this.createLabel("Starting Day:", 0, 250, 200, 30);
    this.createComboBox(daysOfWeek, 125, 250);

    this.createLabel("Starting Time:", 0, 325, 200, 30);
    this.createTextField("", 125, 325, 425, 50);

    this.createLabel("Ending Day:", 0, 400, 200, 30);
    this.createComboBox(daysOfWeek, 125, 400);

    this.createLabel("Ending Time:", 0, 475, 200, 30);
    this.createTextField("", 125, 475, 425, 50);

    this.createLabel("Available Users", 0, 550, 200, 30);
    this.createScrollableTextArea();
    this.populateTextFields();
  }

  /**
   * Displays the buttons for creating, modifying, and removing events.
   */
  private void displayButtons() {
    // Panel for buttons
    JPanel buttonPanel = new JPanel(null); // Use null layout
    buttonPanel.setPreferredSize(new Dimension(WIDTH, 50));
    buttonPanel.setBounds(0, 750, WIDTH, 50); // Adjusted y-coordinate
    buttonPanel.setBackground(Color.lightGray);

    createEventButton = this.createButton("Create event");
    modifyEventButton = this.createButton("Modify event");
    removeEventButton = this.createButton("Remove event");

    // Set bounds for buttons
    createEventButton.setBounds(5, 5, 150, 40);
    modifyEventButton.setBounds(WIDTH / 2 - 75, 5, 150, 40);
    removeEventButton.setBounds(WIDTH - 170, 5, 150, 40);

    // Add buttons to buttonPanel
    buttonPanel.add(createEventButton);
    buttonPanel.add(modifyEventButton);
    buttonPanel.add(removeEventButton);

    // Add buttonPanel to the frame
    this.add(buttonPanel);
  }

  /**
   * Creates a JLabel with the specified text and adds it to the frame.
   *
   * @param text   The text to be displayed on the label.
   * @param x      The x-coordinate of the label.
   * @param y      The y-coordinate of the label.
   * @param width  The width of the label.
   * @param height The height of the label.
   */
  private void createLabel(String text, int x, int y, int width, int height) {
    JLabel label = new JLabel(text);
    label.setForeground(Color.black);
    label.setFont(new Font("Aptos", Font.PLAIN, 20));
    label.setBounds(x, y, width, height);
    this.add(label);
  }

  /**
   * Creates a JTextField with the specified text and adds it to the frame.
   *
   * @param text   The initial text displayed in the text field.
   * @param x      The x-coordinate of the text field.
   * @param y      The y-coordinate of the text field.
   * @param width  The width of the text field.
   * @param height The height of the text field.
   */
  private void createTextField(String text, int x, int y, int width, int height) {
    JTextField textField = new JTextField(text);
    textField.setPreferredSize(new Dimension(width, 30));
    textField.setFont(new Font("Aptos", Font.PLAIN, 20));
    textField.setBackground(Color.white);
    textField.setForeground(Color.black);
    textField.setBounds(x, y, width, height);
    this.add(textField);
  }

  /**
   * Creates a JButton with the specified text and adds it to the frame.
   *
   * @param text The text displayed on the button.
   * @return The created JButton.
   */
  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Aptos", Font.BOLD, 15));
    button.setBackground(Color.white);
    button.setPreferredSize(new Dimension(150, 40));
    button.setActionCommand(text);
    return button;
  }

  /**
   * Creates a JComboBox with the specified options and adds it to the frame.
   *
   * @param options The array of options displayed in the combo box.
   * @param x       The x-coordinate of the combo box.
   * @param y       The y-coordinate of the combo box.
   */
  private void createComboBox(String[] options, int x, int y) {
    JComboBox<String> comboBox = new JComboBox<>(options);
    comboBox.setFont(new Font("Aptos", Font.PLAIN, 20));
    comboBox.setForeground(Color.black);
    comboBox.setBackground(Color.white);
    comboBox.setBounds(x, y, 200, 30);
    this.add(comboBox);
  }

  /**
   * Creates a scrollable JTextArea and adds it to the frame within a JScrollPane.
   */
  private void createScrollableTextArea() {
    JTextArea textArea = new JTextArea("");
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setBackground(Color.white);
    textArea.setForeground(Color.black);
    textArea.setFont(new Font("Aptos", Font.PLAIN, 20));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(550, 150));
    scrollPane.setBounds(5, 590, 550, 150); // Adjust bounds if necessary

    getContentPane().add(scrollPane); // Ad
  }

  /**
   * Populates the text fields, combo boxes, and text areas with event data.
   */
  private void populateTextFields() {
    setEventName();
    setOnlineComboBox();
    setLocationTextField();
    setStartingDayComboBox();
    setStartingTimeTextField();
    setEndingDayComboBox();
    setEndingTimeTextField();
    setAvailableUsersTextArea();
  }

  /**
   * Sets the text of the event name text field.
   */
  private void setEventName() {
    JTextField eventNameTextField = (JTextField) getContentPane().getComponent(1);
    try {
      eventNameTextField.setText(event.getName());
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the selected item of the online combo box based on event data.
   */
  private void setOnlineComboBox() {
    JComboBox<String> onlineComboBox = (JComboBox<String>) getContentPane().getComponent(4);
    try {
      onlineComboBox.setSelectedItem(event.getLocation().isOnline() ? "Yes" : "No");
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the text of the location text field.
   */
  private void setLocationTextField() {
    JTextField locationTextField = (JTextField) getContentPane().getComponent(5);
    try {
      locationTextField.setText(event.getLocation().getLocation());
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the selected item of the starting day combo box based on event data.
   */
  private void setStartingDayComboBox() {
    JComboBox<String> startingDayComboBox = (JComboBox<String>) getContentPane().getComponent(7);
    try {
      startingDayComboBox.setSelectedItem(daysOfWeek[event.getTime().getStartDay().getValue() % 7]);
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the text of the starting time text field.
   */
  private void setStartingTimeTextField() {
    JTextField startingTimeTextField = (JTextField) getContentPane().getComponent(9);
    try {
      startingTimeTextField.setText(event.getTime().getStartTime().toString());
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the selected item of the ending day combo box based on event data.
   */
  private void setEndingDayComboBox() {
    JComboBox<String> endingDayComboBox = (JComboBox<String>) getContentPane().getComponent(11);
    try {
      endingDayComboBox.setSelectedItem(daysOfWeek[event.getTime().getEndDay().getValue() % 7]);
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the text of the ending time text field.
   */
  private void setEndingTimeTextField() {
    JTextField endingTimeTextField = (JTextField) getContentPane().getComponent(13);
    try {
      endingTimeTextField.setText(event.getTime().getEndTime().toString());
    } catch (IllegalStateException ignored) {
    }
  }

  /**
   * Sets the text of the available users text area.
   */
  private void setAvailableUsersTextArea() {
    JTextArea textArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(15))
            .getViewport().getView();
    textArea.setText(String.join("\n", event.getInvitees()));
  }
}
