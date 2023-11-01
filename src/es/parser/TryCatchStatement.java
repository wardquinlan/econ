package es.parser;

import java.util.ArrayList;
import java.util.List;

import es.core.ESIterator;

public class TryCatchStatement extends Statement {
  private String exceptionName;
  private List<Statement> tryBody = new ArrayList<>();
  private List<Statement> catchBody = new ArrayList<>();
  
  public TryCatchStatement(String exceptionName) {
    this.exceptionName = exceptionName;
  }

  public List<Statement> getTryBody() {
    return tryBody;
  }

  public List<Statement> getCatchBody() {
    return catchBody;
  }

  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    try {
      for (Statement statement: tryBody) {
        Object result2 = statement.evaluate(symbolTable);
        if (result2 instanceof Return) {
          return result2;
        }
      }
    } catch(Exception ex) {
      SymbolTable childSymbolTable = new SymbolTable(symbolTable);
      childSymbolTable.put(exceptionName, new Symbol(exceptionName, ex.getMessage()));
      ESIterator<Statement> itr = new ESIterator<>(catchBody);
      if (itr.hasNext()) {
        Evaluator e = new Evaluator(childSymbolTable);
        Statement statement = itr.next();
        return e.evaluate(statement, itr);
      }
    }
    return null;
  }
  
  @Override
  public String toString() {
    return "{TryCatch: " + exceptionName + "}";
  }
}
