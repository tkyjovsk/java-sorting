package tkyjovsk.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.SequencedMap;
import java.util.Set;

public class ItemComparator implements Comparator<Item> {

  public enum SortingOrder {
    ASCENDING, DESCENDING
  }

  private final SequencedMap<String, SortingOrder> sortingSequence;
  private final SequencedMap<String, SortingOrder> sortingSequenceUnmodifiable;

  public ItemComparator() {
    this.sortingSequence = new LinkedHashMap<>();
    this.sortingSequenceUnmodifiable = Collections.unmodifiableSequencedMap(this.sortingSequence);
  }

  public SequencedMap<String, SortingOrder> getSortingSequence() {
    return sortingSequenceUnmodifiable;
  }

  public ItemComparator clear() {
    sortingSequence.clear();
    return this;
  }

  public ItemComparator remove(String sortingKey) {
    sortingSequence.remove(sortingKey);
    return this;
  }

  public ItemComparator add(String sortingKey, SortingOrder ascending) {
    remove(sortingKey);
    sortingSequence.putLast(sortingKey, ascending);
    return this;
  }

  public ItemComparator add(String sortingKey) {
    return add(sortingKey, SortingOrder.ASCENDING);
  }

  public ItemComparator addFirst(String sortingKey, SortingOrder ascending) {
    remove(sortingKey);
    sortingSequence.putFirst(sortingKey, ascending);
    return this;
  }

  public ItemComparator addFirst(String sortingKey) {
    return addFirst(sortingKey, SortingOrder.ASCENDING);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (sortingSequence.isEmpty()) {
      sb.append("Sorting sequence empty.");
    } else {
      sb.append("Sorting");
      for (Entry<String, SortingOrder> sse : sortingSequence.entrySet()) {
        sb.append(" by ").append(sse.getKey());
        sb.append(" ").append(sse.getValue().equals(SortingOrder.ASCENDING) ? "⯅" : "⯆");
      }
    }
    sb.append("\n");
    return sb.toString();
  }

  private int compare(Item i1, Item i2, String key, SortingOrder sortingOrder) {
    String v1 = i1.get(key);
    String v2 = i2.get(key);
    int diff = v1 == null
            ? v2 == null ? 0 : -1
            : v2 == null ? 1 : v1.compareTo(v2);
    return switch (sortingOrder) {
      case ASCENDING ->
        diff;
      case DESCENDING ->
        -diff;
    };
  }

  @Override
  public int compare(Item i1, Item i2) {
    int diff = 0;
    Set<String> unsortedKeys = new HashSet<>(i1.keySet());
    unsortedKeys.addAll(i2.keySet());
    for (Entry<String, SortingOrder> sse : sortingSequence.entrySet()) {
      diff = compare(i1, i2, sse.getKey(), sse.getValue());
      unsortedKeys.remove(sse.getKey());
      if (diff != 0) {
        break;
      }
    }
    if (diff == 0) { // natural ordering for the rest of the keys (those not specified in the sortingSequence)
      for (String key : unsortedKeys) {
        diff = compare(i1, i2, key, SortingOrder.ASCENDING);
        if (diff != 0) {
          break;
        }
      }
    }
    return diff;
  }

}
