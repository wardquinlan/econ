package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Average implements Command {
  @Override
  public String getSummary() {
    return "Object  " + Utils.ROOT_NAMESPACE + "Average(Series series[, int n]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("If 'n' is present, calculates the n-period moving average of 'series'.  Otherwise, calculates the absolute series average.");
    list.add("");
    list.add("Note that 0 < n <= size(series)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "n-period moving average of 'series' (if 'n' is present), or the absolute series average";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getType() != TimeSeries.FLOAT) {
      throw new Exception(params.get(0) + " is not a FLOAT series");
    }
    if (params.size() == 1) {
      return average(timeSeries);
    }
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an int");
    }
    int n = (int) params.get(1);
    
    if (n <= 0 || n > timeSeries.size()) {
      throw new Exception("n must be: 0 < n <= size(series)");
    }
    
    TimeSeries timeSeriesAvg = new TimeSeries(TimeSeries.FLOAT);
    for (int i = 0; i < timeSeries.size() - n + 1; i++) {
      float sum = 0;
      for (int j = 0; j < n; j++) {
        sum += (float) timeSeries.get(i + j).getValue();
      }
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeries.get(i + n - 1).getDate());
      timeSeriesData.setValue(sum / n);
      timeSeriesAvg.add(timeSeriesData);
    }
    return timeSeriesAvg;
  }
  
  private float average(TimeSeries timeSeries) throws Exception {
    List<TimeSeriesData> list = timeSeries.getTimeSeriesDataList();
    if (list.size() == 0) {
      throw new Exception("series is empty: " + timeSeries);
    }
    float sum = 0;
    for (int i = 0; i < list.size(); i++) {
      sum += (float) list.get(i).getValue();
    }
    return sum / list.size();
  }
}
