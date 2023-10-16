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
  public Object evaluate(SymbolTable symbolTable) {
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
