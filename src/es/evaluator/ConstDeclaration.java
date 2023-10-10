package es.evaluator;

import es.parser.SymbolTable;

public class ConstDeclaration implements Statement {
  private ESNode expr;
  
  @Override
  public void evaluate(SymbolTable symbolTable) {
    
  }
  
  @Override
  public String toString() {
    return "{ConstDeclaration: " + expr + "}";
  }
}
