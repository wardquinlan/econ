package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Function;
import es.parser.OldParser;
import es.parser.ReturnResult;
import es.parser.Symbol;
import es.parser.SymbolTable;
import es.parser.Token;
import es.parser.TokenIterator;

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
    if (!(params.get(0) instanceof Function)) {
      throw new Exception(params.get(0) + " is not a function");
    }
    Function function = (Function) params.get(0);
    if (function.getParams().size() != 1) {
      throw new Exception("wrong number of params in function call (must be 1)");
    }
    if (function.getTokenList().size() > 0) {
      List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
      for (TimeSeries timeSeries: list) {
        SymbolTable childSymbolTable = new SymbolTable(symbolTable);
        childSymbolTable.localPut(function.getParams().get(0), new Symbol(timeSeries));
        OldParser parser = new OldParser(childSymbolTable);
        TokenIterator itr2 = new TokenIterator(function.getTokenList());
        Token tk2 = itr2.next();
        ReturnResult returnResult = parser.parse(tk2, itr2);
        if (returnResult != null && returnResult.getValue() != null) {
          throw new Exception("cannot return anything during iterations: " + returnResult.getValue());
        }
      }
    }
    return null;
  }
}
