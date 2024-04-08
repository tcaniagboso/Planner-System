package controller.command;

/**
 * The Command interface is part of the Command design pattern, which encapsulates a request as
 * an object, thereby allowing for parameterization of clients with queues, requests, and
 * operations.
 * Implementing this interface enables the creation of concrete command objects that encapsulate
 * specific actions and parameters. These commands can be executed at a later time, providing
 * flexibility in the scheduling and execution of operations.
 */
public interface Command {

  /**
   * Executes the command encapsulated by the implementor. This method should contain
   * the logic necessary to perform the action represented by the command. The specifics
   * of this execution logic will vary depending on the implementor's purpose and the action
   * it encapsulates.
   * Implementations may perform a wide range of operations, including, but not limited to,
   * modifying data, invoking methods on objects, or triggering external processes. The method
   * may also support undo functionality to reverse the effects of the execution, though this
   * capability is not a requirement of the interface itself.
   */
  void execute();
}
