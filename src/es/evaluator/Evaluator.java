package es.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.core.ESIterator;
import es.parser.SymbolTable;

public class Evaluator {
  private static final Log log = LogFactory.getFactory().getInstance(Evaluator.class);
  private SymbolTable symbolTable;
  
  public Evaluator(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public void evaluate(ESIterator<Statement> itr) throws Exception {
    while (itr.hasNext()) {
      Statement statement = itr.next();
      statement.evaluate(symbolTable);
    }
  }
}
