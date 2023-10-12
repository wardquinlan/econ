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
  public void evaluate(SymbolTable symbolTable) throws Exception {
    Symbol symbolT = symbolTable.get(name);
    if (symbolT != null) {
      throw new Exception("cannot overwrite symbol with a function: " + name);
    }
    Symbol symbolNew = new Symbol(name, this, true);
    symbolTable.put(name, symbolNew);
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
