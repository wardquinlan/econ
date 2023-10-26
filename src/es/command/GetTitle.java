package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class GetTitle implements Command {
  @Override
  public String getSummary() {
    return "String  ES:GetTitle(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets series title");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series title";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    return timeSeries.getTitle();
  }
}
