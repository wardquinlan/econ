package es.parser;

import java.util.Date;

import es.core.TimeSeries;
import es.core.Utils;

public class IncrDecr implements UnaryOperator {
  private SymbolTable symbolTable;
  private ESNode node;
  
  public IncrDecr(SymbolTable symbolTable, ESNode node) {
    this.symbolTable = symbolTable;
    this.node = node;
  }

  @Override
  public int getAssociatedSeriesType() {
    return TimeSeries.NULL;
  }
  
  @Override
  public Object exec(Object val) throws Exception {
    if (!(val instanceof Symbol)) {
      throw new Exception("must be an lvalue: " + val);
    }
    Symbol symbol = (Symbol) val;
    if (symbolTable.get(symbol.getName()) == null) {
      throw new Exception("unititalized: " + symbol.getName());
    }
    Symbol symbolExisting = symbolTable.get(symbol.getName());
    if (!(symbolExisting.getValue() instanceof Integer) && !(symbolExisting.getValue() instanceof Date)) {
      throw new Exception(symbol.getName() + " is not an integer nor a Date");
    }
    if (symbolExisting.isConstant()) {
      throw new Exception("const: " + symbol.getName());
    }
    Object value = symbolExisting.getValue();
    if (node.getType() == ESNode.INCR) {
      if (node.getLhs() != null) {
        // post-increment
        if (value instanceof Integer) {
          symbol.setValue((Integer) value + 1);
        } else {
          symbol.setValue(Utils.addDays((Date) value, 1));
        }
        symbolTable.put(symbol.getName(), symbol);
      } else {
        // pre-increment
        if (value instanceof Integer) {
          value = (Integer) value + 1;
        } else {
          value = Utils.addDays((Date) value, 1);
        }
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
      }
    } else if (node.getType() == ESNode.DECR) {
      if (node.getLhs() != null) {
        // post-decrement
        if (value instanceof Integer) {
          symbol.setValue((Integer) value - 1);
        } else {
          symbol.setValue(Utils.addDays((Date) value, -1));
        }
        symbolTable.put(symbol.getName(), symbol);
      } else {
        // pre-decrement
        if (value instanceof Integer) {
          value = (Integer) value - 1;
        } else {
          value = Utils.addDays((Date) value, -1);
        }
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
      }
    } else {
      Utils.ASSERT(false, "unsupported node type: " + node.getType());
    }
    return value;
  }
}
