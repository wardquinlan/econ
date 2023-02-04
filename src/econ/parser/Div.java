package econ.parser;

public class Div extends Operator {
  @Override
  public Float exec(Float val1, Float val2) throws Exception {
    if (val2 == 0f) {
      throw new Exception("divide by 0");
    }
    return val1 / val2;
  }
  
  @Override
  public Object exec(Integer val1, Integer val2) throws Exception {
    if (val2 == 0) {
      throw new Exception("divide by 0");
    }
    if (val1 % val2 == 0) {
      return val1 / val2;
    }
    return val1.floatValue() / val2.floatValue();
  }
}
