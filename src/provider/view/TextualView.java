package provider.view;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;

/**
 * Provides a textual representation of a user's schedule, listing events
 * by day of the week.
 */
public class TextualView {
  private final ISchedule schedule;

  /**
   * Constructs a textual view of the given schedule.
   *
   * @param schedule The schedule to be viewed.
   */
  public TextualView(ISchedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Generates a textual representation of the schedule, organized by day of the week.
   * Each event is detailed with its name, time, location, online status, and invitees.
   *
   * @return A string representation of the schedule.
   */
  public String generateView() {
    StringBuilder viewBuilder = new StringBuilder();
    viewBuilder.append("User: ").append(schedule.getUserName()).append("\n");

    EnumMap<DayOfWeek, StringBuilder> dayViews = new EnumMap<>(DayOfWeek.class);
    for (DayOfWeek day : DayOfWeek.values()) {
      dayViews.put(day, new StringBuilder());
    }

    for (ReadOnlyEvent event : schedule.getEvents()) {
      StringBuilder eventView = dayViews.get(event.getStartDay());

      if (eventView.length() > 0) {
        eventView.append("\n");
      }

      eventView.append("        name: ").append(event.getName()).append("\n")
              .append("        time: ").append(capitalizeFirstLetter(event.getStartDay()
                              .toString()))
              .append("        time: ").append(capitalizeFirstLetter(
                      event.getStartDay().toString()))
              .append(": ").append(formatTime(event.getStartTime()))
              .append(" -> ").append(capitalizeFirstLetter(event.getEndDay().toString()))
              .append(": ").append(formatTime(event.getEndTime())).append("\n")
              .append("        location: ").append(event.getLocation()).append("\n")
              .append("        online: ").append(event.isOnline() ? "true" : "false").append("\n")
              .append("        invitees: ");

      List<String> invitees = event.getInvitees();
      for (int i = 0; i < invitees.size(); i++) {
        if (i > 0) {
          eventView.append(", ");
        }
        if (i > 0) {
          eventView.append(", ");
        }
        eventView.append(invitees.get(i));
      }
    }

    DayOfWeek[] days = DayOfWeek.values();
    for (int i = 6; i < days.length + 6; i++) {
      DayOfWeek day = days[i % days.length];
      StringBuilder dayView = dayViews.get(day);
      viewBuilder.append(capitalizeFirstLetter(day.toString())).append(":");
      if (dayView.length() > 0) {
        viewBuilder.append("\n").append(dayView);
      }
      viewBuilder.append("\n");
    }
    return viewBuilder.toString().trim();
  }

  private String capitalizeFirstLetter(String original) {
    if (original == null || original.length() == 0) {
      return original;
    }
    return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
  }

  private String formatTime(int time) {
    if (time < 1000) {
      return String.valueOf(time);
    } else {
      return String.format("%04d", time);
    }
  }

}
