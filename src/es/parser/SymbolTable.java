package es.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    if (symbol == null && parent != null) {
      symbol = parent.get(symbolName);
    }
    return symbol;
  }
  
  public void put(String symbolName, Symbol symbol) {
    // NOTE: could also put into the parent
    map.put(symbolName, symbol);
  }
  
  public Set<String> keySet() {
    Set<String> set = new HashSet<String>();
    set.addAll(map.keySet());
    if (parent == null) {
      return set;
    }
    // current scope hides parent scope
    for (String key: parent.keySet()) {
      if (!set.contains(key)) {
        set.add(key);
      }
    }
    return set;
  }

  public SymbolTable getParent() {
    return parent;
  }
}
