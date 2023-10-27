package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class PChange implements Command {
  @Override
  public String getSummary() {
    return "Series  " + Utils.ROOT_NAMESPACE + "PChange(Series series[, int n]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the 'n'-period percentage change of 'series' (n defaults to 1)");
    list.add("");
    list.add("Note that 0 < n < size(series).  Also note that all series values must be > 0.");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "'n'-period percentage change of 'series'";
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
      float val1 = (float) timeSeries.get(i - n).getValue();
      float val2 = (float) timeSeries.get(i).getValue();
      if (val1 <= 0 || val2 <= 0) {
        throw new Exception("series values must be >= 0");
      }
      float change = (val2 - val1) / val1;
      change *= 100f;
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeries.get(i).getDate());
      timeSeriesData.setValue(change);
      timeSeriesChange.add(timeSeriesData);
    }
    return timeSeriesChange;
  }
}
