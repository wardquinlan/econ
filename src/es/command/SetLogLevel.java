package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import es.core.Utils;
import es.parser.SymbolTable;

public class SetLogLevel implements Command {
  @Override
  public String getSummary() {
    return "void    ES:SetLogLevel(int level);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets the logging level to 'level', where 'level' is one of:");
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
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception(params.get(0) + " is not an int");
    }
    int level = (Integer) params.get(0);
    switch(level) {
      case 0:
        Logger.getRootLogger().setLevel(Level.DEBUG);
        break;
      case 1:
        Logger.getRootLogger().setLevel(Level.INFO);
        break;
      case 2:
        Logger.getRootLogger().setLevel(Level.WARN);
        break;
      case 3:
        Logger.getRootLogger().setLevel(Level.ERROR);
        break;
      default:
        throw new Exception("invalid logging level: " + level);
    }
    return null;
  }
}
