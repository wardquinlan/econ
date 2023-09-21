package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.SymbolTable;

public class Update implements Command {
  @Override
  public String getSummary() {
    return "void    update(Series series, String date[, Object value]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Updates data in a series, where:");
    list.add("");
    list.add("  - 'series' is the series in question");
    list.add("  - 'date' is the date, in the format yyyy-mm-dd");
    list.add("  - 'value' is the value (must be a boolean, an int or a float");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 3, 3);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getType() != TimeSeries.FLOAT && timeSeries.getType() != TimeSeries.BOOLEAN) {
      throw new Exception("Series must be of type float or boolean");
    }

    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a string");
    }
    if (((String) params.get(1)).length() != 10) {
      throw new Exception("date must be of the format YYYY-MM-DD");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    
    TimeSeriesData timeSeriesData = null;
    for(int i = 0; i < timeSeries.size(); i++) {
      TimeSeriesData timeSeriesDataTmp = timeSeries.get(i);
      if (timeSeriesDataTmp.getDate().equals(date)) {
        timeSeriesData = timeSeriesDataTmp;
        break;
      }
    }
    if (timeSeriesData == null) {
      throw new Exception("date not found in series: " + params.get(1));
    }
    
    Object value = null;
    if (timeSeries.getType() == TimeSeries.FLOAT) {
      if (params.get(2) instanceof Integer) {
        value = ((Integer) params.get(2)).floatValue();
      } else if (params.get(2) instanceof Float) {
        value = (Float) params.get(2);
      } else {
        throw new Exception("series has type float; 'value' must be either an int or a  float");
      }
    } else if (timeSeries.getType() == TimeSeries.BOOLEAN) {
      if (params.get(2) instanceof Boolean) {
        value = (Boolean) params.get(2);
      } else {
        throw new Exception("series has type boolean; 'value' must be a boolean");
      }
    }

    timeSeriesData.setValue(value);
    log.info("observation updated at " + Utils.DATE_FORMAT.format(date));
    return null;
  }
}
