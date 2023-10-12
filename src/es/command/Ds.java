package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.FunctionDeclaration;
import es.parser.SymbolTable;

public class Ds implements Command {
  private static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 20, 8};
  
  @Override
  public String getSummary() {
    return "void    ds([function fn]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("With no parameters, lists series in the datastore in a human-readable format.  If the optional parameter 'fn' is supplied,");
    list.add("will iterate through all series in the datastore, calling the supplied function which must take a Series as its only parameter.");
    list.add("Moreover, the supplied function can not return any value.");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    if (params.size() > 0) {
      return runAsIterator(symbolTable, file, params);
    }
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
  
  private Object runAsIterator(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (!(params.get(0) instanceof FunctionDeclaration)) {
      throw new Exception(params.get(0) + " is not a function");
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) params.get(0);
    List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
    FunctionCaller functionCaller = new FunctionCaller();
    for (TimeSeries timeSeries: list) {
      List<Object> list2 = new ArrayList<>();
      list2.add(timeSeries);
      functionCaller.invokeFunction(functionDeclaration.getName(), symbolTable, file, list2);
    }
    return null;
  }
}
