package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.parser.Symbol;

public class Average implements Command {
  @Override
  public String getSummary() {
    return "float  average(Series series, int n);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the 'n'-period moving average of 'series'");
    list.add("");
    list.add("Note that 0 < n <= size(series)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "'n'-period Moving average of 'series'";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 2) {
      throw new Exception("missing argument(s)");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    
    if (!(params.get(1) instanceof Integer)) {
      throw new Exception("'n' is not an int");
    }
    int n = (int) params.get(1);
    
    if (n <= 0 || n > timeSeries.size()) {
      throw new Exception("n must be: 0 < n <= size(series)");
    }
    
    TimeSeries timeSeriesAvg = new TimeSeries(TimeSeries.TYPE_FLOAT);
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
}
