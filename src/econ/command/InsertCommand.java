package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import econ.TimeSeries;
import econ.TimeSeriesData;
import econ.Utils;
import econ.parser.Symbol;

public class InsertCommand implements Command {
  @Override
  public String getSummary() {
    return "float insert(Series series, String date, float value);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Inserts data into a series, where:");
    list.add("");
    list.add("  - 'series' is the series in question");
    list.add("  - 'date' is the date, in the format yyyy-mm-dd");
    list.add("  - 'value' is the value");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "float";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 3) {
      throw new Exception("too many arguments");
    }
    if (params.size() == 0) {
      throw new Exception("missing arguments");
    }

    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);

    if (!(params.get(1) instanceof String)) {
      throw new Exception("'date' is not a string");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    for(TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      if (timeSeriesData.getDate().equals(date)) {
        throw new Exception("that date already exists");
      }
    }
    
    float value;
    if (params.get(2) instanceof Integer) {
      value = ((Integer) params.get(2)).floatValue();
    } else if (params.get(2) instanceof Float) {
      value = (Float) params.get(2);
    } else {
      throw new Exception("'value' is neither an int nor a  float");
    }
    TimeSeriesData timeSeriesData = new TimeSeriesData();
    timeSeriesData.setDate(date);
    timeSeriesData.setValue(value);
    timeSeries.add(timeSeriesData);
    Collections.sort(timeSeries.getTimeSeriesDataList());
    return timeSeries;
  }
}
