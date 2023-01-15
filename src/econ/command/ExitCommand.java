package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class ExitCommand implements Command {
  @Override
  public String getSummary() {
    return "int exit([int code]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Exits the application");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "never returns, but process exits with exit code 'code' (or 0 if 'code' not supplied)";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      System.exit(0);
    }
    
    int value = Integer.parseInt(params.get(0).toString());
    System.exit(value);
    return 0;
  }
}
