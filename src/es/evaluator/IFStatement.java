package es.evaluator;

import java.util.List;

import es.parser.SymbolTable;

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
