package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Symbol;

public class Ds implements Command {
  private static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 20, 8};
  
  @Override
  public String getSummary() {
    return "void    ds();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Lists series in the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source", "Source Id", "Size");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
    List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
    for (TimeSeries timeSeries: list) {
      Integer size = TimeSeriesDAO.getInstance().size(timeSeries);
      System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
        timeSeries.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, Utils.stringWithNULL(timeSeries.getSource())), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, Utils.stringWithNULL(timeSeries.getSourceId())),
        size.toString());
    }
    return null;
  }
}
