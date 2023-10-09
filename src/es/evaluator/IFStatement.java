package es.evaluator;

import java.util.List;

public class IFStatement extends Statement {
  private ESNode predicate;
  private List<Statement> ifBody;
  private List<Statement> elseBody;
}
