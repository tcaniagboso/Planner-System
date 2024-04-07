package autoscheduling;

import java.util.List;

import schedule.Event;
import schedule.Schedule;

public interface AutoSchedule {

  Event scheduleEvent(Event event, int duration, List<Schedule> scheduleList);
}
