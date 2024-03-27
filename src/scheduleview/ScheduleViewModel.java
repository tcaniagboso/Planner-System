package scheduleview;

import java.time.DayOfWeek;
import java.util.List;

import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;
import schedule.TimeUtilities;

/**
 * A class that implements the ScheduleView interface to provide functionality
 * for rendering a Schedule object into a human-readable string representation.
 * This class primarily interacts with the PlannerSystem to retrieve a user's
 * schedule and then formats it into a structured text format that outlines
 * each event within the schedule, including details such as event name, time,
 * location, whether it's an online event, and invitees.
 *
 * <p>Usage requires a valid instance of PlannerSystem to be passed during
 * construction. The main functionality is accessed through the render method,
 * which takes a user identifier and returns the formatted schedule as a string.</p>
 *
 * <p>This implementation ensures that schedules are displayed in a week view,
 * with days of the week as headers followed by the events occurring on those
 * days. Each event's details are indented for clarity, and events are listed
 * under the day of the week on which they start. Time is displayed in a
 * human-readable format, leveraging the TimeUtilities class for formatting.</p>
 *
 * @see PlannerSystem
 * @see Schedule
 * @see Event
 * @see TimeUtilities
 */
public class ScheduleViewModel implements ScheduleView {

  private final PlannerSystem system;

  /**
   * Constructs a ScheduleViewModel with a specific PlannerSystem.
   *
   * @param system the PlannerSystem instance that this ScheduleViewModel
   *               will use to retrieve schedule data; must not be null.
   * @throws IllegalArgumentException if the system argument is null.
   */
  public ScheduleViewModel(PlannerSystem system) {
    if (system == null) {
      throw new IllegalArgumentException("System is null");
    }
    this.system = system;
  }

  @Override
  public String render(String user) {
    if (user == null) {
      throw new IllegalArgumentException("user is null");
    }

    Schedule schedule = this.system.getSchedule(user);
    StringBuilder view = new StringBuilder();
    schedule.sortSchedule();

    for (int day = 0; day < 7; day++) {
      int index = (day == 0) ? day + 7 : day;
      DayOfWeek currentDay = DayOfWeek.of(index);
      view.append(TimeUtilities.formatDay(currentDay)).append(":");
      if (day < 6) {
        view.append(System.lineSeparator());
      }
      for (Event event : schedule.getEvents()) {
        if (event.getTime().getStartDay() == currentDay) {
          view.append("        name: ").append(event.getName()).append(System.lineSeparator());
          view.append("        time: ").append(TimeUtilities.formatDay(currentDay)).append(": ")
                  .append(event.getTime().getStartTime()).append(" -> ")
                  .append(TimeUtilities.formatDay(event.getTime().getEndDay())).append(": ")
                  .append(event.getTime().getEndTime()).append(System.lineSeparator());
          view.append("        location: ").append(event.getLocation().getLocation())
                  .append(System.lineSeparator());
          view.append("        online: ").append(event.getLocation().isOnline())
                  .append(System.lineSeparator());
          view.append("        invitees: ");
          List<String> invitees = event.getInvitees();
          for (int i = 0; i < event.getInvitees().size(); i++) {
            if (i > 0) {
              view.append("                  ");
            }
            view.append(invitees.get(i)).append(System.lineSeparator());
          }
        }
      }
    }
    return view.toString();
  }
}
