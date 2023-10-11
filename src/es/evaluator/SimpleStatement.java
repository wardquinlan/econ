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
    System.out.println(toString());
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      Object result = evaluable.evaluate(symbolTable);
      System.out.println("Result: " + result);
    }
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
