package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class GetEnv implements Command {
  @Override
  public String getSummary() {
    return "String  " + Utils.ROOT_NAMESPACE + "GetEnv(String name);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets environment variable");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Environment variable";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    return System.getenv((String) params.get(0));
  }
}
