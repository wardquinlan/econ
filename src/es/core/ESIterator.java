package es.core;

import java.util.Iterator;
import java.util.List;

public class ESIterator<T> implements Iterator<T> {
  private List<T> list;
  private int idx;
  
  public ESIterator(List<T> list) {
    this.list = list;
    idx = -1;
  }
  
  public boolean hasNext() {
    return (idx != (list.size() - 1));
  }
  
  public T peek() {
    return list.get(idx + 1);
  }
  
  public T next() {
    return list.get(++idx);
  }
  
  public void remove() {
    Utils.ASSERT(false, "remove is not supported");
    System.exit(1);
  }
}
