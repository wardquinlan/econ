package es.parser;

public class ContinueStatement extends Statement {
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    return new Continue();
  }
}
