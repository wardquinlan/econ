package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import econ.parser.Symbol;

public class Timestamp implements Command {
  @Override
  public String getSummary() {
    return "String timestamp();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns the current date and time");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The current time";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("too many arguments");
    }
    Date date = new Date();
    return date.toString();
  }
}
