package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.parser.Symbol;

public class Log implements Command {
  @Override
  public String getSummary() {
    return "float  log(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the natural logarithm of 'object', where 'object' is:");
    list.add("  - an int");
    list.add("  - a float");
    list.add("  - a series");
    list.add("");
    list.add("Note that the parameter must be strictly > 0");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Natural logarithm";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    Object object = params.get(0);
    if (object instanceof Float) {
      Float value = ((Float) object).floatValue();
      if (value <= 0) {
        throw new Exception("cannot take log of values <= 0");
      }
      return (float) Math.log(value);
    } else if (object instanceof Integer) {
      Float value = ((Integer) object).floatValue();
      if (value <= 0) {
        throw new Exception("cannot take log of values <= 0");
      }
      return (float) Math.log(value);
    } else if (object instanceof TimeSeries) {
      return 0;
    } else {
      throw new Exception("invalid param: " + object);
    }
  }
}
