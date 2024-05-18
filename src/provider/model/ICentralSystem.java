package provider.model;

import schedule.ISchedule;
import schedule.ReadOnlyEvent;

/**
 * The ICentralSystem interface defines the behaviours and observations (via extending a
 * ReadOnlyCentralSystem) for the Central System class.
 * A client can add itself as user, retrieve user details, manage events associated with a
 * particular user including adding, removing and modifying events as well as loading and saving
 * user schedules to and from files.
 */
public interface ICentralSystem extends ReadOnlyCentralSystem {

  /**
   * Adds a new user Schedule to the system with the specified userName.
   *
   * @param userName the name of the user being added
   * @throws IllegalArgumentException if a user with the given username already exists
   */
  void addSchedule(String userName);

  /**
   * Removes the user with the specified username from the system.
   *
   * @param userName the users unique username
   * @return Boolean true if the user schedule was successfully removed
   */
  boolean removeSchedule(String userName);


  /**
   * Adds an event to all invitees schedules.
   *
   * @param event the event to be added
   * @throws IllegalArgumentException if one of the invitees already has a conflict
   */
  void createNewEvent(ReadOnlyEvent event);

  /**
   * A Host cancels an event, effectively removing the event from everyone's schedule.
   *
   * @param userName the users unique username
   * @param event    the event that will be cancelled
   */
  void hostDeleteEvent(String userName, ReadOnlyEvent event);

  /**
   * Modifies a host's event by overriding it with a given event.
   *
   * @param oldEvent    the oldEvent to be modified
   * @param updateEvent the new event to be updated
   * @throws IllegalArgumentException if the oldEvent is not in the system.
   */

  void modifyEvent(ReadOnlyEvent oldEvent, ReadOnlyEvent updateEvent);

  /**
   * Adds an already existing Schedule object to the central system.
   * @param newSchedule the new schedule to be added
   * @throws IllegalArgumentException if a schedule with that username already exists.
   */
  void addFullSchedule(ISchedule newSchedule);

}