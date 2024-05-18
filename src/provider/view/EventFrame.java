package provider.view;

import provider.dto.EventData;
import provider.model.ReadOnlyCentralSystem;
import schedule.ReadOnlyEvent;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;


/**
 * A frame for creating, modifying, or removing events within the application.
 * This frame allows the user to input event details such as name, location,
 * whether the event is online or not, starting and ending days, as well as
 * starting and ending times. It also provides the functionality to manage
 * participants of the event.
 */
public class EventFrame extends JFrame implements IEventFrame {
  private JTextField eventNameField;
  private JTextField locationField;
  private JCheckBox isOnlineCheckBox;
  private JComboBox<String> startingDayComboBox;
  private JComboBox<String> endingDayComboBox;
  private JTextField startingTimeField;
  private JTextField endingTimeField;
  private JList<String> userList;
  private String hostUsername;
  private IFeatures features;
  private EventData originalEventDetails;

  /**
   * Constructs an EventFrame with a specified model, host username, and event details.
   * Initializes the UI components and sets up the event data if provided.
   *
   * @param model The read-only central system model providing the data context.
   * @param hostUsername The username of the host managing the event.
   * @param event The event to be edited; if null, the frame is set up for creating a new event.
   */
  public EventFrame(ReadOnlyCentralSystem model, String hostUsername, ReadOnlyEvent event) {
    this.hostUsername = hostUsername;
    initializeUI(model, event);
    if (event != null) {
      setEventDetails(event);
    }
  }

  public void setFeatures(IFeatures features) {
    this.features = features;
  }

  private void initializeUI(ReadOnlyCentralSystem model, ReadOnlyEvent event) {
    setTitle("Event Editor");
    setSize(300, 700);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    setupComponents(mainPanel, model);
    setupButtons();

    add(mainPanel, BorderLayout.CENTER);
    pack();
  }

  private void setupComponents(JPanel mainPanel, ReadOnlyCentralSystem model) {
    eventNameField = new JTextField();
    eventNameField.setBorder(BorderFactory.createTitledBorder("Event name:"));
    mainPanel.add(eventNameField);

    locationField = new JTextField();
    locationField.setBorder(BorderFactory.createTitledBorder("Location:"));
    mainPanel.add(locationField);

    JPanel onlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    isOnlineCheckBox = new JCheckBox("Is online");
    onlinePanel.add(isOnlineCheckBox);
    mainPanel.add(onlinePanel);

    String[] daysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday", "Saturday" };
    startingDayComboBox = new JComboBox<>(daysOfWeek);
    startingDayComboBox.setBorder(BorderFactory.createTitledBorder("Starting Day:"));
    mainPanel.add(startingDayComboBox);

    endingDayComboBox = new JComboBox<>(daysOfWeek);
    endingDayComboBox.setBorder(BorderFactory.createTitledBorder("Ending Day:"));
    mainPanel.add(endingDayComboBox);

    startingTimeField = new JTextField();
    startingTimeField.setBorder(BorderFactory.createTitledBorder("Starting time (HH:MM):"));
    mainPanel.add(startingTimeField);

    endingTimeField = new JTextField();
    endingTimeField.setBorder(BorderFactory.createTitledBorder("Ending time (HH:MM):"));
    mainPanel.add(endingTimeField);

    DefaultListModel<String> userListModel = new DefaultListModel<>();
    model.allUsersInSystem().stream()
            .filter(user -> !user.equals(hostUsername))
            .forEach(userListModel::addElement);
    userList = new JList<>(userListModel);
    userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    userList.setBorder(BorderFactory.createTitledBorder("Invitees (select one or more)"));
    JScrollPane listScroller = new JScrollPane(userList);
    mainPanel.add(listScroller);
  }

  private void showSuccessMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
    close();
  }

  private void setupButtons() {
    JPanel buttonPanel = new JPanel();
    JButton createButton = new JButton("Create event");
    JButton modifyButton = new JButton("Modify event");
    JButton removeButton = new JButton("Remove event");

    createButton.addActionListener(e -> {
      if (validateInputs()) {
        features.createEvent(collectEventData());
        showSuccessMessage("Event created successfully");
      } else {
        JOptionPane.showMessageDialog(this,
                "Ensure all fields are filled correctly, and time is in HH:MM format.",
                "Cannot create Event", JOptionPane.ERROR_MESSAGE);
      }
    });

    modifyButton.addActionListener(e -> {
      if (validateInputs()) {
        features.modifyEvent(originalEventDetails, collectEventData());
        showSuccessMessage("Event modified successfully");
      } else {
        JOptionPane.showMessageDialog(
                this, "Ensure all fields are filled correctly,"
                        + " and time is in HH:MM format.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    removeButton.addActionListener(e -> {
      if (originalEventDetails != null) {
        features.removeEvent(originalEventDetails);
        showSuccessMessage("Event removed successfully");
        originalEventDetails = null;
      } else {
        JOptionPane.showMessageDialog(this, "No event selected for removal",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    buttonPanel.add(createButton);
    buttonPanel.add(modifyButton);
    buttonPanel.add(removeButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void setEventDetails(ReadOnlyEvent event) {
    eventNameField.setText(event.getName());
    locationField.setText(event.getLocation());
    isOnlineCheckBox.setSelected(event.isOnline());

    String startDay = capitalizeFirstLetter(event.getStartDay().toString().toLowerCase());
    startingDayComboBox.setSelectedItem(startDay);

    String endDay = capitalizeFirstLetter(event.getEndDay().toString().toLowerCase());
    endingDayComboBox.setSelectedItem(endDay);

    startingTimeField.setText(formatTime(event.getStartTime()));
    endingTimeField.setText(formatTime(event.getEndTime()));

    List<String> inviteesList = new ArrayList<>(event.getInvitees());
    this.originalEventDetails = new EventData(
            event.getName(),
            event.getLocation(),
            event.isOnline(),
            event.getStartDay(),
            event.getEndDay(),
            event.getStartTime(),
            event.getEndTime(),
            event.getHost(),
            inviteesList
    );
  }

  private String capitalizeFirstLetter(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  private String formatEventDetailsForModification(ReadOnlyEvent event) {
    String invitees = String.join(", ", event.getInvitees());

    return String.format("Event Name: %s, Location: %s, Is Online: %b, Start Day: %s, "
                    + "End Day: %s, Start Time: %04d, End Time: %04d, Host: %s, Invitees: %s",
            event.getName(), event.getLocation(), event.isOnline(),
            event.getStartDay().toString(), event.getEndDay().toString(),
            event.getStartTime(), event.getEndTime(), hostUsername, invitees);
  }


  private EventData collectEventData() {
    String eventName = eventNameField.getText();
    String location = locationField.getText();
    boolean isOnline = isOnlineCheckBox.isSelected();
    DayOfWeek startDay = getDayOfWeekFromString((String) startingDayComboBox.getSelectedItem());
    DayOfWeek endDay = getDayOfWeekFromString((String) endingDayComboBox.getSelectedItem());
    String[] startTimeParts = startingTimeField.getText().split(":");
    int startTime = Integer.parseInt(startTimeParts[0]) * 100 + Integer.parseInt(startTimeParts[1]);

    String[] endTimeParts = endingTimeField.getText().split(":");
    int endTime = Integer.parseInt(endTimeParts[0]) * 100 + Integer.parseInt(endTimeParts[1]);

    List<String> invitees = new ArrayList<>(userList.getSelectedValuesList());

    return new EventData(eventName, location, isOnline, startDay, endDay, startTime, endTime,
            hostUsername, invitees);
  }

  // ensures that all inputs are valid meaning nothing is missing and
  // the time is in the correct format instead of being able to just put text
  private boolean validateInputs() {
    if (eventNameField.getText().isEmpty() || locationField.getText().isEmpty()) {
      System.out.println("Error: Missing event name or location.");
      return false;
    }
    if (!startingTimeField.getText().matches("\\d{2}:\\d{2}") || !endingTimeField.getText()
            .matches("\\d{2}:\\d{2}")) {
      System.out.println("Error: Time must be in HH:MM format.");
      return false;
    }
    return true;
  }

  @Override
  public void close() {
    dispose();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void refresh() {
    repaint();
  }

  //This meethod helps us format time from HHMM integer to HH:MM string
  private String formatTime(int time) {
    int hours = time / 100;
    int minutes = time % 100;
    return String.format("%02d:%02d", hours, minutes);
  }

  //Another helper method for getting the day of week
  private DayOfWeek getDayOfWeekFromString(String dayString) {
    switch (dayString.toLowerCase()) {
      case "sunday": return DayOfWeek.SUNDAY;
      case "monday": return DayOfWeek.MONDAY;
      case "tuesday": return DayOfWeek.TUESDAY;
      case "wednesday": return DayOfWeek.WEDNESDAY;
      case "thursday": return DayOfWeek.THURSDAY;
      case "friday": return DayOfWeek.FRIDAY;
      case "saturday": return DayOfWeek.SATURDAY;

      default: throw new IllegalArgumentException("Invalid day string");
    }
  }



}
