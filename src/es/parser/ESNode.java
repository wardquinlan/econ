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
  public static final int MULT     = 15; // *
  public static final int DIV      = 16; // /
  public static final int EXP      = 17; // ^
  public static final int INCR     = 18; // ++
  public static final int DECR     = 19; // --

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
    map.put(MULT,    "MULT");
    map.put(DIV,     "DIV");
    map.put(EXP,     "EXP");
    map.put(INCR,    "INCR");
    map.put(DECR,    "DECR");
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
    operatorMap.put(MULT, new Mult());
    operatorMap.put(DIV, new Div());
    operatorMap.put(EXP, new Exp());
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
    if (type == INCR || type == DECR) {
      UnaryOperator operator;
      Utils.ASSERT((lhs != null && rhs == null) || (lhs == null && rhs != null), "both/neither lhs/rhs are null");
      if (type == INCR && rhs != null) {
        operator = new PreIncr(symbolTable);
        return operator.exec(rhs);
      } else if (type == INCR && lhs != null) {
        operator = new PostIncr(symbolTable);
        return operator.exec(lhs);
      } else if (type == DECR && rhs != null) {
        operator = new PreDecr(symbolTable);
        return operator.exec(rhs);
      } else if (type == DECR && lhs != null) {
        operator = new PostDecr(symbolTable);
        return operator.exec(lhs);
      } else {
        Utils.ASSERT(false, "unexpected INCR / DECR state");
        return null;
      }
    }
    Operator operator = operatorMap.get(type);
    Utils.ASSERT(operator != null, "operator not found");
    Executor executor = new Executor(operator);
    if (operator instanceof UnaryOperator) {
      Object val2 = (rhs instanceof Evaluable ? ((Evaluable) rhs).evaluate(symbolTable) : rhs);
      return executor.exec(val2);
    }  else {
      Object val1 = (lhs instanceof Evaluable ? ((Evaluable) lhs).evaluate(symbolTable) : lhs);
      if (operator instanceof And && val1 instanceof Boolean && (!(Boolean) val1)) {
        // don't evaluate rhs
        return false;
      }
      if (operator instanceof Or && val1 instanceof Boolean && ((Boolean) val1)) {
        // don't evaluate rhs
        return true;
      }
      Object val2 = (rhs instanceof Evaluable ? ((Evaluable) rhs).evaluate(symbolTable) : rhs);
      if (operator instanceof Eq || operator instanceof Ne) {
        // don't try and generate time series if doing simple comparisons; use the equals() and notEquals() commands
        return ((BinaryOperator) operator).exec(val1, val2);
      }
      if (operator instanceof Plus) {
        // special cases for operator Plus that involve Strings (including Dates)
        if (val1 instanceof String && val2 instanceof TimeSeries) {
          // don't call the evaluator when adding the time series to a string
          return ((BinaryOperator) operator).exec(val1, val2);
        }
        if (val1 instanceof TimeSeries && ((TimeSeries) val1).getType() != TimeSeries.FLOAT) {
          throw new Exception("cannot add to a non-floating point time series: " + val1);
        }
      }
      return executor.exec(val1, val2);
    }
  }
  
  private Object evaluateAssignment(SymbolTable symbolTable) throws Exception {
    Utils.ASSERT(lhs != null, "lhs is null");
    Utils.ASSERT(lhs instanceof Symbol, "lhs is not a Symbol");
    Symbol symbol = (Symbol) lhs;
    Utils.validateRootNameSpaceWrite(symbol.getName());
    Utils.validateSystemFunctionWrite(symbol.getName());
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
      timeSeries.setId(timeSeries1.getId());
      timeSeries.setName(timeSeries1.getName());
      timeSeries.setSource(timeSeries1.getSource());
      timeSeries.setSourceId(timeSeries1.getSourceId());
      timeSeries.setUnits(timeSeries1.getUnits());
      timeSeries.setUnitsShort(timeSeries1.getUnitsShort());
      timeSeries.setFrequency(timeSeries1.getFrequency());
      timeSeries.setFrequencyShort(timeSeries1.getFrequencyShort());
      timeSeries.setNotes(timeSeries1.getNotes());
      timeSeries.setTitle(timeSeries1.getTitle());
      val = timeSeries;
    }
    Utils.symbolConstCheck(symbolTable, symbol);
    Utils.functionReferenceCheck(symbolTable, symbol);
    symbol.setValue(val);
    symbolTable.put(symbol.getName(), symbol);
    return val;
  }
  
  @Override
  public String toString() {
    return "{" + map.get(type) + ": " + lhs + ", " + rhs + "}"; 
  }
}
