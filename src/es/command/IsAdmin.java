package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Settings;
import es.core.Utils;
import es.parser.SymbolTable;

public class IsAdmin implements Command {
  @Override
  public String getSummary() {
    return "boolean isAdmin();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Determines if you are running in administrative mode");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "true if you are running in administrative mode; false otherwise";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    return Settings.getInstance().isAdmin();
  }
}
