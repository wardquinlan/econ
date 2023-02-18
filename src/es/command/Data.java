package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class Data implements Command {
  private static final int TIME_SERIES_DATA_COL_WIDTHS[] = {8, 5, 10, 10};
  
  @Override
  public String getSummary() {
    return "void   data(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Displays series data of 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", "Index", "Id", "Date", "Value");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_DATA_COL_WIDTHS) + "\n");
    for (Integer idx = 0; idx < timeSeries.getTimeSeriesDataList().size(); idx++) {
      TimeSeriesData timeSeriesData = timeSeries.getTimeSeriesDataList().get(idx);
      System.out.printf(Utils.generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n",
        idx.toString(),
        timeSeriesData.getId() == null ? "" : timeSeriesData.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 2, Utils.DATE_FORMAT.format(timeSeriesData.getDate())), 
        timeSeriesData.getValue() == null ? "" : timeSeriesData.getValue()); 
    }
    return null;
  }
}
