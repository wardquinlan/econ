package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.TimeSeries;
import econ.Utils;
import econ.parser.Symbol;

public class CollapseCommand implements Command {
  @Override
  public String getSummary() {
    return "Series collapse([Series series, ...]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Collapses 0 or more series into a single series");
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
        throw new Exception("argument(s) is/are not a series");
      }
      timeSeriesList.add((TimeSeries) object);
    }
    return Utils.collapse(timeSeriesList);
  }
}
