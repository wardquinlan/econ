package es.parser;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration implements Statement {
  private String name;
  private List<String> params = new ArrayList<>();
  private List<Statement> statements = new ArrayList<>();
  
  public FunctionDeclaration(String name) {
    this.name = name;
  }
  
  @Override
  public void evaluate(SymbolTable symbolTable) {
    System.out.println("evaluating function");
  }
  
  public String getName() {
    return name;
  }

  public List<String> getParams() {
    return params;
  }

  public List<Statement> getStatements() {
    return statements;
  }

  @Override
  public String toString() {
    return "{FunctionDeclaration: }";
  }
}
