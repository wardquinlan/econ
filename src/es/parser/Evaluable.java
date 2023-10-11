package es.parser;

public interface Evaluable {
  public Object evaluate(SymbolTable symbolTable) throws Exception;
}
