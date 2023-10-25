package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class SetId implements Command {
  @Override
  public String getSummary() {
    return "void    setId(Series series, int id);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series id to 'id'");
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
    
    if (!(params.get(1) instanceof Integer) && params.get(1) != null) {
      throw new Exception(params.get(1) + " is not an int (or null)");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    Integer id = (Integer) params.get(1);
    if (timeSeries.getId() != null && !timeSeries.getId().equals(id)) {
      log.warn("resetting an id which has already been set: " + timeSeries.getId());
    }
    timeSeries.setId(id);
    for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      timeSeriesData.setId(id);
    }
    return null;
  }
}
