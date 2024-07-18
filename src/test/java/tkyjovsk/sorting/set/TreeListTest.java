package tkyjovsk.sorting.set;

import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.list.TreeList;
import org.junit.jupiter.api.Test;

public class TreeListTest {

  @Test
  public void testAddingSpeed() {
    List<String> list = new TreeList<>();
    List<String> listUnique = new TreeList<>();
    long listNanos = 0;
    long listUniqueNanos = 0;
    int n = 10_000;
    System.out.printf("Initializing %s items.\n", n);
    for (int i = 0; i < n; i++) {
      String s = UUID.randomUUID().toString();

      long nanos = System.nanoTime();
      list.add(s);
      listNanos += System.nanoTime() - nanos;

      nanos = System.nanoTime();
      if (!listUnique.contains(s)) {
        listUnique.add(s);
      }
      listUniqueNanos += System.nanoTime() - nanos;
    }
    System.out.printf("List.add(): %s ns per item\n", listNanos / n);
    System.out.printf("List.contains() && add(): %s ns per item\n", listUniqueNanos / n);
  }

}
