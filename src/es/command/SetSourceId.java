package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class SetSourceId implements Command {
  @Override
  public String getSummary() {
    return "void    setSourceId(String series, String sourceId);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series source id to 'sourceId'");
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
    String sourceId = (String) params.get(1);
    if (sourceId.contains("\n")) {
      throw new Exception("invalid character(s)");
    }
    timeSeries.setSourceId(sourceId);
    return null;
  }
}
