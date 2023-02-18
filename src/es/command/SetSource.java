package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class SetSource implements Command {
  @Override
  public String getSummary() {
    return "void   setSource(String series, String source);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series source to 'source'");
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
    String source = (String) params.get(1);
    timeSeries.setSource(source);
    return null;
  }
}
