package es.parser;

public interface UnaryOperator extends Operator {
  public abstract Object exec(Object val1) throws Exception;
}
