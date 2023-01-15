package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class PrintCommand implements Command {
  @Override
  public String getSummary() {
    return "Object print([Object object]);";
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
    return "'object', or 0 if 'object' not supplied";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      System.out.println();
      return 0;
    } else {
      System.out.println(params.get(0).toString());
      return params.get(0);
    }
  }
}
