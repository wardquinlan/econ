package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Normalize implements Command {
  @Override
  public String getSummary() {
    return "Series normalize(Series seriesCollapsed, Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Normalizes series against seriesCollapsed, prepending with NULLs as needed");
    list.add("(Note that this is primarily a test function)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof TimeSeries) || !(params.get(1) instanceof TimeSeries)) {
      throw new Exception("argument(s) is/are not of type Series");
    }
    TimeSeries timeSeriesCollapsed = (TimeSeries) params.get(0);
    TimeSeries timeSeries = (TimeSeries) params.get(1);
    
    return Utils.normalize(timeSeriesCollapsed, timeSeries);
  }
}