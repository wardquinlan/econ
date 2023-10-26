package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Help implements Command {
  @Override
  public String getSummary() {
    return "void    ES:Help([String command]);";
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
    Utils.ASSERT(false, "run method of help() invoked");
    return null;
  }
}
