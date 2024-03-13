package plannersystem;

import java.io.File;
import java.util.List;

import event.Event;
import user.User;

public interface PlannerSystem {

  void readUserSchedule(File xmlFile);

  void saveUserSchedule();

  String displayUserSchedule(String userID);

  void createEvent(String userID, String name, String startDay, String startTime, String endDay,
                   String endTime, boolean isOnline, String location, List<User> invitees);

  void modifyEvent(String userID, Event event);

  void modifyEvent(String userID, Event event, String name);

  void modifyEvent(String userID, Event event, String startDay, String startTime, String endDay,
                   String endTime);

  void modifyEvent(String userID, Event event, boolean isOnline, String location);

  void modifyEvent(String userID, Event event, List<User> invitees);

  void removeEvent(String userID, Event event);

  void automaticScheduling(String time);

  void showEvents(String startDay, String startTime, String endDay, String endTime);


}
