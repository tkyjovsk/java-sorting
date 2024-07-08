package tkyjovsk.sorting;

import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static tkyjovsk.sorting.Item.AGE;
import static tkyjovsk.sorting.Item.NAME;
import static tkyjovsk.sorting.Item.RANK;
import static tkyjovsk.sorting.ItemComparator.SORT_BY_AGE_ASC;
import static tkyjovsk.sorting.ItemComparator.SORT_BY_NAME_ASC;
import static tkyjovsk.sorting.ItemComparator.SORT_BY_NAME_DESC;
import static tkyjovsk.sorting.ItemComparator.SORT_BY_RANK_ASC;

public class ItemTest {

  @Test
  public void testEquals() {
    Item alice = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    Item alice2 = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    assertEquals(alice, alice2);

    Item bob = new Item().set(NAME, "Bob").set(AGE, "20").set(RANK, "4");
    assertNotEquals(alice, bob);

    alice2.remove(RANK);
    assertNotEquals(alice, alice2);

    alice.remove(RANK);
    assertEquals(alice, alice2);

    alice.remove(AGE);
    assertNotEquals(alice, alice2);

    alice2.remove(AGE);
    assertEquals(alice, alice2);
  }

  @Test
  public void testEmptyComparator() {
    Item alice = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    Item alice2 = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");

    ItemComparator c = new ItemComparator();
    assertEqual(c, alice, alice2);

    Item bob = new Item().set(NAME, "Bob").set(AGE, "20").set(RANK, "2");
    assertLargerThan(c, bob, alice);
  }

  @Test
  public void testSingleKeyComparator() {
    Item alice = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    Item alice2 = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");

    ItemComparator c = new ItemComparator().add(SORT_BY_AGE_ASC);
    assertEqual(c, alice, alice2);

    Item bob = new Item().set(NAME, "Bob").set(AGE, "20");
    assertLargerThan(c, alice, bob);

    c = new ItemComparator().add(SORT_BY_NAME_DESC);
    assertEqual(c, alice, alice2);
    assertLargerThan(c, alice, bob);

    c = new ItemComparator().add(SORT_BY_RANK_ASC);
    assertEqual(c, alice, alice2);
    assertLargerThan(c, alice, bob);
  }

  @Test
  public void testMultipleKeyComparator() {
    Item alice = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    Item alice2 = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    Item bob = new Item().set(NAME, "Bob").set(AGE, "20");
    Item carol = new Item().set(NAME, "Carol").set(AGE, "30").set(RANK, "2");

    ItemComparator c = new ItemComparator().add(SORT_BY_AGE_ASC).add(SORT_BY_NAME_ASC);
    assertEqual(c, alice, alice2);
    assertEqual(c, bob, bob);
    assertEqual(c, carol, carol);
    assertLargerThan(c, alice, bob);
    assertLargerThan(c, carol, bob);
    assertLargerThan(c, carol, alice);

    c = new ItemComparator().add(SORT_BY_RANK_ASC).add(SORT_BY_RANK_ASC);
    assertEqual(c, alice, alice2);
    assertEqual(c, bob, bob);
    assertEqual(c, carol, carol);
    assertLargerThan(c, alice, bob);
    assertLargerThan(c, alice, carol);
    assertLargerThan(c, carol, bob);
  }

  private void assertEqual(Comparator<Item> c, Item i1, Item i2) {
    assertEquals(0, c.compare(i1, i1));
    assertEquals(0, c.compare(i2, i2));
    assertEquals(0, c.compare(i1, i2));
    assertEquals(0, c.compare(i2, i1));
  }

  private void assertLargerThan(Comparator<Item> c, Item i1, Item i2) {
    assertTrue(c.compare(i1, i2) > 0);
    assertTrue(c.compare(i2, i1) < 0);
  }

}
