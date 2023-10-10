package es.evaluator;

import es.parser.SymbolTable;

public class FunctionDeclaration implements Statement {
  @Override
  public void evaluate(SymbolTable symbolTable) {
  }
  
  @Override
  public String toString() {
    return "{FunctionDeclaration: }";
  }
}
