package provider.view;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import provider.model.ReadOnlyCentralSystem;
import provider.dto.EventData;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

/**
 * A particular implementation of a frame meant for scheduling an event.
 */
public class ScheduleEventFrame extends JFrame implements IScheduleEventFrame {
  private JTextField eventNameField;
  private JTextField locationField;
  private JCheckBox isOnlineCheckBox;
  private JTextField durationField;
  private JList<String> userList;
  private String hostUsername;
  private IFeatures features;

  /**
   * Initializes the scheduling frame.
   * @param model a ReadOnlyCentralSystem used to retrieve the users inside the system and make them
   *              available as invitees
   * @param hostUsername the host scheduling the event
   */
  public ScheduleEventFrame(ReadOnlyCentralSystem model, String hostUsername) {
    this.hostUsername = hostUsername;
    initializeUI(model);
  }

  public void setFeatures(IFeatures features) {
    this.features = features;
  }

  private void initializeUI(ReadOnlyCentralSystem model) {
    setTitle("Schedule Event");
    setPreferredSize(new Dimension(300, 400));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    setupComponents(mainPanel, model);
    setupScheduleButton();

    add(mainPanel, BorderLayout.CENTER);
    pack();
  }

  private void setupComponents(JPanel mainPanel, ReadOnlyCentralSystem model) {
    eventNameField = new JTextField();
    eventNameField.setBorder(BorderFactory.createTitledBorder("Event name"));
    mainPanel.add(eventNameField);

    locationField = new JTextField();
    locationField.setBorder(BorderFactory.createTitledBorder("Location"));
    mainPanel.add(locationField);

    isOnlineCheckBox = new JCheckBox("Is online");
    mainPanel.add(isOnlineCheckBox);

    durationField = new JTextField();
    durationField.setBorder(BorderFactory.createTitledBorder("Duration (minutes)"));
    mainPanel.add(durationField);

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

  private void setupScheduleButton() {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton scheduleButton = new JButton("Schedule Event");
    scheduleButton.addActionListener(e -> scheduleEvent());
    buttonPanel.add(scheduleButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void scheduleEvent() {
    String eventName = eventNameField.getText();
    String location = locationField.getText();
    boolean isOnline = isOnlineCheckBox.isSelected();
    int duration;
    try {
      duration = Integer.parseInt(durationField.getText());
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
              "Please enter a valid duration in minutes.", "Invalid Duration",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    List<String> selectedInvitees = userList.getSelectedValuesList();
    if (selectedInvitees.isEmpty()) {
      JOptionPane.showMessageDialog(this,
              "Please select at least one invitee.",
              "No Invitees Selected", JOptionPane.ERROR_MESSAGE);
      return;
    }
    EventData eventData = new EventData(
            eventName, location, isOnline, null, null, -1, duration,
            hostUsername, selectedInvitees
    );
    features.scheduleEvent(eventData);
    close();
  }

  @Override
  public void close() {
    dispose();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void refresh() {
    repaint();
  }
}
