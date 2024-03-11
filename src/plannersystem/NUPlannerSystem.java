package plannersystem;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import event.Event;
import user.User;

public class NUPlannerSystem implements PlannerSystem{
  Map<String, User> users;

  public NUPlannerSystem() {
    this.users = new HashMap<>();
  }
  @Override
  public void readUserSchedule(File xmlFile) {

  }

  @Override
  public File saveUserSchedule() {
    return null;
  }

  @Override
  public String displayUserSchedule(User user) {
    return null;
  }

  @Override
  public void createEvent(Event event, List<String> invitees) {

  }

  @Override
  public void modifyEvent(Event event) {

  }

  @Override
  public void removeEvent(Event event) {

  }

  @Override
  public void automaticScheduling(String time) {

  }

  @Override
  public void showEvents(String startTime) {

  }
}
