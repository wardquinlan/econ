package es.parser;

import es.core.Utils;

public class SimpleStatement extends Statement {
  private Object expr;

  public SimpleStatement() {
  }
  
  public SimpleStatement(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      return evaluable.evaluate(symbolTable);
    }
    Utils.ASSERT(expr != null, "expr cannot be null");
    return expr;
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
