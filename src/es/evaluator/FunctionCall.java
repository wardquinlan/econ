package es.evaluator;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall {
  private String name;
  private List<Object> params = new ArrayList<>();

  public FunctionCall(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Object> getParams() {
    return params;
  }
  
  @Override
  public String toString() {
    return "{FunctionCall: " + name + "}";
  }
}
