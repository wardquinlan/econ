package es.evaluator;

import es.core.TimeSeries;

public class UPlus implements UnaryOperator {
  @Override
  public Object exec(Object val1) throws Exception {
    if (val1 instanceof Integer || val1 instanceof Float) {
      return val1;
    } else {
      throw new Exception("syntax error");
    }
  }
  
  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
