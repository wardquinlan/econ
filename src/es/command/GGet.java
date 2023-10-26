package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class GGet implements Command {
  @Override
  public String getSummary() {
    return "Object  ES:GGet(String symbolName);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets object with name 'symbolName' from the global scope");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Object with name 'symbolName' from the global scope, or null if it does not exist";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    String symbolName = (String) params.get(0);
    Symbol symbol = symbolTable.globalGet(symbolName);
    if (symbol != null) {
      return symbol.getValue();
    }
    return null;
  }
}
