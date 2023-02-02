package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.parser.Symbol;

public class Offset implements Command {
  @Override
  public String getSummary() {
    return "int    offset(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns offset of 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series offset if it exists, otherwise returns -1";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    if (params.size() == 0) {
      throw new Exception("missing arguments");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    for (int i = 0; i < timeSeries.size(); i++) {
      if (timeSeries.get(i).getValue() != null) {
        return i;
      }
    }
    return -1;
  }
}
