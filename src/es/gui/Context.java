package es.gui;

import java.util.ArrayList;

import es.parser.SymbolTable;

public class Context extends GUIObject {
  private ArrayList<Panel> panels = new ArrayList<>();

  public Context(SymbolTable symbolTable) {
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
