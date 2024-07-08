package tkyjovsk.sorting;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;
import static tkyjovsk.sorting.Item.AGE;
import static tkyjovsk.sorting.Item.NAME;
import static tkyjovsk.sorting.Item.RANK;

public class ItemComparator implements Comparator<Item> {

  public static final SortKey SORT_BY_NAME_ASC = new SortKey(NAME);
  public static final SortKey SORT_BY_NAME_DESC = new SortKey(NAME, false);
  public static final SortKey SORT_BY_AGE_ASC = new SortKey(AGE);
  public static final SortKey SORT_BY_AGE_DESC = new SortKey(AGE, false);
  public static final SortKey SORT_BY_RANK_ASC = new SortKey(RANK);
  public static final SortKey SORT_BY_RANK_DESC = new SortKey(RANK, false);

  private SequencedSet<SortKey> sorting;

  public ItemComparator(SequencedSet<SortKey> sorting) {
    this.sorting = sorting;
  }

  public ItemComparator() {
    this(new LinkedHashSet<>());
  }

  public ItemComparator clear() {
    sorting.clear();
    return this;
  }

  /**
   * Adds a key at the end of the sorting sequence. If already exists it will be
   * removed and re-added at the end.
   *
   * @param key
   * @return
   */
  public ItemComparator add(SortKey key) {
    sorting.remove(key);
    sorting.add(key);
    return this;
  }

  public SequencedSet<SortKey> getSorting() {
    return sorting;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (sorting.isEmpty()) {
      sb.append("Not sorting.");
    } else {
      sb.append("Sorting");
      for (SortKey sk : sorting) {
        sb.append(" ").append(sk).append(" ");
      }
    }
    sb.append("\n");
    return sb.toString();
  }

  @Override
  public int compare(Item i1, Item i2) {
    int diff = 0;
    Set<String> unsortedKeys = new HashSet<>(i1.keySet());
    unsortedKeys.addAll(i2.keySet());
    for (SortKey sb : sorting.reversed()) {
      String v1 = i1.get(sb.key);
      String v2 = i2.get(sb.key);
      diff = v1 == null
              ? v2 == null ? 0 : -1
              : v2 == null ? 1 : v1.compareTo(v2);
      diff = sb.ascending ? diff : -diff;
      unsortedKeys.remove(sb.key);
      if (diff != 0) {
        break;
      }
    }
    if (diff == 0 && !unsortedKeys.isEmpty() && !i1.equals(i2)) {
      diff = 1;
    }
    return diff;
  }

}
