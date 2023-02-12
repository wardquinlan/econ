package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.Utils;
import econ.parser.Symbol;

public class Merge implements Command {
  @Override
  public String getSummary() {
    return "int    merge(Series series)";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Merges a series in the catalog into the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getId() == null) {
      throw new Exception("series id not set");
    }
    TimeSeries timeSeriesDS = Utils.load(timeSeries.getId());
    if (timeSeriesDS == null) {
      throw new Exception("series not found");
    }
    return 0;
  }
}
