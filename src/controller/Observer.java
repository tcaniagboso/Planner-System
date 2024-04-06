package controller;

/**
 * The Observer interface is part of the Observer design pattern, which is used for creating
 * a subscription mechanism to allow multiple objects to listen and react to events or changes
 * in another object, known as the subject.
 * Implementers of this interface are considered observers that need to be notified by the subject
 * (typically a model or a controller) about certain changes or updates, allowing them to react
 * accordingly.
 */
public interface Observer {

  /**
   * Called by the subject (the object being observed) to notify this observer about an event or
   * change in state. Implementers should include logic within this method to react to the updates
   * provided by the subject.
   * This method constitutes the core of the Observer design pattern, enabling decoupled
   * communication between subjects and observers.
   */
  void update();
}
