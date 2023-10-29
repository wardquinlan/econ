package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class SubString implements Command {
  @Override
  public String getSummary() {
    return "String  " + Utils.ROOT_NAMESPACE + "SubString(String string, int beginIndex[, int endIndex]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns the substring of 'string', beginning at 'beginIndex', and optionally ending at endIndex - 1");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Substring of 'string";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 3);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    String string = (String) params.get(0);
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an int");
    }
    int beginIndex = (Integer) params.get(1);
    int endIndex = string.length();
    if (params.size() == 3) {
      if (!(params.get(2) instanceof Integer)) {
        throw new Exception(params.get(2) + " is not an int");
      }
      endIndex = (Integer) params.get(2);
    }
    return string.substring(beginIndex, endIndex);
  }
}
