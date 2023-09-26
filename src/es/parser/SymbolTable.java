package es.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.core.Utils;

public class SymbolTable {
  private SymbolTable parent;
  private Map<String, Symbol> map = new HashMap<String, Symbol>();
  
  public SymbolTable() {
    this.parent = null;
  }
  
  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }
  
  public Symbol get(String symbolName) {
    Symbol symbol = map.get(symbolName);
    if (parent != null) {
      if (symbol == null) {
        symbol = parent.get(symbolName);
      } else {
        Utils.ASSERT(parent.get(symbolName) == null, "parent symbol is not null: " + symbolName);
      }
    }
    return symbol;
  }
  
  public void put(String symbolName, Symbol symbol) {
    if (parent != null && parent.get(symbolName) != null) {
      parent.put(symbolName, symbol);
    } else {
      map.put(symbolName, symbol);
    }
  }
  
  public Set<String> keySet() {
    Set<String> set = new HashSet<String>();
    set.addAll(map.keySet());
    if (parent == null) {
      return set;
    }
    for (String key: parent.keySet()) {
      Utils.ASSERT(!set.contains(key), "parent key is not null: " + key);
      set.add(key);
    }
    return set;
  }

  public SymbolTable getParent() {
    return parent;
  }
}
