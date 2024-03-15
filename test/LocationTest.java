import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import schedule.Location;

/**
 * Tests the functionality of the {@link Location} class, ensuring that
 * setting and getting location names and online status work as expected.
 */
public class LocationTest {

  /**
   * The {@link Location} instance used for testing.
   */
  private Location location;

  /**
   * Initializes a new {@link Location} instance before each test.
   */
  @Before
  public void init() {
    location = new Location();
  }

  /**
   * Tests the setLocation and getLocation methods of the {@link Location} class.
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

    location.setLocation("International Village");
    Assert.assertEquals("International Village", location.getLocation());
  }

  /**
   * Tests the setOnline and isOnline methods of the {@link Location} class.
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

  @Test
  public void testEquals() {
    Location spot = new Location();

    location.setOnline(true);
    location.setLocation("place");

    spot.setOnline(false);
    spot.setLocation("place");

    Assert.assertNotEquals(location, spot);

    spot.setOnline(true);
    Assert.assertEquals(location, spot);
  }
}
