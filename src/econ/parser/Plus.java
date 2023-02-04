package econ.parser;

public class Plus extends Operator {
  @Override
  public Float exec(Float val1, Float val2) throws Exception {
    return val1 + val2;
  }

  @Override
  public Object exec(Integer val1, Integer val2) throws Exception {
    return val1 + val2;
  }
  
  @Override
  public String exec(String val1, Object val2) throws Exception {
    return val1 + val2;
  }
}
