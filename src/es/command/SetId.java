package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class SetId implements Command {
  @Override
  public String getSummary() {
    return "int    setId(Series series, int id);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Sets a series id to 'id'");
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
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an int");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getId() != null) {
      throw new Exception("cannot reset an id which has already been set");
    }
    
    Integer id = (Integer) params.get(1);
    timeSeries.setId(id);
    for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      timeSeriesData.setId(id);
    }
    return 0;
  }
}
