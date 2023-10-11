package es.evaluator;

import es.parser.SymbolTable;

public class SimpleStatement implements Statement {
  private Object expr;

  public Object getExpr() {
    return expr;
  }

  public void setExpr(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public void evaluate(SymbolTable symbolTable) throws Exception {
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      evaluable.evaluate(symbolTable);
    }
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
