package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.parser.Symbol;

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
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    if (params.size() < 2) {
      throw new Exception("missing arguments");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception("'id' is not an int");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    Integer id = (Integer) params.get(1);
    timeSeries.setId(id);
    for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      timeSeriesData.setId(id);
    }
    return 0;
  }
}
