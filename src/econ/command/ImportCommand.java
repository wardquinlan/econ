package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class ImportCommand implements Command {
  @Override
  public String getSummary() {
    return "Series import(\"QTEMPLATE\", String templateFilePath, String name);\n" +
           "Series import(\"QDB\", String dbFilePath);\n" +
           "Series import(\"FRED\", String id);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    return 0;
  }
}
