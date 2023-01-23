package econ;

import java.util.Map;

import econ.parser.Symbol;

public abstract class GUIObject {
  protected Map<String, Symbol> symbolTable;
  
  public GUIObject(Map<String, Symbol> symbolTable) {
    this.symbolTable = symbolTable;
  }
}
