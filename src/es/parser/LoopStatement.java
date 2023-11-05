package es.parser;

import java.util.ArrayList;
import java.util.List;

public class LoopStatement extends Statement {
  private Object pre;
  private Object post;
  private Object predicate;
  private List<Statement> body = new ArrayList<>();
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    return null;
  }
}
