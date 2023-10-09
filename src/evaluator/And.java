package evaluator;

import es.core.TimeSeries;

public class And implements BinaryOperator {
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
    return TimeSeries.BOOLEAN;
  }
}
