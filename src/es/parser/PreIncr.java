package es.parser;

import es.core.Utils;

public class PreIncr extends IncrDecrOperator {
  private ESNode node;
  
  public PreIncr(SymbolTable symbolTable, ESNode node) {
    super(symbolTable);
    this.node = node;
  }
  
  @Override
  public Object exec(Object val) throws Exception {
    if (!(val instanceof Symbol)) {
      throw new Exception("preincr: rhs must be an lvalue: " + val);
    }
    Symbol symbol = (Symbol) val;
    if (symbolTable.get(symbol.getName()) == null) {
      throw new Exception("preincr: rhs unititalized: " + symbol.getName());
    }
    Symbol symbolExisting = symbolTable.get(symbol.getName());
    if (!(symbolExisting.getValue() instanceof Integer)) {
      throw new Exception("preincr: rhs is not integral: " + symbol.getName());
    }
    if (symbolExisting.isConstant()) {
      throw new Exception("preincr: rhs is const: " + symbol.getName());
    }
    int value = (Integer) symbolExisting.getValue();
    if (node.getType() == ESNode.INCR) {
      if (node.getLhs() != null) {
        // post-increment
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
        value++;
      } else {
        // pre-increment
        value++;
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
      }
    } else if (node.getType() == ESNode.DECR) {
      if (node.getLhs() != null) {
        // post-decrement
        symbol.setValue(value);
        symbolTable.put(symbol.getName(), symbol);
        value--;
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
