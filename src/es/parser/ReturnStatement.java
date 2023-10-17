package es.parser;

public class ReturnStatement extends Statement {
  private Object expr;

  public ReturnStatement() {
  }
  
  public ReturnStatement(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      return new Return(evaluable.evaluate(symbolTable));
    }
    return new Return(expr);
  }
  
  @Override
  public String toString() {
    return "{ReturnStatement: " + expr + "}";
  }
}
