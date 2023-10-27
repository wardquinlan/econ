package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class Exists implements Command {
  @Override
  public String getSummary() {
    return "boolean " + Utils.ROOT_NAMESPACE + "Exists(Object object);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Determines if a series exists in the datastore, using 'object' as either an id or a name");
    return list;
  }

  @Override
  public String getReturns() {
    return "true if the series exists in the datastore; false otherwise";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    TimeSeries timeSeries =  Utils.load(params.get(0));
    return (timeSeries != null);
  }
}
