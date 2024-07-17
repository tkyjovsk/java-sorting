package tkyjovsk.sorting.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListBackedSet<T extends Comparable<T>> implements Set<T> {

  private final List<T> list = new ArrayList<>();

  private final Comparator<T> comparator;

  public ListBackedSet(Comparator<T> comparator) {
    this.comparator = comparator;
  }

  public List<T> getList() {
    return list;
  }

  public void sort() {
    Collections.sort(list, comparator);
  }

  @Override
  public boolean add(T item) {
    boolean added = false;
    int index = Collections.binarySearch(list, item, comparator);
    if (index < 0) {
      list.add(-index - 1, item);
    }
    return added;
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    boolean contains = false;
    try {
      contains = Collections.binarySearch(list, (T) o, comparator) >= 0;
    } catch (ClassCastException cce) {
    }
    return contains;
  }

  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return list.addAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

}
