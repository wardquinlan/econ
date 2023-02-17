package es.parser;

import es.core.TimeSeries;

public class UMinus implements UnaryOperator {
  @Override
  public Object exec(Object val1) throws Exception {
    if (val1 instanceof Integer) {
      return -((Integer) val1);
    } else if (val1 instanceof Float) {
      return -((Float) val1);
    } else {
      throw new Exception("syntax error");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
