package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Collapse implements Command {
  @Override
  public String getSummary() {
    return "Series collapse([Series series, ...]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Collapses 0 or more series into a single series");
    list.add("(Note that this is primarily a test function)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    List<TimeSeries> timeSeriesList = new ArrayList<>();
    for (Object object: params) {
      if (!(object instanceof TimeSeries)) {
        throw new Exception(object + " is not a Series");
      }
      timeSeriesList.add((TimeSeries) object);
    }
    return Utils.collapse(timeSeriesList);
  }
}
