package es.parser;

public class PostDecr extends IncrDecrOperator {
  public PostDecr(SymbolTable symbolTable) {
    super(symbolTable);
  }
  
  @Override
  public Object exec(Object val) throws Exception {
    if (!(val instanceof Symbol)) {
      throw new Exception("postdecr: lhs must be an lvalue: " + val);
    }
    Symbol symbol = (Symbol) val;
    if (symbolTable.get(symbol.getName()) == null) {
      throw new Exception("postdecr: lhs unititalized: " + symbol.getName());
    }
    symbol = symbolTable.get(symbol.getName());
    if (!(symbol.getValue() instanceof Integer)) {
      throw new Exception("postdecr: lhs is not integral: " + symbol.getName());
    }
    if (symbol.isConstant()) {
      throw new Exception("predecr: lhs is const: " + symbol.getName());
    }
    int value = (Integer) symbol.getValue();
    symbol.setValue(value - 1);
    return value;
  }
}
