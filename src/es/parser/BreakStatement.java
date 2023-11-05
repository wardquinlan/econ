package es.parser;

public class BreakStatement extends Statement {
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    return new Break();
  }
}
