package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;
import econ.TimeSeries;
import econ.TimeSeriesData;
import econ.Utils;

public class DataCommand implements Command {
  private static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};
  
  @Override
  public String getSummary() {
    return "int data(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows series data associated with 'object', where 'object' is:");
    list.add("  - an id");
    list.add("  - a name");
    list.add("  - a Series");
    list.add("");
    list.add("Note that if 'object' is a series, data() shows series data from the catalog; otherwise, data() shows");
    list.add("series data from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }

    TimeSeries timeSeries;
    Object object = params.get(0);
    if (object instanceof TimeSeries) {
      timeSeries = (TimeSeries) object;
    } else {
      timeSeries = Utils.load(params.get(0));
      if (timeSeries == null) {
        throw new Exception("time series not found: " + params.get(0));
      }
    }

    System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", "Id", "Date", "Value");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_DATA_COL_WIDTHS) + "\n");
    for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
      System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", 
        timeSeriesData.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 1, Utils.DATE_FORMAT.format(timeSeriesData.getDate())), 
        Utils.generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 2, timeSeriesData.getValue().toString())); 
    }
    return 0;
  }
}
