package econ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import econ.parser.Symbol;

public class Context extends GUIObject {
  private ArrayList<Panel> panels = new ArrayList<>();

  public Context(Map<String, Symbol> symbolTable) {
    super(symbolTable);
  }
  
  public ArrayList<Panel> getPanels() {
    return panels;
  }

  public Object get(String id) {
    if (symbolTable.get(id) == null) {
      return null;
    }
    return symbolTable.get(id).getValue();
  }
}
