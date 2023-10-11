package es.evaluator;

import es.parser.SymbolTable;

public class Symbol implements Evaluable {
  private String name;
  private Object value;
  private boolean constant = false;
  
  public Symbol(String name) {
    this.name = name;
  }
  
  public Symbol(String name, boolean constant) {
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
    return value;
  }
  
  @Override
  public String toString() {
    return "{Symbol: " + name + "=" + value + "}";
  }
}
