package evaluator;

public interface BinaryOperator extends Operator {
  public abstract Object exec(Object val1, Object val2) throws Exception;
}
