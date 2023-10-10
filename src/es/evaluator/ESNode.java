package es.evaluator;

import java.util.HashMap;
import java.util.Map;

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
  
  @Override
  public String toString() {
    return "{" + map.get(type) + ": " + lhs + ", " + rhs + "}"; 
  }
}
