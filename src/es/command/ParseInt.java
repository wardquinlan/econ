package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class ParseInt implements Command {
  @Override
  public String getSummary() {
    return "void    ES:ParseInt(String value);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Parses 'value', a string representation of an int");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "integer value of 'value'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception("invalid value");
    }
    try {
      return Integer.parseInt((String) params.get(0));
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
