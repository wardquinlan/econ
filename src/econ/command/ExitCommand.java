package econ.command;

import java.io.File;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class ExitCommand extends Command {
  public ExitCommand() {
    super("exit");
  }
  
  public String getSummary() {
    return "int exit([int code]);";
  }
  
  public String getDetails() {
    return "  exits the application with status code 'code', or 0 if 'code' not supplied\n" +
           "  returns: 'code', or 0 if 'code' not supplied";
  }
  
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      System.exit(0);
    }
    
    int value = Integer.parseInt(params.get(0).toString());
    System.exit(value);
    return 0;
  }
}
