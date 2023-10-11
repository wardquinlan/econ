package es.evaluator;

import es.parser.SymbolTable;

public interface Evaluable {
  public Object evaluate(SymbolTable symbolTable) throws Exception;
}
