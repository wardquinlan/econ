package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.parser.Symbol;

public class CreateCommand implements Command {
  @Override
  public String getSummary() {
    return "Series create(String name, int type);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates a new series with name 'name' and type 'type', where type is one of:");
    list.add("  1 - create a floating point series");
    list.add("  2 - create a boolean series");
    list.add("  3 - create a date series");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 2) {
      throw new Exception("missing arguments(s)");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception("argument not an integer");
    }
    
    Integer type = (Integer) params.get(1);
    if (type != TimeSeries.TYPE_FLOAT && type != TimeSeries.TYPE_BOOLEAN && type != TimeSeries.TYPE_DATE) {
      throw new Exception("type must be one of " + TimeSeries.TYPE_FLOAT + ", " + TimeSeries.TYPE_BOOLEAN + ", " + TimeSeries.TYPE_DATE);
    }
    
    TimeSeries timeSeries = new TimeSeries(type);
    timeSeries.setName((String) params.get(0));
    timeSeries.setSource("USER");
    return timeSeries;
  }
}
