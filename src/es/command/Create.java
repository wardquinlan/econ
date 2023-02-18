package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Create implements Command {
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
    return "The created Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a string");
    }
    
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an integer");
    }
    
    Integer type = (Integer) params.get(1);
    if (type != TimeSeries.FLOAT && type != TimeSeries.BOOLEAN && type != TimeSeries.DATE) {
      throw new Exception("type must be one of " + TimeSeries.FLOAT + ", " + TimeSeries.BOOLEAN + ", " + TimeSeries.DATE);
    }
    
    TimeSeries timeSeries = new TimeSeries(type);
    timeSeries.setName((String) params.get(0));
    timeSeries.setBase(true);
    return timeSeries;
  }
}
