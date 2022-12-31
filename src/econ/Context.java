package econ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Context {
  private ArrayList<Panel> panels = new ArrayList<>();
  private Map<String, Symbol> symbolTable = new HashMap<>();

  public ArrayList<Panel> getPanels() {
    return panels;
  }

  public Map<String, Symbol> getSymbolTable() {
    return symbolTable;
  }
  
  public Object get(String id) {
    if (symbolTable.get(id) == null) {
      return null;
    }
    return symbolTable.get(id).getValue();
  }
}
