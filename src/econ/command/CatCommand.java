package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.TimeSeries;
import econ.TimeSeriesDAO;
import econ.Utils;
import econ.parser.Symbol;

public class CatCommand implements Command {
  private static final int TIME_SERIES_COL_WIDTHS[] = {20, 5, 20, 30, 12, 30};
  
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
    System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Symbol", "Id", "Name", "Title", "Source", "Source Id");
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
        timeSeries.getId() == null ? "NULL" : timeSeries.getId().toString(),
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, Utils.stringWithNULL(timeSeries.getName())), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, Utils.stringWithNULL(timeSeries.getTitle())), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, Utils.stringWithNULL(timeSeries.getSource())), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 5, Utils.stringWithNULL(timeSeries.getSourceId())));
    }
    return 0;
  }
}
