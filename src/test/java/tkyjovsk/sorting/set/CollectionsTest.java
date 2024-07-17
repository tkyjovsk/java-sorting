package tkyjovsk.sorting.set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;

public class CollectionsTest {

  private void print(List l) {
    l.forEach(i -> System.out.println(i));
  }

  @Test
  public void test() {
    List<String> list = new ArrayList<>();
    int n = 10;
    int r = ThreadLocalRandom.current().nextInt(1, n);
    for (int i = 0; i < n; i++) {
      list.add(i == r ? list.getLast() : UUID.randomUUID().toString());
    }
    print(list);

    System.out.println("SORTING");
    Collections.sort(list);
    print(list);
  }

}
