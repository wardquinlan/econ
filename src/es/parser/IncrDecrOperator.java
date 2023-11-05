package es.parser;

import es.core.TimeSeries;

public abstract class IncrDecrOperator implements UnaryOperator {
  protected SymbolTable symbolTable;
  
  public IncrDecrOperator(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.NULL;
  }
  
  public abstract Object exec(Object val1) throws Exception;
}
