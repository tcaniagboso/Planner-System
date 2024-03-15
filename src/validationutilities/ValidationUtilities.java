package validationutilities;

public class ValidationUtilities {

  public static void validateNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Argument is null");
    }
  }
}
