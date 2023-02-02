package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.parser.Symbol;

public class Get implements Command {
  @Override
  public String getSummary() {
    return "float  get(Series series, int index);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Gets value of series 'series' at index 'index'");
    return list;
  }
  
  @Override
  public String getReturns() {
    // Note: I hate to introduce concept of NULL in a test function but can
    // consider if it is needed in real code
    return "Value of series at given index if found; otherwise -1";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    if (params.size() < 2) {
      throw new Exception("missing argument(s)");
    }

    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("'series' is not a Series");
    }

    if (!(params.get(1) instanceof Integer)) {
      throw new Exception("'index' is not an int");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    int index = (Integer) params.get(1);
    
    if (index < 0 || index >= timeSeries.size()) {
      throw new Exception("out of bounds: " + index);
    }
    
    Float value = timeSeries.get(index).getValue();
    if (value == null) {
      return -1f;
    }
    
    return value;
  }
}
