package es.parser;

public class ContinueStatement extends Statement {
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    throw new Exception("unexpected continue statement");
  }
}
