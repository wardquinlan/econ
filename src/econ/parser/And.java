package econ.parser;

import econ.core.TimeSeries;

public class And extends Operator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Boolean && val2 instanceof Boolean) {
      return (Boolean) val1 && (Boolean) val2;
    } else {
      throw new Exception("syntax error");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.TYPE_BOOLEAN;
  }
}