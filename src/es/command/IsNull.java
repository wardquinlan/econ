package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class IsNull implements Command {
  @Override
  public String getSummary() {
    return "boolean isNull(Object object);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Determines if 'object' is null");
    return list;
  }

  @Override
  public String getReturns() {
    return "true if 'object' is null; false otherwise";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    return (params.get(0) == null);
  }
}
