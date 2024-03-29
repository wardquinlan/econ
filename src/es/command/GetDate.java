package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class GetDate implements Command {
  @Override
  public String getSummary() {
    return "Object  " + Utils.ROOT_NAMESPACE + "GetDate(Series series, int index);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets date of series 'series' at index 'index'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Date of series at given index";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);

    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }

    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an int");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    int index = (Integer) params.get(1);
    
    if (index < 0 || index >= timeSeries.size()) {
      throw new Exception("out of bounds: " + index);
    }
    return new java.util.Date(timeSeries.get(index).getDate().getTime());
  }
}
