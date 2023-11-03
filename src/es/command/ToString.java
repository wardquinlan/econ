package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class ToString implements Command {
  @Override
  public String getSummary() {
    return "String  " + Utils.ROOT_NAMESPACE + "ToString(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns 'object' represented as a String");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "'object', represented as a String";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (params.get(0) == null) {
      return "null";
    }
    return params.get(0).toString();
  }
}
