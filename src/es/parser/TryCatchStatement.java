package es.parser;

import java.util.ArrayList;
import java.util.List;

public class TryCatchStatement extends Statement {
  private String ex;
  private List<Statement> tryBody = new ArrayList<>();
  private List<Statement> catchBody = new ArrayList<>();
  
  public TryCatchStatement(String ex) {
    this.ex = ex;
  }

  public List<Statement> getTryBody() {
    return tryBody;
  }

  public List<Statement> getCatchBody() {
    return catchBody;
  }

  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    return null;
  }
  
  @Override
  public String toString() {
    return "{TryCatch: " + ex + "}";
  }
}
