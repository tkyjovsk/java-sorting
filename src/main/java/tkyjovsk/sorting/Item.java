package tkyjovsk.sorting;

import java.util.HashMap;

public class Item extends HashMap<String, String> {

  public static final String NAME = "name";
  public static final String AGE = "age";
  public static final String RANK = "rank";

  public Item set(String key, String value) {
    put(key, value);
    return this;
  }

  public String getName() {
    return get(NAME);
  }

}
