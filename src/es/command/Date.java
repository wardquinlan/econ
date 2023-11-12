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
    return "Object  " + Utils.ROOT_NAMESPACE + "Date([Object object[);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("If 'object' is not present, creates and returns today's date");
    list.add("If 'object' is a Series, creates and returns a series of type 'date' whose values are the dates of 'series'.");
    list.add("If 'object' is a String in the formation 'yyyy-mm-dd', creates a new Date object representing said String.");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The new Date or Date series.";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    if (params.size() == 0) {
      return Utils.DATE_FORMAT.parse(Utils.DATE_FORMAT.format(new java.util.Date()));
    }
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
