package econ.parser;

import econ.core.TimeSeries;

public class UNot implements UnaryOperator {
  @Override
  public Object exec(Object val1) throws Exception {
    if (val1 instanceof Boolean) {
      return !((Boolean) val1);
    } else {
      throw new Exception("syntax error");
    }
  }
  
  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.TYPE_BOOLEAN;
  }
}
