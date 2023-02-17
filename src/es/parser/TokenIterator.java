package es.parser;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TokenIterator implements Iterator<Token> {
  private static Log log = LogFactory.getFactory().getInstance(TokenIterator.class);
  private List<Token> list;
  private int idx;
  
  public TokenIterator(List<Token> list) {
    this.list = list;
    idx = -1;
  }
  
  public boolean hasNext() {
    return (idx != (list.size() - 1));
  }
  
  public Token peek() {
    return list.get(idx + 1);
  }
  
  public Token next() {
    return list.get(++idx);
  }
  
  public void remove() {
    log.fatal("remove is not supported");
    System.exit(0);
  }
}
