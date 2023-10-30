package es.parser;

@SuppressWarnings("serial")
public class ESException extends Exception {
  private Object value;
  
  public ESException(Object value) {
    super(value.toString());
    this.value = value;
  }

  public Object getValue() {
    return value;
  }
}
