package es.parser;

import java.util.Date;

import es.core.TimeSeries;
import es.core.Utils;

public class Plus implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      return (Integer) val1 + (Integer) val2;
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      return ((Integer) val1).floatValue() + (Float) val2;
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return ((Float) val1 + ((Integer) val2).floatValue());
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return (Float) val1 + (Float) val2;
    } else if (val1 instanceof Date && val2 instanceof Integer) {
      long l = ((Date) val1).getTime() + (int) val2 * Utils.DATE_CONVERSION;
      return new Date(l);
    } else if (val1 instanceof Integer && val2 instanceof Date) {
      long l = ((Date) val2).getTime() + (int) val1 * Utils.DATE_CONVERSION;
      return new Date(l);
    } else if (val1 instanceof String) {
      return (String) val1 + val2;
    } else {
      throw new Exception("syntax error: Plus");
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.FLOAT;
  }
}
