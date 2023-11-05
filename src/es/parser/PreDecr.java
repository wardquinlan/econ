package es.parser;

public class PreDecr extends IncrDecrOperator {
  public PreDecr(SymbolTable symbolTable) {
    super(symbolTable);
  }
  
  @Override
  public Object exec(Object val) throws Exception {
    if (!(val instanceof Symbol)) {
      throw new Exception("predecr: rhs must be an lvalue: " + val);
    }
    Symbol symbol = (Symbol) val;
    if (symbolTable.get(symbol.getName()) == null) {
      throw new Exception("predecr: rhs unititalized: " + symbol.getName());
    }
    symbol = symbolTable.get(symbol.getName());
    if (!(symbol.getValue() instanceof Integer)) {
      throw new Exception("predecr: rhs is not integral: " + symbol.getName());
    }
    if (symbol.isConstant()) {
      throw new Exception("predecr: rhs is const: " + symbol.getName());
    }
    int value = (Integer) symbol.getValue();
    value--;
    symbol.setValue(value);
    return value;
  }
}
