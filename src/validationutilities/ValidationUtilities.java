package validationutilities;

public class ValidationUtilities {

  public static void validateNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Argument is null");
    }
  }

  public static void validateGetNull(Object obj) {
    if (obj == null) {
      throw new IllegalStateException("Field is null.");
    }
  }
}
