package econ.parser;

public abstract class Operator {
  public abstract Float exec(Float val1, Float val2) throws Exception;
  public abstract Object exec(Integer val1, Integer val2) throws Exception;
  public String exec(String val1, Object val2) throws Exception {
    throw new Exception("syntax error");
  }
}
