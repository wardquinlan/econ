package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class Collapse implements Command {
  @Override
  public String getSummary() {
    return "Series  " + Utils.ROOT_NAMESPACE + "Collapse([Series series, ...]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Collapses 0 or more series into a single series");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The collapsed Series";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
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
