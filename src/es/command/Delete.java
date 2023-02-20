package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class Delete implements Command {
  @Override
  public String getSummary() {
    return "void   delete(Series series, String date);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Deletes observation at 'date' from 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    for(int i = 0; i < timeSeries.size(); i++) {
      TimeSeriesData timeSeriesData = timeSeries.get(i);
      if (timeSeriesData.getDate().equals(date)) {
        timeSeries.getTimeSeriesDataList().remove(i);
        log.info("observation deleted at " + Utils.DATE_FORMAT.format(date));
        return 0;
      }
    }
    log.warn("observation not found: " + Utils.DATE_FORMAT.format(date));
    return null;
  }
}
