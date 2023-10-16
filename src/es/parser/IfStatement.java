package es.parser;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement {
  private Object predicate;
  private List<Statement> ifBody = new ArrayList<>();
  private List<Statement> elseBody = new ArrayList<>();
  
  public IfStatement(Object predicate) {
    this.predicate = predicate;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    Boolean result;
    if (predicate instanceof Boolean) {
      result = (Boolean) predicate;
    } else if (predicate instanceof Evaluable) {
      Object tmp = ((Evaluable) predicate).evaluate(symbolTable);
      if (!(tmp instanceof Boolean)) {
        throw new Exception("predicate does not evaluate to a boolean expression");
      }
      result = (Boolean) tmp;
    } else {
      throw new Exception("predicate does not evaluate to a boolean expression");
    }
    if (result) {
      for (Statement statement: ifBody) {
        statement.evaluate(symbolTable);
      }
    } else {
      for (Statement statement: elseBody) {
        statement.evaluate(symbolTable);
      }
    }
    return null;
  }
  
  public Object getPredicate() {
    return predicate;
  }

  public List<Statement> getIfBody() {
    return ifBody;
  }

  public List<Statement> getElseBody() {
    return elseBody;
  }

  @Override
  public String toString() {
    return "{IFStatement: " + predicate + "}";
  }
}
