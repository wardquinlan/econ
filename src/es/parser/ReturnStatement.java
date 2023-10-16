package es.parser;

public class ReturnStatement extends Statement {
  private Object expr;

  public void setExpr(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      return evaluable.evaluate(symbolTable);
    }
    return expr;
  }
  
  @Override
  public String toString() {
    return "{ReturnStatement: " + expr + "}";
  }
}
