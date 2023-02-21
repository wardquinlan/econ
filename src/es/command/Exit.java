package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.Utils;
import es.parser.Symbol;

public class Exit implements Command {
  @Override
  public String getSummary() {
    return "void    exit([int code]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Exits the application, with exit code 'code' (or 0 if not supplied)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    if (params.size() == 0) {
      System.exit(0);
    }
    
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception(params.get(0) + " is not an int");
    }
    int value = Integer.parseInt(params.get(0).toString());
    System.exit(value);
    return null;
  }
}
