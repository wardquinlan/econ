package es.parser;

import es.core.ESIterator;

public class Evaluator {
  private SymbolTable symbolTable;
  
  public Evaluator(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public void evaluate(ESIterator<Statement> itr) throws Exception {
    while (itr.hasNext()) {
      Statement statement = itr.next();
      statement.evaluate(symbolTable);
    }
  }
}
