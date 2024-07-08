package tkyjovsk.sorting;

import java.util.Objects;

public class SortingKey {

  final String key;
  final boolean ascending;

  public SortingKey(String key, boolean ascending) {
    this.key = key;
    this.ascending = ascending;
  }

  public SortingKey(String key) {
    this(key, true);
  }

  public String getKey() {
    return key;
  }

  public boolean isAscending() {
    return ascending;
  }

  public boolean isDescending() {
    return !isAscending();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + Objects.hashCode(this.key);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SortingKey other = (SortingKey) obj;
    return Objects.equals(this.key, other.key);
  }

  @Override
  public String toString() {
    return String.format("by %s %s", getKey(), isAscending() ? "⯅" : "⯆");
  }

}
