package es.evaluator;

import java.util.HashMap;
import java.util.Map;

import es.core.Utils;
import es.parser.Token;

public class ESNode {
  public static final int PLUS     = 18; // +
  public static final int MINUS    = 19; // - 
  public static final int AND      = 26; // and
  public static final int OR       = 27; // or

  public static Map<Integer, String> map = new HashMap<Integer, String>();
  static {
    map.put(PLUS,    "PLUS");
    map.put(MINUS,   "MINUS");
    map.put(AND,     "AND");
    map.put(OR,      "OR");
  }
  
  private static final Map<Integer, Operator> operatorMap = new HashMap<>();
  static {
    operatorMap.put(PLUS, new Plus());
    operatorMap.put(MINUS, new Minus());
    operatorMap.put(AND, new And());
    operatorMap.put(OR, new Or());
  }
  
  private Object lhs;
  private Object rhs;
  private int type;
  
  public ESNode(int type) {
    this.type = type;
  }
  
  public Object getLhs() {
    return lhs;
  }
  
  public void setLhs(Object lhs) {
    this.lhs = lhs;
  }
  
  public Object getRhs() {
    return rhs;
  }

  public void setRhs(Object rhs) {
    this.rhs = rhs;
  }
  
  public int getType() {
    return type;
  }
  
  public Object evaluate() throws Exception {
    Operator operator = operatorMap.get(type);
    Utils.ASSERT(operator != null, "operator not found");
    Utils.ASSERT(rhs != null, "rhs is null");
    Executor executor = new Executor(operator);
    if (lhs == null) {
      Utils.ASSERT(operator instanceof UnaryOperator, "operator is not unary");
      return executor.exec(rhs);
    }  else {
      Utils.ASSERT(operator instanceof BinaryOperator, "operator is not binary");
      return executor.exec(lhs, rhs);
    }
  }
  
  @Override
  public String toString() {
    return "{" + map.get(type) + ": " + lhs + ", " + rhs + "}"; 
  }
}
