package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.Utils;
import es.parser.Symbol;

public class ExitIf implements Command {
  @Override
  public String getSummary() {
    return "void   exitIf(boolean condition, String message[, int code]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Exits if 'condition' is true, displaying 'message', with exit code 'code' (or 0 if not supplied)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 3);
    if (!(params.get(0) instanceof Boolean)) {
      throw new Exception(params.get(0) + " is not a boolean");
    }
    Boolean condition = (Boolean) params.get(0);
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    String message = (String) params.get(1);
    int code = 0;
    if (params.size() == 3) {
      if (!(params.get(2) instanceof Integer)) {
        throw new Exception(params.get(2) + " is not an int");
      }
      code = (Integer) params.get(2);
    }
    if (!condition) {
      System.out.println(message);
      System.exit(code);
    }
    return null;
  }
}
