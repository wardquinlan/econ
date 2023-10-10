package es.evaluator;

public class SimpleStatement implements Statement {
  private Object expr;

  public Object getExpr() {
    return expr;
  }

  public void setExpr(Object expr) {
    this.expr = expr;
  }
  
  @Override
  public void evaluate() {
    System.out.println(toString());
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
