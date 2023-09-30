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
    if (symbol == null && parent != null) {
      return parent.get(symbolName);
    }
    return symbol;
  }
  
  public void globalPut(String symbolName, Symbol symbol) {
    SymbolTable symbolTable = this;
    while (symbolTable.getParent() != null) {
      symbolTable = symbolTable.getParent();
    }
    symbolTable.put(symbolName, symbol);
  }
  
  public void localPut(String symbolName, Symbol symbol) {
    map.put(symbolName, symbol);
  }
  
  public void put(String symbolName, Symbol symbol) {
    if (map.get(symbolName) != null) {
      // if already in local symbol table
      map.put(symbolName, symbol);
    } else if (parent != null && parent.get(symbolName) != null) {
      // if already in parent symbol table
      parent.put(symbolName, symbol);
    } else {
      // if in neither
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
