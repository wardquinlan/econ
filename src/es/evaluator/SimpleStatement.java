package es.evaluator;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.SymbolTable;

public class SimpleStatement implements Statement {
  private FunctionCaller functionCaller = new FunctionCaller();
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
    if (expr instanceof ESNode) {
      ESNode node = (ESNode) expr;
      Object result = node.evaluate();
      System.out.println("Result: " + result);
    } else if (expr instanceof FunctionCall) {
      FunctionCall functionCall = (FunctionCall) expr;
      System.out.println("Result: " + expr);
      Utils.ASSERT(functionCall.getFile() != null, "file is null");
      Object result = functionCaller.invokeFunction(functionCall.getName(), symbolTable, functionCall.getFile(), functionCall.getParams());
      System.out.println("Result: " + result);
    }
  }
  
  @Override
  public String toString() {
    return "{SimpleStatement: " + expr + "}";
  }
}
