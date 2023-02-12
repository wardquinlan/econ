package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class Data implements Command {
  private static final int TIME_SERIES_DATA_COL_WIDTHS[] = {8, 5, 10, 10};
  
  @Override
  public String getSummary() {
    return "int    data(Object object);";
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
    Utils.validate(params, 1, 1);
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

    System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", "Index", "Id", "Date", "Value");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_DATA_COL_WIDTHS) + "\n");
    for (Integer idx = 0; idx < timeSeries.getTimeSeriesDataList().size(); idx++) {
      TimeSeriesData timeSeriesData = timeSeries.getTimeSeriesDataList().get(idx);
      System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n",
        idx.toString(),
        timeSeriesData.getId() == null ? "NULL" : timeSeriesData.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 2, Utils.DATE_FORMAT.format(timeSeriesData.getDate())), 
        timeSeriesData.getValue() == null ? "NULL" : timeSeriesData.getValue()); 
    }
    return 0;
  }
}
