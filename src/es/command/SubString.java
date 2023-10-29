package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class SubString implements Command {
  @Override
  public String getSummary() {
    return "boolean " + Utils.ROOT_NAMESPACE + "EndsWith(String string, String end);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns true if 'string' ends with 'end'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "true if 'string' ends with 'end'; otherwise false";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    String string = (String) params.get(0);
    String end = (String) params.get(1);
    return string.endsWith(end);
  }
}
