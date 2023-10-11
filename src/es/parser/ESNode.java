package es.parser;

import java.util.HashMap;
import java.util.Map;

import es.core.Utils;

public class ESNode implements Evaluable {
  public static final int PLUS     = 0; // +
  public static final int MINUS    = 1; // - 
  public static final int AND      = 2; // and
  public static final int OR       = 3; // or
  public static final int UPLUS    = 5; // +
  public static final int UMINUS   = 6; // -
  public static final int UNOT     = 7; // !
  public static final int ASSIGN   = 8; // =

  public static Map<Integer, String> map = new HashMap<Integer, String>();
  static {
    map.put(PLUS,    "PLUS");
    map.put(MINUS,   "MINUS");
    map.put(AND,     "AND");
    map.put(OR,      "OR");
    map.put(UPLUS,   "UPLUS");
    map.put(UMINUS,  "UMINUS");
    map.put(UNOT,    "UNOT");
    map.put(ASSIGN,  "ASSIGN");
  }
  
  private static final Map<Integer, Operator> operatorMap = new HashMap<>();
  static {
    operatorMap.put(PLUS, new Plus());
    operatorMap.put(MINUS, new Minus());
    operatorMap.put(AND, new And());
    operatorMap.put(OR, new Or());
    operatorMap.put(UPLUS, new UPlus());
    operatorMap.put(UMINUS, new UMinus());
    operatorMap.put(UNOT, new UNot());
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
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    if (type == ASSIGN) {
      return evaluateAssignment(symbolTable);
    }
    Operator operator = operatorMap.get(type);
    Utils.ASSERT(operator != null, "operator not found");
    Utils.ASSERT(rhs != null, "rhs is null");
    Executor executor = new Executor(operator);
    if (lhs == null) {
      Utils.ASSERT(operator instanceof UnaryOperator, "operator is not unary");
      Object val2 = (rhs instanceof Evaluable ? ((Evaluable) rhs).evaluate(symbolTable) : rhs);
      return executor.exec(val2);
    }  else {
      Utils.ASSERT(operator instanceof BinaryOperator, "operator is not binary");
      Object val1 = (lhs instanceof Evaluable ? ((Evaluable) lhs).evaluate(symbolTable) : lhs);
      Object val2 = (rhs instanceof Evaluable ? ((Evaluable) rhs).evaluate(symbolTable) : rhs);
      return executor.exec(val1, val2);
    }
  }
  
  private Object evaluateAssignment(SymbolTable symbolTable) throws Exception {
    Utils.ASSERT(lhs != null, "lhs is null");
    Utils.ASSERT(rhs != null, "rhs is null");
    Utils.ASSERT(lhs instanceof Symbol, "lhs is not a Symbol");
    Symbol symbol = (Symbol) lhs;
    Object val = (rhs instanceof Evaluable ? ((Evaluable) rhs).evaluate(symbolTable) : rhs);
    symbol.setValue(val);
    symbolTable.put(symbol.getName(), symbol);
    return val;
  }
  
  @Override
  public String toString() {
    return "{" + map.get(type) + ": " + lhs + ", " + rhs + "}"; 
  }
}
