package econ.command;

import java.io.File;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class PrintCommand extends Command {
  public PrintCommand() {
    super("print");
  }
  
  public String getSummary() {
    return "Object print([Object object]);";
  }
  
  public String getDetails() {
    return "  prints from memory:\n" +
           "  - 'object'\n" +
           "  - an empty line if 'object' not supplied\n" +
           "  returns: 'object', or 0 if 'object' not supplied";
  }

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
