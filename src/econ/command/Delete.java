package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class Delete implements Command {
  @Override
  public String getSummary() {
    return "int    delete(Series series, String date);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Deletes observation at 'date' from 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (!(params.get(1) instanceof String)) {
      throw new Exception("'date' is not a String");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    for(int i = 0; i < timeSeries.size(); i++) {
      TimeSeriesData timeSeriesData = timeSeries.get(i);
      if (timeSeriesData.getDate().equals(date)) {
        timeSeries.getTimeSeriesDataList().remove(i);
        return 0;
      }
    }
    log.warn("date not found in series: " + params.get(1));
    return 0;
  }
}
