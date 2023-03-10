package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Offset implements Command {
  @Override
  public String getSummary() {
    return "int     offset(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns offset of 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series offset if it exists, otherwise returns -1";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    return Utils.offset((TimeSeries) params.get(0));
  }
}
