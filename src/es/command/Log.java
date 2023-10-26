package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Log implements Command {
  @Override
  public String getSummary() {
    return "void    ES:Log(int level, String message);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Log the status message 'message' to the logging system, using logging level 'level', where level is one of:");
    list.add("- DEBUG (0)");
    list.add("- INFO  (1)");
    list.add("- WARN  (2)");
    list.add("- ERROR (3)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception(params.get(0) + " is not an int");
    }
    int level = (Integer) params.get(0);
    String message = params.get(1).toString();
    switch(level) {
      case 0:
        log.debug(message);
        break;
      case 1:
        log.info(message);
        break;
      case 2:
        log.warn(message);
        break;
      case 3:
        log.error(message);
        break;
      default:
        throw new Exception("invalid logging level: " + level);
    }
    return null;
  }
}
