package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class Log implements Command {
  @Override
  public String getSummary() {
    return "float  log(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Calculates the natural logarithm of 'object', where 'object' is:");
    list.add("  - an int");
    list.add("  - a float");
    list.add("  - a series");
    list.add("");
    list.add("Note that the parameter (or all series values) must be strictly > 0");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Natural logarithm";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    Object object = params.get(0);
    if (object instanceof Float) {
      Float value = ((Float) object).floatValue();
      if (value <= 0) {
        throw new Exception("cannot take log of values <= 0");
      }
      return (float) Math.log(value);
    } else if (object instanceof Integer) {
      Float value = ((Integer) object).floatValue();
      if (value <= 0) {
        throw new Exception("cannot take log of values <= 0");
      }
      return (float) Math.log(value);
    } else if (object instanceof TimeSeries) {
      TimeSeries timeSeries = (TimeSeries) object;
      if (timeSeries.getType() != TimeSeries.FLOAT) {
        throw new Exception("can only take log of series with type float");
      }
      TimeSeries timeSeriesLog = new TimeSeries(TimeSeries.FLOAT);
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        // NOTE: May have to deal with offsets here (not sure)
        Float value = (Float) timeSeriesData.getValue();
        if (value <= 0) {
          throw new Exception("cannot take log values <= 0");
        }
        TimeSeriesData timeSeriesDataLog = new TimeSeriesData();
        timeSeriesDataLog.setDate(timeSeriesData.getDate());
        timeSeriesDataLog.setValue((float) Math.log(value));
        timeSeriesLog.add(timeSeriesDataLog);
      }
      return timeSeriesLog;
    } else {
      throw new Exception("invalid param: " + object);
    }
  }
}
