package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import schedule.Event;

public class EventViewImpl extends JFrame implements EventView {

  private final static int WIDTH = 600;
  private final static int HEIGHT = 800;

  private final static String[] labels = {"Event name:", "Location:", "Starting Day:",
          "Starting time:", "Ending Day:", "Ending time:", "Available users:"};

  private final Event event;

  public EventViewImpl(Event event) {
    if (event == null) {
      throw new IllegalArgumentException("Event is null.");
    }
    this.event = event;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setSize(WIDTH, HEIGHT);
    this.setTitle("Event");
    this.setLayout(new GridBagLayout());
    this.getContentPane().setBackground(Color.lightGray);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;


    for (String label : labels) {
      gbc.gridwidth = 2;
      this.add(createLabel(label), gbc);
      gbc.gridy++;
      if ("Location:".equals(label)) {
        this.add(createCheckBox("Is online"), gbc); // For the online checkbox
        gbc.gridy++;
      }
      if ("Starting Day:".equals(label) || "Ending Day:".equals(label)) {
        this.add(createComboBox(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"}), gbc);
        gbc.gridy++;
      } else if (!"Available users:".equals(label)) {
        this.add(createTextField(WIDTH - 50), gbc);
        gbc.gridy++;
      }
    }

    JTextArea usersTextArea = new JTextArea();
    usersTextArea.setFont(new Font("Calibri", Font.PLAIN, 20)); // Set the font to match other UI elements
    usersTextArea.setLineWrap(true);
    usersTextArea.setWrapStyleWord(true);
    JScrollPane userScroll = new JScrollPane(usersTextArea);
    userScroll.setPreferredSize(new Dimension(WIDTH - 50, 100)); // Set the preferred size for the scroll pane
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    gbc.gridy++; // Make sure the gridy value is set to place the component in the right position
    this.add(userScroll, gbc); // Add the scroll pane with the text area to the frame


    // Add buttons at the bottom
    gbc.gridwidth = 2; // Set this to 2 to span two columns if needed
    gbc.gridx = 0; // Reset to the first column
    gbc.gridy++; // Increment the row to move below the user list area

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(createButton("Modify event"));
    buttonPanel.add(createButton("Remove event"));
    buttonPanel.setBackground(Color.lightGray);

    // Adjust constraints for the button panel
    gbc.fill = GridBagConstraints.NONE;  // Reset fill if previously set to HORIZONTAL
    gbc.anchor = GridBagConstraints.CENTER; // Center the panel
    this.add(buttonPanel, gbc);

    this.setVisible(true);
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setForeground(Color.black);
    label.setFont(new Font("Calibri", Font.PLAIN, 20));
    return label;
  }

  private JTextField createTextField(int width) {
    JTextField textField = new JTextField();
    textField.setPreferredSize(new Dimension(width, 30));
    textField.setFont(new Font("Calibri", Font.PLAIN, 20));
    textField.setBackground(Color.white);
    return textField;
  }

  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Calibri", Font.BOLD, 15));
    button.setBackground(Color.white);
    return button;
  }

  private JCheckBox createCheckBox(String text) {
    JCheckBox checkBox = new JCheckBox(text);
    checkBox.setFont(new Font("Calibri", Font.PLAIN, 20));
    checkBox.setBackground(Color.lightGray);
    return checkBox;
  }

  private JComboBox<String> createComboBox(String[] options) {
    JComboBox<String> comboBox = new JComboBox<>(options);
    comboBox.setFont(new Font("Calibri", Font.PLAIN, 20));
    return comboBox;
  }
}
