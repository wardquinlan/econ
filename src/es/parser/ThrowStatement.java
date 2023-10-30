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
      if (result == null) {
        throw new Exception("cannot throw a null result");
      }
      throw new ESException(result);
    }
    if (expr == null) {
      throw new Exception("cannot throw a null result");
    }
    throw new ESException(expr);
  }
  
  @Override
  public String toString() {
    return "{ThrowStatement: " + expr + "}";
  }
}
