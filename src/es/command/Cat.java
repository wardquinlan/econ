package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.FunctionDeclaration;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class Cat implements Command {
  private static final int TIME_SERIES_COL_WIDTHS[] = {20, 5, 20, 30, 8, 12, 20};
  
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Cat([function fn]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("With no parameters, lists series in the catalog in a human-readable format.  If the optional parameter 'fn' is supplied,");
    list.add("will iterate through all series in the catalog, calling the supplied function which must take a Series as its only parameter.");
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
    return null;
  }

  private Object runAsIterator(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (!(params.get(0) instanceof FunctionDeclaration)) {
      throw new Exception(params.get(0) + " is not a function");
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) params.get(0);
    FunctionCaller functionCaller = new FunctionCaller();
    for(String key: symbolTable.keySet()) {
      Symbol symbol = symbolTable.get(key);
      if (symbol.getValue() instanceof TimeSeries) {
        TimeSeries timeSeries = (TimeSeries) symbol.getValue();
        List<Object> list = new ArrayList<>();
        list.add(timeSeries);
        functionCaller.invokeFunction(functionDeclaration.getName(), symbolTable, file, list);
      }
    }
    return null;
  }
}
