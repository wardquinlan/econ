package es.parser;

import java.util.List;

public class IFStatement extends Statement {
  private ESNode predicate;
  private List<Statement> ifBody;
  private List<Statement> elseBody;
  
  @Override
  public Object evaluate(SymbolTable symbolTable) {
    return null;
    
  }
  
  @Override
  public String toString() {
    return "{IFStatement: " + predicate + "}";
  }
}
