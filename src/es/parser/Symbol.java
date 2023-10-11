package es.parser;

// Note that the concept of a symbol's "value" only has meaning during the evaluation phase.  However, a symbol 
// can still be created by the parser to indicate that a symbol was found.
public class Symbol implements Evaluable {
  private String name;
  private Object value;
  private boolean constant = false;
  
  public Symbol(String name) {
    this(name, null);
  }
  
  public Symbol(String name, Object value) {
    this(name, value, false);
  }
  
  public Symbol(String name, Object value, boolean constant) {
    this.name = name;
    this.value = value;
    this.constant = constant;
  }
  
  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public boolean isConstant() {
    return constant;
  }
  
  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    Symbol symbol = symbolTable.get(name);
    if (symbol == null) {
      throw new Exception("symbol not found: " + name);
    }
    return symbol.getValue();
  }
  
  @Override
  public String toString() {
    return "{Symbol: " + name + "=" + value + "}";
  }
}
