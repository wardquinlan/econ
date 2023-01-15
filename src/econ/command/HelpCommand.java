package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;
import econ.Utils;

public class HelpCommand implements Command {
  @Override
  public String getSummary() {
    return "int help([String command]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Prints help for 'command' (if supplied), or prints this screen");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.ASSERT(false, "run method of help() invoked");
    return 0;
  }
}
