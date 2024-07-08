package tkyjovsk.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;
import static tkyjovsk.sorting.Item.AGE;
import static tkyjovsk.sorting.Item.NAME;
import static tkyjovsk.sorting.Item.RANK;

public class ItemComparator implements Comparator<Item> {

  public static final SortingKey SORT_BY_NAME_ASC = new SortingKey(NAME);
  public static final SortingKey SORT_BY_NAME_DESC = new SortingKey(NAME, false);
  public static final SortingKey SORT_BY_AGE_ASC = new SortingKey(AGE);
  public static final SortingKey SORT_BY_AGE_DESC = new SortingKey(AGE, false);
  public static final SortingKey SORT_BY_RANK_ASC = new SortingKey(RANK);
  public static final SortingKey SORT_BY_RANK_DESC = new SortingKey(RANK, false);

  private final SequencedSet<SortingKey> sortingSequence;
  private final SequencedSet<SortingKey> sortingSequenceUnmodifiable;

  public ItemComparator(SequencedSet<SortingKey> sorting) {
    this.sortingSequence = sorting;
    this.sortingSequenceUnmodifiable = Collections.unmodifiableSequencedSet(this.sortingSequence);
  }

  public ItemComparator() {
    this(new LinkedHashSet<>());
  }

  public ItemComparator clear() {
    sortingSequence.clear();
    return this;
  }

  public ItemComparator addFirst(SortingKey key) {
    sortingSequence.remove(key);
    sortingSequence.addFirst(key);
    return this;
  }

  public ItemComparator add(SortingKey key) {
    sortingSequence.remove(key);
    sortingSequence.add(key);
    return this;
  }

  public SequencedSet<SortingKey> getSortingSequence() {
    return sortingSequenceUnmodifiable;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (sortingSequence.isEmpty()) {
      sb.append("Not sorting.");
    } else {
      sb.append("Sorting");
      for (SortingKey sk : sortingSequence) {
        sb.append(" ").append(sk).append(" ");
      }
    }
    sb.append("\n");
    return sb.toString();
  }

  private int compare(Item i1, Item i2, String key, boolean ascending) {
    String v1 = i1.get(key);
    String v2 = i2.get(key);
    int diff = v1 == null
            ? v2 == null ? 0 : -1
            : v2 == null ? 1 : v1.compareTo(v2);
    return ascending ? diff : -diff;
  }

  @Override
  public int compare(Item i1, Item i2) {
    int diff = 0;
    Set<String> unsortedKeys = new HashSet<>(i1.keySet());
    unsortedKeys.addAll(i2.keySet());
    for (SortingKey sk : sortingSequence) {
      diff = compare(i1, i2, sk.key, sk.ascending);
      unsortedKeys.remove(sk.key);
      if (diff != 0) {
        break;
      }
    }
    if (diff == 0) { // natural sort for the keys not specified in the sortingSequence sequence
      for (String key : unsortedKeys) {
        diff = compare(i1, i2, key, true);
        if (diff != 0) {
          break;
        }
      }
    }
    return diff;
  }

}
