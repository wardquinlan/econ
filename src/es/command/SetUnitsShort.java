package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class SetUnitsShort implements Command {
  @Override
  public String getSummary() {
    return "void    setUnitsShort(String series, String unitsShort);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets series short units to 'unitsShort'");
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
    
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    String param = (String) params.get(1);
    if (param.contains("\n")) {
      throw new Exception("invalid character(s)");
    }
    timeSeries.setUnitsShort(param);
    return null;
  }
}
