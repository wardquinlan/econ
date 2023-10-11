package es.parser;

public interface Statement {
  public void evaluate(SymbolTable symbolTable) throws Exception;
}
