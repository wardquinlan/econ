package es.parser;

import java.util.Date;

import es.core.TimeSeries;
import es.core.Utils;

public class Ne implements BinaryOperator {
  @Override
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof Integer && val2 instanceof Integer) {
      return ((Integer) val1).compareTo((Integer) val2) != 0;
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      val1 = ((Integer) val1).floatValue();
      return ((Float) val1).compareTo((Float) val2) != 0;
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return ((Float) val1).compareTo(((Integer) val2).floatValue()) != 0;
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return ((Float) val1).compareTo((Float) val2) != 0;
    } else if (val1 instanceof String && val2 instanceof String) {
      return ((String) val1).compareTo((String) val2) != 0;
    } else if (val1 instanceof Boolean && val2 instanceof Boolean) {
      return !val1.equals(val2);
    } else if (val1 instanceof Date && val2 instanceof Date) {
      return !val1.equals(val2);
    } else if (val1 instanceof Date && val2 instanceof String) {
      Date date = Utils.DATE_FORMAT.parse((String) val2);
      return !val1.equals(date);
    } else if (val1 instanceof String && val2 instanceof Date) {
      Date date = Utils.DATE_FORMAT.parse((String) val1);
      return !date.equals(val2);
    } else if (val1 == null && val2 == null) {
      return false;
    } else {
      // throw new Exception("syntax error");
      return true;
    }
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.BOOLEAN;
  }
}
