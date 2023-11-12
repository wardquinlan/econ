package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class StdDev implements Command {
  @Override
  public String getSummary() {
    return "Object  " + Utils.ROOT_NAMESPACE + "Stdev(Series series[, int n]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("If 'n' is present, calculates the n-period (moving) standard deviation of 'series'.  Otherwise, calculates the absolute standard deviation.");
    list.add("");
    list.add("Note that 1 < n <= size(series)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "n-period moving standard deviation of 'series' (if 'n' is present), or the absolute standard deviation";
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
      return stddev(timeSeries);
    }
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception(params.get(1) + " is not an int");
    }
    int n = (int) params.get(1);
    
    if (n <= 1 || n > timeSeries.size()) {
      throw new Exception("n must be: 1 < n <= size(series)");
    }
    
    TimeSeries timeSeriesStdev = new TimeSeries(TimeSeries.FLOAT);
    for (int i = 0; i < timeSeries.size() - n + 1; i++) {
      float sum = 0;
      for (int j = 0; j < n; j++) {
        sum += (float) timeSeries.get(i + j).getValue();
      }
      float mean = sum / n;
      
      sum = 0;
      for (int j = 0; j < n; j++) {
        float x = (float) timeSeries.get(i + j).getValue() - mean;
        sum += (x * x);
      }
      float s = (float) Math.sqrt(sum / (n - 1));
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeries.get(i + n - 1).getDate());
      timeSeriesData.setValue(s);
      timeSeriesStdev.add(timeSeriesData);
    }
    
    return timeSeriesStdev;
  }
  
  private float stddev(TimeSeries timeSeries) throws Exception {
    if (timeSeries.size() < 2) {
      throw new Exception("series must have at least 2 observations: " + timeSeries);
    }
    float xbar = Utils.average(timeSeries);
    float sum = 0;
    for (int i = 0; i < timeSeries.size(); i++) {
      float xi = (float) timeSeries.get(i).getValue();
      sum += (xi - xbar) * (xi - xbar);
    }
    float var = sum / (timeSeries.size() - 1);
    return (float) Math.sqrt(var);
  }
}
