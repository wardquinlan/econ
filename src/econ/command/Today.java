package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import econ.core.Utils;
import econ.parser.Symbol;

public class Today implements Command {
  @Override
  public String getSummary() {
    return "String today();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns a string representation of today's date");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Today's date as a string";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("too many arguments");
    }
    return Utils.DATE_FORMAT.format(new Date());
  }
}
