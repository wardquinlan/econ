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
  public void evaluate() throws Exception {
    System.out.println(toString());
    if (expr instanceof ESNode) {
      ESNode node = (ESNode) expr;
      Object result = node.evaluate();
      System.out.println("Result: " + result);
    }
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
