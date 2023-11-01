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
      return parent.get(symbolName);
    }
    return symbol;
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
    Symbol tmp = symbolTable.get(symbolName);
    if (tmp != null && tmp.isConstant()) {
      throw new Exception("symbol is already defined as a const: " + symbolName);
    }
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

  public SymbolTable getParent() {
    return parent;
  }
}
