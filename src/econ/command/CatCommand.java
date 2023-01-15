package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;
import econ.TimeSeries;
import econ.TimeSeriesDAO;
import econ.Utils;

public class CatCommand implements Command {
  @Override
  public String getSummary() {
    return "int cat()";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Lists series in the catalog");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("too many arguments");
    }
    System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source", "Source Id");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
    
    List<TimeSeries> list = new ArrayList<>();
    for(Object key: symbolTable.keySet()) {
      Symbol symbol = symbolTable.get(key);
      if (symbol.getValue() instanceof TimeSeries) {
        list.add((TimeSeries) symbol.getValue());
      }
    }
    for (TimeSeries timeSeries: list) {
      System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
        timeSeries.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, timeSeries.getSource()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, Utils.stringWithNULL(timeSeries.getSourceId())));
    }
    return 0;
  }
}
