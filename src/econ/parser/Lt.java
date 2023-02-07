package econ.parser;

import econ.core.TimeSeries;

public class Lt implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      return ((Integer) val1).compareTo((Integer) val2) < 0;
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      val1 = ((Integer) val1).floatValue();
      return ((Float) val1).compareTo((Float) val2) < 0;
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return ((Float) val1).compareTo(((Integer) val2).floatValue()) < 0;
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return ((Float) val1).compareTo((Float) val2) < 0;
    } else if (val1 instanceof String && val2 instanceof String) {
      return ((String) val1).compareTo((String) val2) < 0;
    } else {
      throw new Exception("syntax error");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.TYPE_BOOLEAN;
  }
}
