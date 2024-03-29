package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class SetName implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "SetName(Series series, String name);";
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
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    if (!(params.get(1) instanceof String) && params.get(1) != null) {
      throw new Exception(params.get(1) + " is not a String (or null)");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    String name = (String) params.get(1);
    if (timeSeries.getName() != null && !timeSeries.getName().equals(name)) {
      log.warn("resetting a name which has already been set: " + timeSeries.getName());
    }
    if (name == null) {
      timeSeries.setName(null);
      return null;
    }
    if (name.contains("\n")) {
      throw new Exception("invalid character(s)");
    }
    timeSeries.setName(name);
    return null;
  }
}
