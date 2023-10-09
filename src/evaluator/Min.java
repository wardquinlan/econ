package evaluator;

import es.core.TimeSeries;

public class Min implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      return Math.min((Integer) val1, (Integer) val2);
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      return Math.min(((Integer) val1).floatValue(), (Float) val2);
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return Math.min((Float) val1, ((Integer) val2).floatValue());
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return Math.min((Float) val1, (Float) val2);
    } else {
      throw new Exception("syntax error");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
