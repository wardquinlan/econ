package es.evaluator;

import es.parser.SymbolTable;

public interface Statement {
  public void evaluate(SymbolTable symbolTable) throws Exception;
}
