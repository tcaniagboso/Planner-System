package validationutilities;

/**
 * Provides utility methods for performing common validation tasks.
 * This class is designed to ensure that objects meet certain criteria before proceeding
 * with further processing.
 */
public class ValidationUtilities {

  /**
   * Validates that an object is not null.
   * Throws an {@link IllegalArgumentException} if the object is null.
   *
   * @param o The object to validate.
   * @throws IllegalArgumentException if the provided object is null.
   */
  public static void validateNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Argument is null");
    }
  }

  /**
   * Validates that a field of an object is not null.
   * This method is typically used to ensure that a field which is lazily initialized or set after
   * construction is not null at the time of access.
   * Throws an {@link IllegalStateException} if the field is null.
   *
   * @param obj The field of the object to validate.
   * @throws IllegalStateException if the field is null.
   */
  public static void validateGetNull(Object obj) {
    if (obj == null) {
      throw new IllegalStateException("Field is null.");
    }
  }
}
