package es.parser;

import es.core.TimeSeries;

public class Exp implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      return pow(((Integer) val1).floatValue(), ((Integer) val2).floatValue());
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      return pow(((Integer) val1).floatValue(), (Float) val2);
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return pow((Float) val1, ((Integer) val2).floatValue());
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return pow((Float) val1, (Float) val2);
    } else {
      throw new Exception("syntax error");
    }
  }

  private Float pow(Float val1, Float val2) {
    return ((Double) Math.pow((Float) val1, (Float) val2)).floatValue();
  }
  
  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
