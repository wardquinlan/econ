package es.parser;

public class BreakStatement extends Statement {
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    throw new Exception("unexpected break statement");
  }
}
