package econ.parser;

public class Plus implements Operator {
  @Override
  public Float exec(Float val1, Float val2) {
    return val1 + val2;
  }
}
