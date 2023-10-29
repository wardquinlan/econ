package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class GetLength implements Command {
  @Override
  public String getSummary() {
    return "int     " + Utils.ROOT_NAMESPACE + "GetLength(String string);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets the length of 'string'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Length of 'string'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    return ((String) params.get(0)).length();
  }
}
