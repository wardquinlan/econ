package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.FunctionDeclaration;
import es.parser.SymbolTable;

public class Iterate implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Iterate(function fn[, Object arg1, ...]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Iterates through 'arg1' (and subsequent arguments), calling 'fn' with 'arg1'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() == 0) {
      throw new Exception("missing function");
    }
    if (!(params.get(0) instanceof FunctionDeclaration)) {
      throw new Exception(params.get(0) + " is not a function");
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) params.get(0);
    for(int i = 1; i < params.size(); i++) {
      List<Object> list = new ArrayList<>();
      list.add(params.get(i));
      FunctionCaller.getInstance().invokeFunction(functionDeclaration.getName(), symbolTable, file, list);
    }
    return null;
  }
}
