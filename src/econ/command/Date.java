package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.BinaryOperator;
import econ.parser.Symbol;

public class Date implements Command {
  @Override
  public String getSummary() {
    return "Series date(Series series);";
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
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("argument not a Series");
    }
    TimeSeries timeSeries1 = (TimeSeries) params.get(0);
    TimeSeries timeSeries = new TimeSeries(TimeSeries.TYPE_DATE);
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(Utils.DATE_FORMAT.format(timeSeriesData1.getDate()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }
}
