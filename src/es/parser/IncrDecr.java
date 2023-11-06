package es.parser;

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
    if (!(symbolExisting.getValue() instanceof Integer)) {
      throw new Exception("not integral: " + symbol.getName());
    }
    if (symbolExisting.isConstant()) {
      throw new Exception("const: " + symbol.getName());
    }
    int value = (Integer) symbolExisting.getValue();
    if (node.getType() == ESNode.INCR) {
      if (node.getLhs() != null) {
        // post-increment
        symbol.setValue(value + 1);
        symbolTable.put(symbol.getName(), symbol);
      } else {
        // pre-increment
        value++;
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
      }
    } else if (node.getType() == ESNode.DECR) {
      if (node.getLhs() != null) {
        // post-decrement
        symbol.setValue(value + -1);
        symbolTable.put(symbol.getName(), symbol);
      } else {
        // pre-decrement
        value--;
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
      }
    } else {
      Utils.ASSERT(false, "unsupported node type: " + node.getType());
    }
    return value;
  }
}
