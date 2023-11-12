package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Date implements Command {
  @Override
  public String getSummary() {
    return "Series  " + Utils.ROOT_NAMESPACE + "Date(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates a series of type 'date' whose values are the dates of 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The new date series";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (params.get(0) instanceof TimeSeries) {
      TimeSeries timeSeries1 = (TimeSeries) params.get(0);
      TimeSeries timeSeries = new TimeSeries(TimeSeries.DATE);
      for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(timeSeriesData1.getDate());
        timeSeries.add(timeSeriesData);
      }
      return timeSeries;
    } else if (params.get(0) instanceof String) {
      return Utils.DATE_FORMAT.parse((String) params.get(0));
    } else {
      throw new Exception(params.get(0) + " is not a Series or a String");
    }
  }
}
