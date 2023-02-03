package econ.parser;

import econ.core.TimeSeries;

public interface Operator {
  public Float exec(Float val1, Float val2);
}
