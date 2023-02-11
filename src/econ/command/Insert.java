package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class Insert implements Command {
  @Override
  public String getSummary() {
    return "float  insert(Series series, String date, Object value);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Inserts data into a series, where:");
    list.add("");
    list.add("  - 'series' is the series in question");
    list.add("  - 'date' is the date, in the format yyyy-mm-dd");
    list.add("  - 'value' is the value (must be an int, a float, or a boolean");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "float";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() < 2) {
      throw new Exception("missing argument(s)");
    }
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);

    if (!(params.get(1) instanceof String)) {
      throw new Exception("'date' is not a string");
    }
    if (((String) params.get(1)).length() != 10) {
      throw new Exception("'date' must be of the format YYYY-MM-DD");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    for(TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      if (timeSeriesData.getDate().equals(date)) {
        throw new Exception("that date already exists");
      }
    }
    
    Object value = null;
    if (timeSeries.getType() == TimeSeries.TYPE_DATE) {
      if (params.size() > 2) {
        throw new Exception("too many arguments");
      }
      value = params.get(1);
    } else if (timeSeries.getType() == TimeSeries.TYPE_FLOAT) {
      if (params.size() < 3) {
        throw new Exception("missing argument(s)");
      }
      if (params.size() > 3) {
        throw new Exception("too many arguments");
      }
      if (params.get(2) instanceof Integer) {
        value = ((Integer) params.get(2)).floatValue();
      } else if (params.get(2) instanceof Float) {
        value = (Float) params.get(2);
      } else {
        throw new Exception("series has type float; 'value' must be either an int or a  float");
      }
    } else if (timeSeries.getType() == TimeSeries.TYPE_BOOLEAN) {
      if (params.size() < 3) {
        throw new Exception("missing argument(s)");
      }
      if (params.size() > 3) {
        throw new Exception("too many arguments");
      }
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
    return timeSeries;
  }
}
