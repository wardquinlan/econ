package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Defined implements Command {
  @Override
  public String getSummary() {
    return "boolean " + Utils.ROOT_NAMESPACE + "Defined(String symbolName);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Determines if symbolName is defined in any scope");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "true if symbolName is defined in any scope; false otherwise";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    String symbolName = (String) params.get(0);
    return symbolTable.get(symbolName) != null;
  }
}
