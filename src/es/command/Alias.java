package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.SymbolTable;

public class Alias implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Alias(String systemFunction);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates an alias to ES System Function 'systemFunction'");
    return list;
  }

  @Override
  public String getReturns() {
    return null;
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
    FunctionCaller functionCaller = FunctionCaller.getInstance();
    functionCaller.alias((String) params.get(0), (String) params.get(1));
    return null;
  }
}
