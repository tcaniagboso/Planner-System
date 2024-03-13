package plannersystem;

import java.io.File;
import java.util.List;

import event.Event;
import user.User;

public interface PlannerSystem {

  void readUserSchedule(File xmlFile);

  void saveUserSchedule();

  String displayUserSchedule(String userId);

  void createEvent(String userId, String name, String startDay, String startTime, String endDay,
                   String endTime, boolean isOnline, String location, List<User> invitees);

  void modifyEvent(String userId, Event event, String name, String startDay, String startTime,
                   String endDay, String endTime, boolean isOnline, String location,
                   List<User> invitees);

  void modifyEvent(String userId, Event event, String name);

  void modifyEvent(String userId, Event event, String startDay, String startTime, String endDay,
                   String endTime);

  void modifyEvent(String userId, Event event, boolean isOnline, String location);

  void modifyEvent(String userId, Event event, List<User> invitees);

  void removeEvent(String userId, Event event);

  void automaticScheduling(String time);

  void showEvents(String userId, String startDay, String startTime, String endDay, String endTime);


}
