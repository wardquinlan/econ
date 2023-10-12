package es.parser;

import es.core.ESIterator;

public class Evaluator {
  private SymbolTable symbolTable;
  
  public Evaluator(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public void evaluate(Statement statement, ESIterator<Statement> itr) throws Exception {
    while (true) {
      statement.evaluate(symbolTable);
      if (!itr.hasNext()) {
        break;
      }
      statement = itr.next();
    }
  }
}
