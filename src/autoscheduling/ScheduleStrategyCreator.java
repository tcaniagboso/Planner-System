package autoscheduling;

/**
 * A factory class for creating scheduling strategy instances based on specified strategy types.
 * Supports the creation of different scheduling strategies, including 'Anytime', 'WorkHours',
 * and 'Lenient' scheduling. Each strategy has its own implementation of the AutoSchedule interface
 * to handle event scheduling based on different criteria and constraints.
 */
public class ScheduleStrategyCreator {

  /**
   * Enumerates the types of scheduling strategies available for creating events.
   */
  public enum ScheduleStrategy {
    ANYTIME, // Represents a scheduling strategy that finds the first possible time slot.
    WORKHOURS, // Represents a scheduling strategy that finds a time slot within work hours.
    LENIENT // Represents a scheduling strategy that is more flexible with scheduling constraints.
  }

  /**
   * Creates an instance of a scheduling strategy based on the specified strategy type.
   * This method acts as a factory, returning an appropriate instance of AutoSchedule
   * that corresponds to the given strategy type.
   *
   * @param strategy The type of scheduling strategy to create, as defined in ScheduleStrategy.
   * @return An instance of AutoSchedule that implements the requested scheduling strategy.
   * @throws IllegalArgumentException if an unknown or unsupported strategy type is provided.
   */
  public static AutoSchedule createScheduleStrategy(ScheduleStrategy strategy) {
    switch (strategy) {
      case ANYTIME:
        return new AnyTimeSchedule();

      case WORKHOURS:
        return new WorkHourSchedule();

      case LENIENT:
        return new LenientSchedule();

      default:
        throw new IllegalArgumentException("Unknown Schedule Strategy: " + strategy);
    }
  }
}
