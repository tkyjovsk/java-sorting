package tkyjovsk.sorting;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.TreeSet;

public class ItemTreeSet extends TreeSet<Item> {
  
  public ItemTreeSet(Comparator<? super Item> comparator) {
    super(comparator);
  }
  
  public SequencedSet<String> keyset() {
    SequencedSet<String> ks = new LinkedHashSet<>();
    for (Item i : this) {
      ks.addAll(i.keySet());
    }
    return ks;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ItemTreeSet ");
    sb.append(keyset()).append("\n");
    for (Item i : this) {
      sb.append("  - ").append(i).append("\n");
    }
    return sb.toString();
  }
  
}
;
