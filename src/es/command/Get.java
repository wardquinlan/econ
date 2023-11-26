package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Get implements Command {
  @Override
  public String getSummary() {
    return "Object  " + Utils.ROOT_NAMESPACE + "Get(Series series, Object index);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets value of series 'series' at index 'index', where 'index' is an int, a String, or a Date");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Value of series at given index, or null if not found";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (params.get(1) instanceof Integer) {
      int index = (Integer) params.get(1);
      
      if (index < 0 || index >= timeSeries.size()) {
        throw new Exception("out of bounds: " + index);
      }
      return timeSeries.get(index).getValue();
    }
    
    java.util.Date date;
    if (params.get(1) instanceof String) {
      date = Utils.DATE_FORMAT.parse((String) params.get(1));
    } else if (params.get(1) instanceof java.util.Date) {
      date = (java.util.Date) params.get(1);
    } else {
      throw new Exception(params.get(1) + " is not an int, a String, nor a Date");
    }
    
    for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      if (timeSeriesData.getDate().equals(date)) {
        return timeSeriesData.getValue();
      }
    }
    return null;
  }
}
