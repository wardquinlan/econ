package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Settings;
import es.core.Utils;
import es.parser.SymbolTable;

public class Version implements Command {
  @Override
  public String getSummary() {
    return "String  ES:Version();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns the version of ES");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The version of ES";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    return Settings.getInstance().getVersion();
  }
}
