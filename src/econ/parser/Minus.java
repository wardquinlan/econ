package econ.parser;

public class Minus implements Operator {
  @Override
  public Float exec(Float val1, Float val2) {
    return val1 - val2;
  }
}
