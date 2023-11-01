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
  
  public Symbol localGet(String symbolName) {
    return map.get(symbolName);
  }

  public void put(String symbolName, Symbol symbol) {
    map.put(symbolName, symbol);
  }
  
  public Symbol globalGet(String symbolName) {
    SymbolTable symbolTable = this;
    while (symbolTable.getParent() != null) {
      symbolTable = symbolTable.getParent();
    }
    return symbolTable.get(symbolName);
  }
  
  public void globalPut(String symbolName, Symbol symbol) throws Exception {
    SymbolTable symbolTable = this;
    while (symbolTable.getParent() != null) {
      symbolTable = symbolTable.getParent();
    }
    Utils.symbolConstCheck(symbolTable, symbol);
    symbolTable.put(symbolName, symbol);
  }

  public Set<String> keySet() {
    Set<String> set = new HashSet<String>();
    if (parent != null) {
      set.addAll(parent.keySet());
    }
    set.addAll(map.keySet());
    return set;
  }

  public Set<String> localKeySet() {
    return map.keySet();
  }
  
  public SymbolTable getParent() {
    return parent;
  }
}
