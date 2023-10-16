package es.parser;

import es.core.ESIterator;

public class Evaluator {
  private SymbolTable symbolTable;
  
  public Evaluator(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public Object evaluate(Statement statement, ESIterator<Statement> itr) throws Exception {
    while (true) {
      Object object = statement.evaluate(symbolTable);
      if (statement instanceof ReturnStatement) {
        return object;
      }
      if (!itr.hasNext()) {
        break;
      }
      statement = itr.next();
    }
    return null;
  }
}
