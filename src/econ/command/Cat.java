package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.TimeSeriesDAO;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class Cat implements Command {
  private static final int TIME_SERIES_COL_WIDTHS[] = {20, 5, 20, 30, 8, 12, 30};
  private static final int TIME_SERIES_DATA_COL_WIDTHS[] = {8, 5, 10, 10};
  
  @Override
  public String getSummary() {
    return "int    cat([Series series])";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Lists or displays series in the catalog");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    if (params.size() == 0) {
      System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Symbol", "Id", "Name", "Title", "Type", "Source", "Source Id");
      System.out.printf(Utils.generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
      
      List<String> list = new ArrayList<>();
      for(String key: symbolTable.keySet()) {
        Symbol symbol = symbolTable.get(key);
        if (symbol.getValue() instanceof TimeSeries) {
          list.add(key);
        }
      }
      for (String name: list) {
        TimeSeries timeSeries = (TimeSeries) symbolTable.get(name).getValue();
        System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
          name,
          timeSeries.getId() == null ? "" : timeSeries.getId().toString(),
          Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, Utils.stringWithNULL(timeSeries.getName())), 
          Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, Utils.stringWithNULL(timeSeries.getTitle())),
          timeSeries.getTypeAsString(),
          Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 5, Utils.stringWithNULL(timeSeries.getSource())), 
          Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 6, Utils.stringWithNULL(timeSeries.getSourceId())));
      }
    } else {
      if (!(params.get(0) instanceof TimeSeries)) {
        throw new Exception("'series' is not a Series");
      }
      
      TimeSeries timeSeries = (TimeSeries) params.get(0);
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
    }
    return 0;
  }
}
