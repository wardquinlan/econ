package es.evaluator;

import es.core.TimeSeries;

public class Div implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      if (((Integer) val2) == 0) {
        throw new Exception("divide by zero");
      }
      if ((Integer) val1 % (Integer) val2 == 0) {
        return (Integer) val1 / (Integer) val2;
      }
      return ((Integer) val1).floatValue() / ((Integer) val2).floatValue();
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      if ((Float) val2 == 0f) {
        throw new Exception("divide by zero");
      }
      return ((Integer) val1).floatValue() / (Float) val2;
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      if (((Integer) val2) == 0) {
        throw new Exception("divide by zero");
      }
      return ((Float) val1 / ((Integer) val2).floatValue());
    } else if (val1 instanceof Float && val2 instanceof Float) {
      if ((Float) val2 == 0f) {
        throw new Exception("divide by zero");
      }
      return (Float) val1 / (Float) val2;
    } else {
      throw new Exception("syntax error");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
