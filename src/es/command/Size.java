package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Size implements Command {
  @Override
  public String getSummary() {
    return "int    size(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns series size associated with 'object', where 'object' is:");
    list.add("  - an id");
    list.add("  - a name");
    list.add("  - a Series");
    list.add("");
    list.add("Note that if 'object' is a series, size() returns series size from the catalog; otherwise, size() returns");
    list.add("series size from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series size";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    return timeSeries.size();
  }
}
