package es.evaluator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FunctionCall {
  private String name;
  private List<Object> params = new ArrayList<>();
  private File file;

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
  
  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public String toString() {
    return "{FunctionCall: " + name + "}";
  }
}
