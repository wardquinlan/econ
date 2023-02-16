package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.Settings;
import econ.core.Utils;
import econ.parser.Symbol;

public class Version implements Command {
  @Override
  public String getSummary() {
    return "String version()";
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
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    return Settings.getInstance().getVersion();
  }
}
