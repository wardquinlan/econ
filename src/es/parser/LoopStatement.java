package es.parser;

import java.util.ArrayList;
import java.util.List;

public class LoopStatement extends Statement {
  private Object pre;
  private Object post;
  private Object predicate;
  private List<Statement> body = new ArrayList<>();

  public LoopStatement(Object predicate) {
    this.predicate = predicate;
  }

  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    whileloop:
    while (evaluatePredicate(symbolTable)) {
      forloop:
      for (Statement statement: body) {
        if (statement instanceof BreakStatement) {
          break whileloop;
        } else if (statement instanceof ContinueStatement) {
          break forloop;
        } else {
          Object result2 = statement.evaluate(symbolTable);
          if (result2 instanceof Return) {
            return result2;
          }
          if (result2 instanceof Break) {
            break whileloop;
          }
          if (result2 instanceof Continue) {
            break forloop;
          }
        }
      }
    }
    return null;
  }

  public boolean evaluatePredicate(SymbolTable symbolTable) throws Exception {
    if (predicate instanceof Boolean) {
      return (Boolean) predicate;
    } else if (predicate instanceof Evaluable) {
      Object tmp = ((Evaluable) predicate).evaluate(symbolTable);
      if (!(tmp instanceof Boolean)) {
        throw new Exception("predicate does not evaluate to a boolean expression");
      }
      return (Boolean) tmp;
    } else {
      throw new Exception("predicate does not evaluate to a boolean expression");
    }
  }
  
  public List<Statement> getBody() {
    return body;
  }
}
