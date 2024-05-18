package provider.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import schedule.ISchedule;
import provider.model.ReadOnlyCentralSystem;
import schedule.ReadOnlyEvent;

/**
 * A particular implementation of IView. It represents the main view/frame of the scheduling system
 * and contains all the functionality expected from it, such as the grid with the days, buttons to
 * create events, schedule events and even a combo box to select ths user schedule desired.
 * It also contains a File menu option that allows a User to choose an xml file to load in, or to
 * save an XML file somewhere in their computer.
 */
public class ScheduleFrame extends JFrame implements IView {
  private JComboBox<String> selectSchedule;
  private JPanel gridPanel;
  private final ReadOnlyCentralSystem model;
  private final JMenuBar menuBar = new JMenuBar();
  private final JMenu fileMenu = new JMenu("File");
  private EventFrame eventFrame;
  private final List<IFeatures> featuresListeners = new ArrayList<>();

  /**
   * The constructor that sets up the frame to be displayed to the user.
   *
   * @param givenModel the model that is currently hooked up to the view (this)
   */
  public ScheduleFrame(ReadOnlyCentralSystem givenModel) {
    //Setting default stuff up
    super();
    this.setTitle("Schedule Manager");
    this.setPreferredSize(new Dimension(1400, 700));
    this.setSize(getPreferredSize());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    model = givenModel;

    //Setting up the menu bar stuff
    this.setJMenuBar(menuBar);
    menuBar.add(fileMenu);
    JMenuItem addCalendar = new JMenuItem("Add calendar");
    JMenuItem saveCalendars = new JMenuItem("Save calendars");
    addCalendar.addActionListener(this::loadXML);
    saveCalendars.addActionListener(this::saveXML);
    fileMenu.add(addCalendar);
    fileMenu.add(saveCalendars);

    //Bottom panel set up
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton createEvent = new JButton("Create Event");
    createEvent.addActionListener(this::createEventMain);

    JButton scheduleEvent = new JButton("Schedule Event");
    scheduleEvent.addActionListener(this::openScheduleEventFrame);
    List<String> schedules = model.allUsersInSystem();


    selectSchedule = new JComboBox<>(schedules.toArray(new String[0]));
    selectSchedule.addActionListener(this::scheduleSelect);
    bottomPanel.add(createEvent);
    bottomPanel.add(scheduleEvent);
    bottomPanel.add(selectSchedule);

    //Actually setting up the frame
    this.setLayout(new BorderLayout());
    gridPanel = new GridPanel(model.getUserSchedule((String) selectSchedule.getSelectedItem()),
            this);
    this.add(gridPanel, BorderLayout.CENTER);
    this.add(bottomPanel, BorderLayout.SOUTH);
    this.pack();
  }

  //handles the response to attempting to save an XML
  private void saveXML(ActionEvent e) {
    JFileChooser chooseFile = new JFileChooser();
    chooseFile.setDialogTitle("Save XML Calendar");
    if (chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      File file = chooseFile.getSelectedFile();
      String filePath = file.getAbsolutePath();
      String userName = selectSchedule.getSelectedItem().toString();
      for (IFeatures listener : featuresListeners) {
        listener.saveXML(userName, filePath);
      }
    }
  }


  //handles the response to attempting to load an XML
  // Modify the loadXML method to refresh the UI components after loading the XML
  private void loadXML(ActionEvent e) {
    JFileChooser chooseFile = new JFileChooser();
    chooseFile.setDialogTitle("Load in XML Calendar");
    if (chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File file = chooseFile.getSelectedFile();
      boolean loadSuccessful = false;
      String newUserName = null;
      try {
        for (IFeatures listener : featuresListeners) {
          newUserName = listener.loadXML(file.getAbsolutePath());
          loadSuccessful = true;
        }
        if (loadSuccessful && newUserName != null) {
          updateScheduleComboBox(newUserName);
          JOptionPane.showMessageDialog(this,
                  "XML file successfully loaded.",
                  "Load Success", JOptionPane.INFORMATION_MESSAGE);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Load Error: "
                +
                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Updates the combobox to have the new user appear.
   * @param newUserName the new user name that should appear in the combo box
   */
  @Override
  public void updateScheduleComboBox(String newUserName) {
    List<String> schedules = model.allUsersInSystem();
    selectSchedule.setModel(new DefaultComboBoxModel<>(schedules.toArray(new String[0])));
    selectSchedule.setSelectedItem(newUserName); // Select the newly added user
    selectSchedule.revalidate();
    selectSchedule.repaint();
    scheduleSelect(new ActionEvent(selectSchedule, ActionEvent.ACTION_PERFORMED, null));
  }

  @Override
  public void close() {
    this.setVisible(false);
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void addFeatureListener(IFeatures features) {
    this.featuresListeners.add(Objects.requireNonNull(features));
  }

  @Override
  public void updateGridPanel(ISchedule schedule) {
    this.remove(gridPanel);
    gridPanel = new GridPanel(schedule, this);
    this.add(gridPanel, BorderLayout.CENTER);
    this.revalidate();
    refresh();
  }

  //handles the clicking of the create event button in the schedule frame
  private void createEventMain(ActionEvent e) {
    String selectedHost = (String) selectSchedule.getSelectedItem();
    for (IFeatures listener : featuresListeners) {
      listener.createEventMain(selectedHost);
    }
  }

  //handles the selection of a schedule/user on the combo box in this frame
  private void scheduleSelect(ActionEvent e) {
    this.remove(gridPanel);
    String selectedUser = (String) selectSchedule.getSelectedItem();
    for (IFeatures listener : featuresListeners) {
      listener.scheduleSelect(selectedUser);
    }
  }


  void openEventFrame(ReadOnlyEvent event) {
    String selectedHost = (String) selectSchedule.getSelectedItem();
    for (IFeatures listener : featuresListeners) {
      listener.openEventFrame(event, selectedHost);
    }
  }

  private void openScheduleEventFrame(ActionEvent e) {
    String selectedHost = (String) selectSchedule.getSelectedItem();
    for (IFeatures listener : featuresListeners) {
      listener.scheduleEventMain(selectedHost);
    }
  }

}

