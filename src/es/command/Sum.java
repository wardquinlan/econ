package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Sum implements Command {
  @Override
  public String getSummary() {
    return "Series  sum(Series series, int n);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the 'n'-period moving summation of 'series'");
    list.add("");
    list.add("Note that 0 < n <= size(series)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "'n'-period moving summation of 'series'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getType() != TimeSeries.FLOAT) {
      throw new Exception(params.get(0) + " is not a FLOAT series");
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
      timeSeriesData.setValue(sum);
      timeSeriesAvg.add(timeSeriesData);
    }
    return timeSeriesAvg;
  }
}
