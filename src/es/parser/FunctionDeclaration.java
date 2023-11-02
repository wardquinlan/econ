package es.parser;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import es.core.Utils;

public class FunctionDeclaration extends Statement {
  private String name;
  private List<String> params = new ArrayList<>();
  private List<Statement> statements = new ArrayList<>();
  private boolean constant = false;
  
  public FunctionDeclaration(String name) throws Exception {
    Utils.validateRootNameSpaceWrite(name);
    Utils.validateSystemFunctionWrite(name);
    this.name = name;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    Symbol symbol = new Symbol(name, this, isConstant());
    Utils.symbolConstCheck(symbolTable, symbol);
    Utils.functionReferenceCheck(symbolTable, symbol);
    symbolTable.put(name, symbol);
    return null;
  }
  
  public String getName() {
    return name;
  }

  public boolean isConstant() {
    return constant;
  }

  public void setConstant(boolean constant) {
    this.constant = constant;
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
    if (constant) {
      sb.append("const ");
    }
    sb.append(name);
    sb.append("(");
    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i));
      if (i != params.size() - 1) {
        sb.append(", ");
      }
    }

    Formatter formatter = new Formatter();
    try {
      sb.append(") [0x" + formatter.format("%x", hashCode()).toString() + "]");
    } finally {
      formatter.close();
    }
    
    return sb.toString();
  }
}
