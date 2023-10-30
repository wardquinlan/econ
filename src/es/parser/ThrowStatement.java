package es.parser;

public class ThrowStatement extends Statement {
  private Object expr;

  public ThrowStatement(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    if (expr instanceof Evaluable) {
      Evaluable evaluable = (Evaluable) expr;
      Object result = evaluable.evaluate(symbolTable);
      if (!(result instanceof String)) {
        throw new Exception("invalid: can only throw String exceptions");
      }
      throw new Exception((String) result);
    }
    if (!(expr instanceof String)) {
      throw new Exception("invalid: can only throw String exceptions");
    }
    throw new Exception((String) expr);
  }
  
  @Override
  public String toString() {
    return "{ThrowStatement: " + expr + "}";
  }
}
