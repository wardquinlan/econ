package es.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;

public class FunctionCall implements Evaluable {
  private String name;
  private List<Object> params = new ArrayList<>();
  private File file;

  public FunctionCall(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<Object> getParams() {
    return params;
  }
  
  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public Object evaluate(SymbolTable symbolTable) throws Exception {
    Utils.ASSERT(file != null, "file is null");
    List<Object> params2 = new ArrayList<>();
    for (Object param: params) {
      params2.add((param instanceof Evaluable ? ((Evaluable) param).evaluate(symbolTable) : param));
    }
    FunctionCaller functionCaller = new FunctionCaller();
    Object result = functionCaller.invokeFunction(name, symbolTable, file, params2);
    if (result instanceof Return) {
      result = ((Return) result).getValue();
    }
    return result;
  }
  
  @Override
  public String toString() {
    return "{FunctionCall: " + name + "}";
  }
}
