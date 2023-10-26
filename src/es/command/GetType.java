package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class GetType implements Command {
  @Override
  public String getSummary() {
    return "String  ES:GetType(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns type of 'object' represented as a String:");
    list.add("  - 'null'");
    list.add("  - 'int'");
    list.add("  - 'float'");
    list.add("  - 'boolean'");
    list.add("  - 'String'");
    list.add("  - 'Series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Type of 'object', represented as a String";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    Object object = params.get(0);
    if (object == null) {
      return "null";
    } else if (object instanceof Integer) {
      return "int";
    } else if (object instanceof Float) {
      return "float";
    } else if (object instanceof Boolean) {
      return "boolean";
    } else if (object instanceof String) {
      return "String";
    } else if (object instanceof TimeSeries) {
      return "Series";
    } else {
      throw new Exception("unknown type: " + object);
    }
  }
}
