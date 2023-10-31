package es.parser;

import java.util.ArrayList;
import java.util.List;

import es.core.Utils;

public class FunctionDeclaration extends Statement {
  private String name;
  private List<String> params = new ArrayList<>();
  private List<Statement> statements = new ArrayList<>();
  
  public FunctionDeclaration(String name) throws Exception {
    Utils.validateRootNameSpaceWrite(name);
    this.name = name;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    Symbol symbolNew = new Symbol(name, this);
    symbolTable.put(name, symbolNew);
    return null;
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
    StringBuffer sb = new StringBuffer();
    sb.append(name);
    sb.append("(");
    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i));
      if (i != params.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append(");");
    return sb.toString();
  }
}
