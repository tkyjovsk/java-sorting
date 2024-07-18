package tkyjovsk.sorting.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class ListBackedSetTest {

  private void print(Collection c) {
    c.forEach(i -> System.out.println(i));
  }

  @Test
  public void addRandomUUIDs() {
    Set<String> treeset = new TreeSet<>();
    Set<String> lbset = new ListBackedSet<>((String s1, String s2) -> s1.compareTo(s2));
    int n = 10_000;
    System.out.printf("Inserting %s random items\n", n);
    for (int i = 0; i < n; i++) {
      String s = UUID.randomUUID().toString();
      treeset.add(s);
      lbset.add(s);
      assertEquals(treeset.size(), lbset.size());
    }
//    System.out.println("Control Set:");
//    print(hashset);
//    System.out.println("Tested Set:");
//    print(lbset);
    Iterator<String> tsi = treeset.iterator();
    Iterator<String> lbsi = lbset.iterator();
    while(tsi.hasNext()) {
      assertTrue(lbsi.hasNext());
      assertEquals(tsi.next(), lbsi.next());
    }
  }

  @Test
  public void addRandomUUIDsWithDuplicates() {
    List<String> list = new ArrayList<>();
    Set<String> hashset = new HashSet<>();
    Set<String> lbset = new ListBackedSet<>((String s1, String s2) -> s1.compareTo(s2));
    int n = 10_000;
    System.out.printf("Inserting %s random items\n", n);
    for (int i = 0; i < n; i++) {
      String s = UUID.randomUUID().toString();
      if (hashset.add(s)) {
        list.add(s);
      }
      lbset.add(s);
      assertEquals(hashset.size(), lbset.size());
    }
    assertEquals(hashset, lbset);
    int d = n / 5;
    System.out.printf("Inserting %s duplicates\n", d);
    for (int i = 0; i < d; i++) {
      String duplicate = list.get(ThreadLocalRandom.current().nextInt(hashset.size()));
      if (hashset.add(duplicate)) {
        list.add(duplicate);
      }
      lbset.add(duplicate);
      assertEquals(hashset.size(), lbset.size());
    }
//    System.out.println("Control Set:");
//    print(hashset);
//    System.out.println("Tested Set:");
//    print(lbset);
    assertEquals(hashset, lbset);
  }

  private final int nAddRemove = Integer.parseInt(System.getProperty("nAddRemove", "100000"));
  private final int nSort = Integer.parseInt(System.getProperty("nSort", "500000"));

  @Test
  @EnabledIfSystemProperty(named = "nAddRemove", matches = "[0-9]+")
  public void testSpeedOfAddRemove() {
    Set<String> treeset = new TreeSet<>((String s1, String s2) -> s1.compareTo(s2));
    ListBackedSet<String> lbset = new ListBackedSet<>((String s1, String s2) -> s1.compareTo(s2));
    int n = nAddRemove;
    int buckets = 10;
    int nPerBucket = n / buckets;
    long[] treesetNanos = new long[buckets];
    long[] lbsetNanos = new long[buckets];
    System.out.printf("Initializing %s items.\n", n);
    System.out.println("Items,TreeSet<String>.add() (ns),ListBackedSet<String>.add() (ns)");
    for (int b = 0; b < buckets; b++) {
      for (int i = 0; i < nPerBucket; i++) {
        String s = UUID.randomUUID().toString();
        long nanos = System.nanoTime();
        treeset.add(s);
        treesetNanos[b] += System.nanoTime() - nanos;
        nanos = System.nanoTime();
        lbset.add(s);
        lbsetNanos[b] += System.nanoTime() - nanos;
      }
      System.out.printf("%s,%s,%s\n", (b + 1) * nPerBucket, treesetNanos[b] / nPerBucket, lbsetNanos[b] / nPerBucket);
    }
    System.out.printf("Measuring time per one remove() operation.\n", n);
    System.out.println("Items,TreeSet<String>.remove() (ns),ListBackedSet<String>.remove() (ns)");
    for (int rb = 0; rb < buckets; rb++) {
      int b = buckets - rb - 1;
      for (int i = 0; i < nPerBucket; i++) {
        String s = lbset.getList().get(ThreadLocalRandom.current().nextInt(lbset.getList().size()));
        long nanos = System.nanoTime();
        treeset.remove(s);
        treesetNanos[b] += System.nanoTime() - nanos;
        nanos = System.nanoTime();
        lbset.remove(s);
        lbsetNanos[b] += System.nanoTime() - nanos;
      }
      System.out.printf("%s,%s,%s\n", (b + 1) * nPerBucket, treesetNanos[b] / nPerBucket, lbsetNanos[b] / nPerBucket);
    }
  }

  @Test
  @EnabledIfSystemProperty(named = "nSort", matches = "[0-9]+")
  public void testSpeedOfSort() {
    Set<String> treeset = new TreeSet<>((String s1, String s2) -> s1.compareTo(s2));
    ListBackedSet<String> lbset = new ListBackedSet<>((String s1, String s2) -> s1.compareTo(s2));
    int n = nSort;
    System.out.printf("Initializing ListBackedSet with %s items.\n", n);
    for (int i = 0; i < n; i++) {
      lbset.add(UUID.randomUUID().toString());
    }
    System.out.println("Items,Unsorted %,TreeSet<String>.addAll() per item (ns),ListBackedSet<String>.add() per 1 item (ns)");

    double[] unsortingFactors = new double[]{0.001, 0.002, 0.005, 0.01, 0.02, 0.05, 0.1, 0.2, 0.5, 0.7, 0.9, 0.99};

    for (double uf : unsortingFactors) {
      unsort(lbset, uf);

      treeset.clear();
      long treesetNanos = System.nanoTime();
      treeset.addAll(lbset.getList());
      treesetNanos = System.nanoTime() - treesetNanos;

      long lbsetNanos = System.nanoTime();
      lbset.sort();
      lbsetNanos = System.nanoTime() - lbsetNanos;
      System.out.printf("%s,%.1f,%s,%s\n", n, 100 * uf, treesetNanos / n, lbsetNanos / n);
    }

  }

  private void unsort(ListBackedSet<String> lbset, double unsortingFactor) {
    if (unsortingFactor < 0 || unsortingFactor > 1) {
      throw new IllegalArgumentException();
    }
    int n = lbset.size();
    int nUnsort = (int) (n * unsortingFactor);
    for (int i = 0; i < nUnsort; i++) {
      int a = ThreadLocalRandom.current().nextInt(n);
      int b = a;
      while (b == a) {
        b = ThreadLocalRandom.current().nextInt(n);
      }
      String tmpItem = lbset.getList().get(a);
      lbset.getList().set(a, lbset.getList().get(b));
      lbset.getList().set(b, tmpItem);
    }
  }

}
