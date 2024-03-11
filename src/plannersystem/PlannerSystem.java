package plannersystem;

import java.io.File;
import java.util.List;

import event.Event;
import user.User;

public interface PlannerSystem {

  void readUserSchedule(File xmlFile);

  File saveUserSchedule();

  String displayUserSchedule(User user);

  void createEvent(Event event, List<String> invitees);

  void modifyEvent(Event event);

  void removeEvent(Event event);

  void automaticScheduling(String time);

  void showEvents(String startTime);


}
