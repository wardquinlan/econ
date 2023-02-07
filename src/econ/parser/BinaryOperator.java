package econ.parser;

public interface BinaryOperator {
  public abstract Object exec(Object val1, Object val2) throws Exception;
  public abstract int getAssociatedSeriesType();
}
