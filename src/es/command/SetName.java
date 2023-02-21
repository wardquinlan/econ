package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class SetName implements Command {
  @Override
  public String getSummary() {
    return "void    setName(Series series, String name);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series name to 'name'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getName() != null) {
      Utils.validateIsAdmin();
    }
    String name = (String) params.get(1);
    timeSeries.setName(name);
    return null;
  }
}
