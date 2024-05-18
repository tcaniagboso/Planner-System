package controller;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import schedule.IEvent;
import schedule.ISchedule;
import schedulestrategy.ScheduleStrategy;
import schedulestrategy.ScheduleStrategyCreator;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;

import plannersystem.NUPlannerSystem;
import view.PlannerSystemViewImpl;

/**
 * The Main class serves as the entry point for the Planner System application.
 * It demonstrates the functionality of the system by creating events, schedules,
 * and initializing the planner system view. Additionally, it supports selecting
 * a scheduling strategy based on command-line arguments to demonstrate different
 * scheduling behaviors.
 */
public class Main {

  /**
   * Initializes the Planner System application by creating sample events and schedules,
   * and setting up the user interface. Based on the command-line arguments, it selects a
   * scheduling strategy to be used within the application. Demonstrates the creation of events,
   * assignment of events to schedules, and the use of different scheduling strategies.
   *
   * @param args The command-line arguments to select a scheduling strategy.
   *             Supports "Anytime", "Work-hours", and "Lenient" as inputs to select the respective
   *             scheduling strategy. If no argument is provided, defaults to "Anytime".
   *             Additionally, a chosen day of the week like "Saturday" can be used to set the
   *             start day of the week.
   */
  public static void main(String[] args) {
    // Creating events
    IEvent event1 = new Event();
    event1.setName("CS3500 Lecture Morning");
    event1.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event1.setLocation(true, "Churchill Hall Room 109");
    event1.setHost("Tobe");
    event1.setInvitees(new ArrayList<>(List.of("Tobe", "Karina")));

    IEvent event2 = new Event();
    event2.setName("Assignments");
    event2.setEventTimes("Friday", "0950", "Sunday", "1130");
    event2.setLocation(true, "Home");
    event2.setHost("Tobe");
    event2.setInvitees(new ArrayList<>(List.of("Tobe", "Karina")));

    IEvent event3 = new Event();
    event3.setName("Football");
    event3.setEventTimes("Wednesday", "0950", "Thursday", "1130");
    event3.setLocation(false, "Carter Field");
    event3.setHost("Tobe");
    event3.setInvitees(new ArrayList<>(List.of("Tobe")));

    // Creating schedules for users
    ISchedule tobeSchedule = new Schedule("Tobe");
    ISchedule karinaSchedule = new Schedule("Karina");
    tobeSchedule.addEvent(event1);
    tobeSchedule.addEvent(event2);
    tobeSchedule.addEvent(event3);
    karinaSchedule.addEvent(event1);
    karinaSchedule.addEvent(event2);

    // Creating a list of schedules
    List<ISchedule> schedules = new ArrayList<>(List.of(tobeSchedule, karinaSchedule));
    // Creating a read-only planner system with the provided schedules
    PlannerSystem system = new NUPlannerSystem(schedules);
    system.setFirstDayOfWeek(setPlannerFirstDay(args));
    system.setScheduleStrategy(getStrategy(args));
    // Initializing the planner system view
    PlannerSystemViewImpl systemView = new PlannerSystemViewImpl(system);
    PlannerSystemController controller = new ScheduleViewController(systemView);
    controller.launch(system);
  }

  /**
   * Sets the first day of the week for the planner based on the command line arguments provided.
   * This method attempts to parse and validate the second argument as a day of the week.
   * If the day is valid, it returns the day in uppercase. If not, it throws an exception.
   * If no day is specified or the arguments are less than required, it defaults to "SUNDAY".
   *
   * @param args Command line arguments passed to the application. It expects the day of the week
   *             as the second argument (index 1).
   * @return The uppercase string of the valid day of the week if provided and valid, or "SUNDAY"
   *         if no valid day is provided or arguments are insufficient.
   * @throws IllegalArgumentException if the provided day argument is invalid or not a recognized
   *                                  day of the week.
   */
  private static String setPlannerFirstDay(String[] args) {
    if (args.length > 1) {
      String day = args[1].strip();
      try {
        DayOfWeek.valueOf(day.toUpperCase());
        return day.toUpperCase();
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid Day argument");
      }
    }
    return "SUNDAY";
  }

  /**
   * Returns the chosen schedule strategy for the planner system based on the provided command-line
   * argument.
   * Supports "Anytime", "Work-hours", and "Lenient" as scheduling strategies.
   *
   * @param args The command-line arguments specifying the desired scheduling strategy.
   * @return the chosen schedule strategy.
   * @throws IllegalArgumentException if an unsupported scheduling strategy is specified.
   */
  public static ScheduleStrategy getStrategy(String[] args) {
    String strategy = (args.length > 0) ? args[0].strip() : "Anytime";
    ScheduleStrategy scheduleStrategy;
    switch (strategy) {
      case "Anytime":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategyType.ANYTIME);
        break;
      case "Work-hours":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategyType.WORKHOURS);
        break;
      case "Lenient":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategyType.LENIENT);
        break;
      default:
        throw new IllegalArgumentException("Unknown Schedule Strategy: " + strategy);
    }
    return scheduleStrategy;
  }
}
