package econ.parser;

public class Symbol {
  private boolean constant;
  private Object value;
  
  public Symbol(Object value) {
    this(value, false);
  }
  
  public Symbol(Object value, boolean constant) {
    this.value = value;
    this.constant = constant;
  }
  
  public boolean isConstant() {
    return constant;
  }
  
  public void setConstant(boolean constant) {
    this.constant = constant;
  }
  
  public Object getValue() {
    return value;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return "SYMBOL=" + value + (constant ? " (constant)" : "");
  }
}
