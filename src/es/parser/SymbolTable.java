package es.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
  private SymbolTable parent = null;
  private Map<String, Symbol> map = new HashMap<String, Symbol>();
  
  public Symbol get(String symbolName) {
    return map.get(symbolName);
  }
  
  public void put(String symbolName, Symbol symbol) {
    map.put(symbolName, symbol);
  }
  
  public Set<String> keySet() {
    return map.keySet();
  }
}