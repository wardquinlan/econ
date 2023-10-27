package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class Normalize implements Command {
  @Override
  public String getSummary() {
    return "Series  " + Utils.ROOT_NAMESPACE + "Normalize(Series seriesCollapsed, Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Normalizes series against seriesCollapsed, prepending with NULLs as needed");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The normalized Series";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + "is not a Series");
    }
    TimeSeries timeSeriesCollapsed = (TimeSeries) params.get(0);
    if ( !(params.get(1) instanceof TimeSeries)) {
      throw new Exception(params.get(1) + "is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(1);
    return Utils.normalize(timeSeriesCollapsed, timeSeries);
  }
}
