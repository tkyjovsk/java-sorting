package tkyjovsk.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static tkyjovsk.sorting.Item.AGE;
import static tkyjovsk.sorting.Item.NAME;
import static tkyjovsk.sorting.Item.RANK;
import static tkyjovsk.sorting.ItemComparator.*;

public class ItemComparatorTest {

  ItemTreeSet items;
  Item alice;
  Item bob;
  Item carol;
  Item david;
  Item eve;

  List<Item> itemsByNameAscending = new ArrayList<>();
  List<Item> itemsByAgeAscending = new ArrayList<>();
  List<Item> itemsByAgeAscendingByNameDescending = new ArrayList<>();
  List<Item> itemsByRankAscending = new ArrayList<>();

  @BeforeEach
  public void createItems() {
    alice = new Item().set(NAME, "Alice").set(AGE, "25").set(RANK, "4");
    bob = new Item().set(NAME, "Bob").set(AGE, "20").set(RANK, "4");
    carol = new Item().set(NAME, "Carol").set(AGE, "30").set(RANK, "3");
    david = new Item().set(NAME, "David").set(RANK, "2");
    eve = new Item().set(NAME, "Eve").set(AGE, "30").set(RANK, "1");
    Collections.addAll(itemsByNameAscending, alice, bob, carol, david, eve);
    Collections.addAll(itemsByAgeAscending, david, bob, alice, carol, eve);
    Collections.addAll(itemsByAgeAscendingByNameDescending, david, bob, alice, eve, carol);
    Collections.addAll(itemsByRankAscending, eve, david, carol, bob, alice);
  }

  private void assertSameOrder(List<Item> expectedItems, SortedSet<Item> items) {
    assertEquals(expectedItems.size(), items.size());
    Iterator<Item> i1 = expectedItems.iterator();
    Iterator<Item> i2 = items.iterator();
    while (i1.hasNext()) {
      assertEquals(i1.next(), i2.next());
    }
  }

  private void printItemList(List<Item> list) {
    StringBuilder sb = new StringBuilder("Item List\n");
    for (Item i : list) {
      sb.append("  - ").append(i).append("\n");
    }
    System.out.println(sb.toString());
  }

  private void addItems() {
    System.out.println("Original data");
    printItemList(itemsByNameAscending);
    items.addAll(itemsByNameAscending);
  }

  @Test
  public void addSameItemTwice() {
    items = new ItemTreeSet(new ItemComparator().clear());

    items.add(alice);
    assertEquals(1, items.size());

    items.add(alice);
    assertEquals(1, items.size());
  }

  private void checkReAddingExitingItems() {
    Collection<Item> c = new ArrayList<>(items);
    for (Item i : c) {
      System.out.printf("Re-adding %s\n", i);
      items.add(i);
      System.out.println(items);
      assertEquals(c.size(), items.size());
    }
  }

  @Test
  public void dontSort() {
    items = new ItemTreeSet(new ItemComparator().clear());
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByNameAscending, items);
  }

  @Test
  public void sortByNameAscending() {
    items = new ItemTreeSet(new ItemComparator().clear().add(SORT_BY_NAME_ASC));
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByNameAscending, items);
  }

  @Test
  public void sortByNameDescending() {
    items = new ItemTreeSet(new ItemComparator().clear().add(SORT_BY_NAME_DESC));
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByNameAscending.reversed(), items);
  }

  @Test
  public void sortByAgeAscending() {
    items = new ItemTreeSet(new ItemComparator().clear().add(SORT_BY_AGE_ASC));
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByAgeAscending, items);
  }

  @Test
  public void sortByAgeDescending() {
    items = new ItemTreeSet(new ItemComparator().clear().add(SORT_BY_AGE_DESC));
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByAgeAscending.reversed(), items);
  }

  @Test
  public void sortByAgeAscendingByNameDescending() {
    items = new ItemTreeSet(new ItemComparator().clear().add(SORT_BY_NAME_DESC).add(SORT_BY_AGE_ASC));
    addItems();
    System.out.println((ItemComparator) items.comparator());
    System.out.println(items);
    assertEquals(5, items.size());
    checkReAddingExitingItems();
    assertSameOrder(itemsByAgeAscendingByNameDescending, items);
  }

}
