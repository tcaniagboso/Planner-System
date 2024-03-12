package plannersystem;

import java.io.File;
import java.util.List;

import event.Event;
import user.User;

public interface PlannerSystem {

  void readUserSchedule(File xmlFile);

  void saveUserSchedule();

  String displayUserSchedule(User user);

  void createEvent(String name, String startDay, String startTime, String endDay,
                   String endTime, boolean isOnline, String location, List<User> invitees);

  void modifyEvent(Event event);

  void modifyEvent(Event event, String name);

  void modifyEvent(Event event, String startDay, String startTime, String endDay,
                   String endTime);

  void modifyEvent(Event event, boolean isOnline, String location);

  void modifyEvent(Event event, List<User> invitees);

  void removeEvent(Event event);

  void automaticScheduling(String time);

  void showEvents(String startDay, String startTime, String endDay, String endTime);


}
