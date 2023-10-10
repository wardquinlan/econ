package es.evaluator;

public class ConstDeclaration implements Statement {
  private ESNode expr;
  
  @Override
  public void evaluate() {
    
  }
  
  @Override
  public String toString() {
    return "{ConstDeclaration: " + expr + "}";
  }
}
