package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Insert implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Insert(Series series, Object date[, Object value]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Inserts data into a series, where:");
    list.add("");
    list.add("  - 'series' is the series in question");
    list.add("  - 'date' is the date, as either a String (in the format 'yyyy-mm-dd'), or as a Date");
    list.add("  - 'value' is the value (must be an int, a float, or a boolean");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() < 2) {
      throw new Exception("missing argument(s)");
    }
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    Date date;
    if (params.get(1) instanceof String) {
      date = Utils.DATE_FORMAT.parse((String) params.get(1));
    } else if (params.get(1) instanceof Date) {
      date = (Date) params.get(1);
    } else {
      throw new Exception(params.get(1) + " is not a String nor a Date");
    }
    for(TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      if (timeSeriesData.getDate().equals(date)) {
        throw new Exception("that date already exists: " + Utils.DATE_FORMAT.format(date));
      }
    }
    Object value = null;
    if (timeSeries.getType() == TimeSeries.DATE) {
      if (params.size() > 2) {
        throw new Exception("too many arguments");
      }
      value = date;
    } else if (timeSeries.getType() == TimeSeries.FLOAT) {
      Utils.validate(params, 3, 3);
      if (params.get(2) instanceof Integer) {
        value = ((Integer) params.get(2)).floatValue();
      } else if (params.get(2) instanceof Float) {
        value = (Float) params.get(2);
      } else {
        throw new Exception("series has type float; 'value' must be either an int or a float");
      }
    } else if (timeSeries.getType() == TimeSeries.BOOLEAN) {
      Utils.validate(params, 3, 3);
      if (params.get(2) instanceof Boolean) {
        value = (Boolean) params.get(2);
      } else {
        throw new Exception("series has type boolean; 'value' must be a boolean");
      }
    } else {
      Utils.ASSERT(false, "invalid series type: " + timeSeries.getType());
    }

    TimeSeriesData timeSeriesData = new TimeSeriesData();
    timeSeriesData.setId(timeSeries.getId());
    timeSeriesData.setDate(date);
    Utils.ASSERT(value != null, "value is null");
    timeSeriesData.setValue(value);
    timeSeries.add(timeSeriesData);
    Collections.sort(timeSeries.getTimeSeriesDataList());
    log.trace("observation inserted at " + Utils.DATE_FORMAT.format(date));
    return null;
  }
}
