package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.Utils;
import econ.parser.Symbol;

public class SetName implements Command {
  @Override
  public String getSummary() {
    return "int    setName(Series series, String name);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series name to 'name'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    
    if (!(params.get(1) instanceof String)) {
      throw new Exception("'name' is not a String");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    String name = (String) params.get(1);
    timeSeries.setName(name);
    return 0;
  }
}
