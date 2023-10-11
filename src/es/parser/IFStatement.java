package es.parser;

import java.util.List;

public class IFStatement implements Statement {
  private ESNode predicate;
  private List<Statement> ifBody;
  private List<Statement> elseBody;
  
  @Override
  public void evaluate(SymbolTable symbolTable) {
    
  }
  
  @Override
  public String toString() {
    return "{IFStatement: " + predicate + "}";
  }
}
