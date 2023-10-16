package es.parser;

import java.util.HashMap;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
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
  public static final int LT       = 9; // <
  public static final int GT       = 10; // >
  public static final int LTE      = 11; // <=
  public static final int GTE      = 12; // >=
  public static final int EQ       = 13; // ==
  public static final int NE       = 14; // !=

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
    map.put(LT,      "LT");
    map.put(GT,      "GT");
    map.put(LTE,     "LTE");
    map.put(GTE,     "GTE");
    map.put(EQ,      "EQ");
    map.put(NE,      "NE");
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
    operatorMap.put(LT, new Lt());
    operatorMap.put(GT, new Gt());
    operatorMap.put(LTE, new Lte());
    operatorMap.put(GTE, new Gte());
    operatorMap.put(EQ, new Eq());
    operatorMap.put(NE, new Ne());
    
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
    if (val instanceof TimeSeries) {
      // need to make a copy of TimeSeries
      TimeSeries timeSeries1 = (TimeSeries) val;
      TimeSeries timeSeries = new TimeSeries(timeSeries1.getType());
      for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setId(timeSeriesData1.getId());
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(timeSeriesData1.getValue());
        timeSeries.add(timeSeriesData);
      }
      if (timeSeries1.isBase()) {
        // copy Id / Name metadata for 'base' series
        timeSeries.setId(timeSeries1.getId());
        timeSeries.setName(timeSeries1.getName());
      }
      timeSeries.setSource(timeSeries1.getSource());
      timeSeries.setSourceId(timeSeries1.getSourceId());
      timeSeries.setNotes(timeSeries1.getNotes());
      timeSeries.setTitle(timeSeries1.getTitle());
      val = timeSeries;
    }
    Symbol symbolT = symbolTable.get(symbol.getName());
    if (symbolT != null) {
      if (symbolT.isConstant()) {
        throw new Exception("symbol is already defined as a constant: " + symbolT.getName());
      }
      if (symbol.isConstant()) {
        throw new Exception("cannot overwrite symbol with a constant: " + symbol.getName());
      }
    }
    Symbol symbolNew = new Symbol(symbol.getName(), val, symbol.isConstant());
    symbolTable.put(symbolNew.getName(), symbolNew);
    return val;
  }
  
  @Override
  public String toString() {
    return "{" + map.get(type) + ": " + lhs + ", " + rhs + "}"; 
  }
}
