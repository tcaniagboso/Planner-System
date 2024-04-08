package view;

import java.util.ArrayList;
import java.util.List;

import autoscheduling.AutoSchedule;
import autoscheduling.ScheduleStrategyCreator;
import controller.PlannerSystemController;
import controller.ScheduleViewController;
import plannersystem.PlannerSystem;
import schedule.Event;
import schedule.Schedule;

import plannersystem.NUPlannerSystem;

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
   *             Supports "Any time", "Work hours", and "Lenient" as inputs to select the respective
   *             scheduling strategy. If no argument is provided, defaults to "Any time".
   */
  public static void main(String[] args) {
    // Creating events
    Event event1 = new Event();
    event1.setName("CS3500 Lecture Morning");
    event1.setEventTimes("Tuesday", "0950", "Tuesday", "1130");
    event1.setLocation(true, "Churchill Hall Room 109");
    event1.setHost("Tobe");
    event1.setInvitees(new ArrayList<>(List.of("Tobe", "Karina")));

    Event event2 = new Event();
    event2.setName("Assignments");
    event2.setEventTimes("Friday", "0950", "Sunday", "1130");
    event2.setLocation(true, "Home");
    event2.setHost("Tobe");
    event2.setInvitees(new ArrayList<>(List.of("Tobe", "Karina")));

    Event event3 = new Event();
    event3.setName("Football");
    event3.setEventTimes("Wednesday", "0950", "Thursday", "1130");
    event3.setLocation(false, "Carter Field");
    event3.setHost("Tobe");
    event3.setInvitees(new ArrayList<>(List.of("Tobe")));

    // Creating schedules for users
    Schedule tobeSchedule = new Schedule("Tobe");
    Schedule karinaSchedule = new Schedule("Karina");
    tobeSchedule.addEvent(event1);
    tobeSchedule.addEvent(event2);
    tobeSchedule.addEvent(event3);
    karinaSchedule.addEvent(event1);
    karinaSchedule.addEvent(event2);

    // Creating a list of schedules
    List<Schedule> schedules = new ArrayList<>(List.of(tobeSchedule, karinaSchedule));

    // Creating a read-only planner system with the provided schedules
    PlannerSystem system = new NUPlannerSystem(schedules);
    assignStrategy(args, system);

    // Initializing the planner system view
    PlannerSystemViewImpl systemView = new PlannerSystemViewImpl(system);
    PlannerSystemController controller = new ScheduleViewController(systemView);
    controller.launch(system);
  }

  /**
   * Assigns a scheduling strategy to the planner system based on the provided command-line argument.
   * Supports "Any time", "Work hours", and "Lenient" as scheduling strategies.
   *
   * @param args   The command-line arguments specifying the desired scheduling strategy.
   * @param system The planner system to which the strategy will be applied.
   * @throws IllegalArgumentException if an unsupported scheduling strategy is specified.
   */
  public static void assignStrategy(String[] args, PlannerSystem system) {
    String strategy = (args.length > 0) ? args[0].strip() : "Anytime";
    AutoSchedule scheduleStrategy;
    switch (strategy) {
      case "Anytime":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategy.ANYTIME);
        break;
      case "Work-hours":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategy.WORKHOURS);
        break;
      case "Lenient":
        scheduleStrategy = ScheduleStrategyCreator
                .createScheduleStrategy(ScheduleStrategyCreator.ScheduleStrategy.LENIENT);
        break;
      default:
        throw new IllegalArgumentException("Unknown Schedule Strategy: " + strategy);
    }
    system.setScheduleStrategy(scheduleStrategy);
  }
}
