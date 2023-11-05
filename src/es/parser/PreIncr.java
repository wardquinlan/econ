package es.parser;

public class PreIncr extends IncrDecrOperator {
  public PreIncr(SymbolTable symbolTable) {
    super(symbolTable);
  }
  
  @Override
  public Object exec(Object val1) throws Exception {
    if (!(val1 instanceof Symbol)) {
      throw new Exception("preincr: rhs must be an lvalue: " + val1);
    }
    Symbol symbol = (Symbol) val1;
    if (symbolTable.get(symbol.getName()) == null) {
      throw new Exception("preincr: rhs unititalized: " + symbol.getName());
    }
    symbol = symbolTable.get(symbol.getName());
    if (!(symbol.getValue() instanceof Integer)) {
      throw new Exception("preincr: rhs is not integral: " + symbol.getName());
    }
    int value = (Integer) symbol.getValue();
    value++;
    symbol.setValue(value);
    return value;
  }
}
