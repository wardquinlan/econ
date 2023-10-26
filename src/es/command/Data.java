package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.FunctionDeclaration;
import es.parser.SymbolTable;

public class Data implements Command {
  private static final int TIME_SERIES_DATA_COL_WIDTHS[] = {8, 5, 10, 10};
  
  @Override
  public String getSummary() {
    return "void    ES:Data(Object object[, function fn]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Displays series data of 'object', where 'object' is one of:");
    list.add("  - a series");
    list.add("  - an id");
    list.add("");
    list.add("If 'object' is an id, series data is displayed directly from the datastore");
    list.add("If the optional function parameter 'fn' is passed, that function is invoked for each data element.");
    list.add("");
    list.add("'fn' must have the signature:");
    list.add("  function f(int idx, String date, Object value);");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 2);
    TimeSeries timeSeries;
    if (params.get(0) instanceof TimeSeries) {
      timeSeries = (TimeSeries) params.get(0);
    } else if (params.get(0) instanceof Integer) {
      timeSeries = TimeSeriesDAO.getInstance().loadSeriesById((Integer) params.get(0));
      if (timeSeries == null) {
        throw new Exception(params.get(0) + " not found");
      }
    } else {
      throw new Exception(params.get(0) + " is neither a Series nor an int");
    }
    if (params.size() > 1) {
      return runAsIterator(timeSeries, symbolTable, file, params);
    }
    
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
  
  private Object runAsIterator(TimeSeries timeSeries, SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (!(params.get(1) instanceof FunctionDeclaration)) {
      throw new Exception(params.get(1) + " is not a function");
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) params.get(1);
    FunctionCaller functionCaller = new FunctionCaller();
    for (int i = 0; i < timeSeries.getTimeSeriesDataList().size(); i++) {
      TimeSeriesData timeSeriesData = timeSeries.getTimeSeriesDataList().get(i);
      List<Object> list2 = new ArrayList<>();
      list2.add(i);
      list2.add(Utils.DATE_FORMAT.format(timeSeriesData.getDate()));
      list2.add(timeSeriesData.getValue());
      functionCaller.invokeFunction(functionDeclaration.getName(), symbolTable, file, list2);
    }
    return null;
  }
}
