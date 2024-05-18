import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import schedule.ILocation;
import schedule.Location;

/**
 * Tests the functionality of the {@link ILocation} class, ensuring that
 * setting and getting location names and online status work as expected.
 */
public class LocationTest {

  /**
   * The {@link Location} instance used for testing.
   */
  private ILocation location;

  /**
   * Initializes a new {@link Location} instance before each test.
   */
  @Before
  public void init() {
    location = new Location();
  }

  /**
   * Tests the setLocation and getLocation methods of the {@link ILocation} class.
   * Checks that:
   * <ul>
   *   <li>Setting a location to null throws an {@link IllegalArgumentException}.</li>
   *   <li>Setting a location to an empty string throws an {@link IllegalArgumentException}.</li>
   *   <li>Setting and getting a valid location name works as expected.</li>
   * </ul>
   */
  @Test
  public void testSetGetLocation() {
    Assert.assertThrows(IllegalArgumentException.class, () -> location.setLocation(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> location.setLocation(""));
    Assert.assertThrows(IllegalStateException.class, () -> location.getLocation());

    location.setLocation("International Village");
    Assert.assertEquals("International Village", location.getLocation());
  }

  /**
   * Tests the setOnline and isOnline methods of the {@link ILocation} class.
   * Checks that:
   * <ul>
   *   <li>The default online status is false.</li>
   *   <li>Setting and getting the online status works as expected.</li>
   * </ul>
   */
  @Test
  public void testSetGetOnline() {
    Assert.assertFalse(location.isOnline());
    location.setOnline(true);
    Assert.assertTrue(location.isOnline());
  }

  /**
   * Tests the equality and inequality of {@link ILocation} objects. This test first verifies that
   * two {@link Location} instances with different online statuses are not considered equal,
   * even if they share the same physical location. It then updates one {@link ILocation} instance
   * to have the same online status as the other, and verifies that the two locations are considered
   * equal. This ensures the {@link Location#equals(Object)} method correctly evaluates both the
   * online status and the physical location string in determining equality.
   */
  @Test
  public void testEquals() {
    ILocation spot = new Location();

    location.setOnline(true);
    location.setLocation("place");

    spot.setOnline(false);
    spot.setLocation("place");

    Assert.assertNotEquals(location, spot);

    spot.setOnline(true);
    Assert.assertEquals(location, spot);
  }
}
