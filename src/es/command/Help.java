package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.SymbolTable;

public class Help implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Help([String command]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Prints help for 'command' (if supplied), or prints this screen");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() == 1 && params.get(0) instanceof String) {  
      String cmd = (String) params.get(0);
      Command command = FunctionCaller.getInstance().getCommandMap().get(cmd);
      if (command != null) {
        System.out.println(command.getSummary());
        System.out.println();
        for (String detail: command.getDetails()) {
          System.out.println(detail);
        }
        if (command.getReturns() != null) {
          System.out.println();
          System.out.println("Returns: " + command.getReturns());
        }
        return null;
      }
    }
    for (String name: FunctionCaller.getInstance().getCommandMap().keySet()) {
      Command command = FunctionCaller.getInstance().getCommandMap().get(name);
      if (name.startsWith((Utils.ROOT_NAMESPACE))) {
        System.out.println(command.getSummary());
      }
    }
    return null;
  }
}
