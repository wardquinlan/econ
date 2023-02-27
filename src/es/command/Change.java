package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class Change implements Command {
  @Override
  public String getSummary() {
    return "Series  change(Series series[, int n]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the 'n'-period change of 'series' (n defaults to 1)");
    list.add("");
    list.add("Note that 0 < n < size(series)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "'n'-period moving average of 'series'";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getType() != TimeSeries.FLOAT) {
      throw new Exception(params.get(0) + " is not a FLOAT series");
    }
    
    int n = 1;
    if (params.size() == 2) {
      if (!(params.get(1) instanceof Integer)) {
        throw new Exception(params.get(1) + " is not an int");
      }
      n = (int) params.get(1);
    }
    
    if (n <= 0 || n >= timeSeries.size()) {
      throw new Exception("n must be: 0 < n < size(series)");
    }
    
    TimeSeries timeSeriesChange = new TimeSeries(TimeSeries.FLOAT);
    for (int i = n; i < timeSeries.size(); i++) {
      float change = (float) timeSeries.get(i).getValue() - (float) timeSeries.get(i - n).getValue();
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeries.get(i).getDate());
      timeSeriesData.setValue(change);
      timeSeriesChange.add(timeSeriesData);
    }
    return timeSeriesChange;
  }
}
