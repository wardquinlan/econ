package es.parser;

public abstract class Statement implements Evaluable{
  @Override
  public abstract Object evaluate(SymbolTable symbolTable) throws Exception;
}
