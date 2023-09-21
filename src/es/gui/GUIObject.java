package es.gui;

import es.parser.SymbolTable;

public abstract class GUIObject {
  protected SymbolTable symbolTable;
  
  public GUIObject(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
}
