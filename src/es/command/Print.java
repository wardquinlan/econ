package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.Utils;
import es.parser.Symbol;

public class Print implements Command {
  @Override
  public String getSummary() {
    return "void   print([Object object]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Prints:");
    list.add("  - 'object'");
    list.add("  - an empty line if 'object' not supplied");
    return list;
  }

  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    if (params.size() == 0) {
      System.out.println();
    } else {
      System.out.println(params.get(0));
    }
    return null;
  }
}
