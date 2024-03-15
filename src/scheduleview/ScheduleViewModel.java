package scheduleview;

import java.time.DayOfWeek;
import java.util.List;

import schedule.Event;
import schedule.Schedule;
import schedule.TimeUtilities;

/**
 * Implementation of the ScheduleView interface that provides functionality
 * to render a Schedule object into a human-readable string representation.
 */
public class ScheduleViewModel implements ScheduleView{

  @Override
  public String render(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule is null");
    }

    StringBuilder view = new StringBuilder();
    schedule.sortSchedule();

    for (int day = 0; day < 7; day++) {
      int index = (day == 0) ? day + 7 : day;
      DayOfWeek currentDay = DayOfWeek.of(index);
      view.append(TimeUtilities.formatDay(currentDay)).append(":").append(System.lineSeparator());
      for (Event event: schedule.getEvents()) {
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
        view.append(System.lineSeparator());
      }
    }
    return view.toString();
  }
}
